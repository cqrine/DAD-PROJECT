<?php
require_once __DIR__ . '/../../config/db.php';
require_once __DIR__ . '/../../config/jwt.php';

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: PATCH");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

$authHeader = $_SERVER['HTTP_AUTHORIZATION'] ?? '';
$token = str_replace('Bearer ', '', $authHeader);
$userData = validateJWT($token);

if (!$userData) {
    http_response_code(401);
    echo json_encode(["success" => false, "message" => "Unauthorized"]);
    exit;
}

$orderId = $_GET['id'] ?? null;
$data = json_decode(file_get_contents("php://input"));

$actualMethod = $_SERVER['HTTP_X_HTTP_METHOD_OVERRIDE'] ?? $_SERVER['REQUEST_METHOD'];

if ($actualMethod === 'PATCH' && $orderId && !empty($data->status)) {

    try {
        // Allow customer and delivery roles only
        if (!in_array($userData['role'], ['customer', 'delivery'])) {
            http_response_code(403);
            echo json_encode(["success" => false, "message" => "Forbidden"]);
            exit;
        }

        // Get current status
        $stmt = $pdo->prepare("SELECT status FROM orders WHERE order_id = ?");
        $stmt->execute([$orderId]);
        $currentStatus = $stmt->fetchColumn();

        if (!$currentStatus) {
            http_response_code(404);
            echo json_encode(["success" => false, "message" => "Order not found"]);
            exit;
        }

        // Define valid transitions
        $validTransitions = [
            'pending' => ['Paid'],
            'Paid' => ['shipped'],
            'shipped' => ['delivered']
        ];

        if (!isset($validTransitions[$currentStatus])) {
            http_response_code(400);
            echo json_encode(["success" => false, "message" => "Invalid current status"]);
            exit;
        }

        if (!in_array($data->status, $validTransitions[$currentStatus])) {
            http_response_code(400);
            echo json_encode(["success" => false, "message" => "Invalid status transition"]);
            exit;
        }

        // Update orders table
        $stmt = $pdo->prepare("UPDATE orders SET status = ? WHERE order_id = ?");
        $stmt->execute([$data->status, $orderId]);

        // Set delivery status mapping
        $deliveryStatus = match ($data->status) {
            'Paid' => 'awaiting_dispatch',
            'shipped' => 'in_transit',
            'delivered' => 'delivered',
            default => null
        };

        $deliveryDate = $data->status === 'delivered' ? date('Y-m-d H:i:s') : null;

        // Update deliveries table only if delivery record exists
        $stmt = $pdo->prepare("SELECT COUNT(*) FROM deliveries WHERE order_id = ?");
        $stmt->execute([$orderId]);
        $deliveryExists = $stmt->fetchColumn();

        if ($deliveryExists) {
            $stmt = $pdo->prepare("UPDATE deliveries SET status = ?, delivery_date = ? WHERE order_id = ?");
            $stmt->execute([$deliveryStatus, $deliveryDate, $orderId]);
        }

        http_response_code(200);
        echo json_encode([
            "success" => true,
            "message" => "Order status updated",
            "new_status" => $data->status
        ]);
    } catch (PDOException $e) {
        http_response_code(500);
        echo json_encode([
            "success" => false,
            "message" => "Failed to update order status",
            "error" => $e->getMessage()
        ]);
    }
} else {
    http_response_code(400);
    echo json_encode(["success" => false, "message" => "Invalid request"]);
}
?>
