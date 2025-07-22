<?php
require_once __DIR__ . '/../../config/db.php';
require_once __DIR__ . '/../../config/jwt.php';

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST, PATCH, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization, X-HTTP-Method-Override");

// Handle preflight
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

// Handle PATCH override
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_SERVER['HTTP_X_HTTP_METHOD_OVERRIDE']) && $_SERVER['HTTP_X_HTTP_METHOD_OVERRIDE'] === 'PATCH') {
    $_SERVER['REQUEST_METHOD'] = 'PATCH';
}

// Validate JWT
$authHeader = $_SERVER['HTTP_AUTHORIZATION'] ?? '';
$token = str_replace('Bearer ', '', $authHeader);
$userData = validateJWT($token);

if (!$userData) {
    http_response_code(401);
    echo json_encode(["success" => false, "message" => "Unauthorized"]);
    exit;
}

// Only allow delivery role
if ($userData['role'] !== 'delivery') {
    http_response_code(403);
    echo json_encode(["success" => false, "message" => "Forbidden - Delivery only"]);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] !== 'PATCH') {
    http_response_code(405);
    echo json_encode(['success' => false, 'message' => 'Method not allowed']);
    exit;
}

// Read body
$data = json_decode(file_get_contents("php://input"), true);
$delivery_id = $data['delivery_id'] ?? null;
$status = $data['status'] ?? null;

if (!$delivery_id || !$status) {
    echo json_encode(['success' => false, 'message' => 'Missing data']);
    exit;
}

try {
    // Verify delivery belongs to current user
   $stmt = $pdo->prepare("SELECT order_id FROM deliveries WHERE delivery_id = ?");
   $stmt->execute([$delivery_id]);
    $order_id = $stmt->fetchColumn();

    if (!$order_id) {
        http_response_code(403);
        echo json_encode(['success' => false, 'message' => 'Access denied']);
        exit;
    }

    // Update delivery status
    $delivery_date = ($status === 'delivered') ? date('Y-m-d H:i:s') : null;
    $stmt = $pdo->prepare("UPDATE deliveries SET status = ?, delivery_date = ? WHERE delivery_id = ?");
    $success = $stmt->execute([$status, $delivery_date, $delivery_id]);

    // Also update order status if delivered
    if ($status === 'delivered') {
        $stmt = $pdo->prepare("UPDATE orders SET status = 'delivered' WHERE order_id = ?");
        $stmt->execute([$order_id]);
    }

    echo json_encode(['success' => $success, 'message' => $success ? 'Delivery status updated' : 'Failed to update']);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(['success' => false, 'message' => 'DB Error: ' . $e->getMessage()]);
}
?>