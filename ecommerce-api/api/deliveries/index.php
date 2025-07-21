<?php
require_once __DIR__ . '/../../config/db.php';
require_once __DIR__ . '/../../config/jwt.php';

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

// Handle OPTIONS preflight request
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

// Get JWT token from header
$authHeader = $_SERVER['HTTP_AUTHORIZATION'] ?? '';
$token = str_replace('Bearer ', '', $authHeader);
$userData = validateJWT($token);

if (!$userData) {
    http_response_code(401);
    echo json_encode(["success" => false, "message" => "Unauthorized"]);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    // Only delivery personnel can access this endpoint
    if ($userData['role'] !== 'delivery') {
        http_response_code(403);
        echo json_encode(["success" => false, "message" => "Forbidden - Delivery personnel only"]);
        exit;
    }
    
    // Get deliveries assigned to this user
    $stmt = $pdo->prepare("
        SELECT d.*, o.user_id, o.total_amount, o.status as order_status, u.username as customer_name
        FROM deliveries d
        JOIN orders o ON d.order_id = o.order_id
        JOIN users u ON o.user_id = u.user_id
        WHERE d.assigned_to = ?
    ");
    $stmt->execute([$userData['userId']]);
    $deliveries = $stmt->fetchAll();
    
    // Get order items for each delivery
    foreach ($deliveries as &$delivery) {
        $stmt = $pdo->prepare("
            SELECT oi.*, p.name 
            FROM order_items oi
            JOIN products p ON oi.product_id = p.product_id
            WHERE oi.order_id = ?
        ");
        $stmt->execute([$delivery['order_id']]);
        $delivery['items'] = $stmt->fetchAll();
    }
    
    http_response_code(200);
    echo json_encode(["success" => true, "deliveries" => $deliveries]);
} else {
    http_response_code(405);
    echo json_encode(["success" => false, "message" => "Method not allowed"]);
}
?>