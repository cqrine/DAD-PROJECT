<?php
require_once __DIR__ . '/../../config/db.php';
require_once __DIR__ . '/../../config/jwt.php';

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: PATCH, POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization, X-HTTP-Method-Override");

// Handle OPTIONS preflight request
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

// Allow PATCH override via POST
if ($_SERVER['REQUEST_METHOD'] === 'POST' &&
    isset($_SERVER['HTTP_X_HTTP_METHOD_OVERRIDE']) &&
    $_SERVER['HTTP_X_HTTP_METHOD_OVERRIDE'] === 'PATCH') {
    $_SERVER['REQUEST_METHOD'] = 'PATCH';
}

// Get JWT token
$authHeader = $_SERVER['HTTP_AUTHORIZATION'] ?? '';
$token = str_replace('Bearer ', '', $authHeader);
$userData = validateJWT($token);

if (!$userData) {
    http_response_code(401);
    echo json_encode(["success" => false, "message" => "Unauthorized"]);
    exit;
}

// Only allow customers
if ($userData['role'] !== 'customer') {
    http_response_code(403);
    echo json_encode(["success" => false, "message" => "Forbidden - Customer only"]);
    exit;
}

$orderId = $_GET['id'] ?? null;
$data = json_decode(file_get_contents("php://input"));

if ($_SERVER['REQUEST_METHOD'] === 'PATCH' &&
    $orderId && !empty($data->status) && !empty($data->payment_method)) {
    
    try {
        // Fetch user default address
        $stmtUser = $pdo->prepare("SELECT default_address FROM users WHERE user_id = ?");
        $stmtUser->execute([$userData['userId']]);
        $userRow = $stmtUser->fetch(PDO::FETCH_ASSOC);

        $addressToUse = $data->address ?? $userRow['default_address'];

        // Update order with payment info and address
        $stmt = $pdo->prepare("
            UPDATE orders 
            SET status = ?, payment_method = ?, address = ?, delivery_address = ?
            WHERE order_id = ? AND user_id = ?
        ");
        $success = $stmt->execute([
            $data->status,
            $data->payment_method,
            $addressToUse,
            $addressToUse,
            $orderId,
            $userData['userId']
        ]);

        if ($success) {
            echo json_encode([
                "success" => true,
                "message" => "Payment confirmed and address updated",
                "order_id" => $orderId,
                "address_used" => $addressToUse
            ]);
        } else {
            http_response_code(500);
            echo json_encode(["success" => false, "message" => "Update failed"]);
        }
    } catch (PDOException $e) {
        http_response_code(500);
        echo json_encode(["success" => false, "message" => "DB Error: " . $e->getMessage()]);
    }
} else {
    http_response_code(400);
    echo json_encode(["success" => false, "message" => "Invalid request"]);
}
