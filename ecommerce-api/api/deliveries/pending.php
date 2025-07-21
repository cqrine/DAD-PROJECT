<?php
require_once __DIR__ . '/../../config/db.php';
require_once __DIR__ . '/../../config/jwt.php';

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

// Get JWT token from header
$authHeader = $_SERVER['HTTP_AUTHORIZATION'] ?? '';
$token = str_replace('Bearer ', '', $authHeader);
$userData = validateJWT($token);

if (!$userData) {
    http_response_code(401);
    echo json_encode(["message" => "Unauthorized"]);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    // Only delivery personnel can access this endpoint
    if ($userData['role'] !== 'delivery') {
        http_response_code(403);
        echo json_encode(["message" => "Forbidden - Delivery personnel only"]);
        exit;
    }
    
    // Get pending deliveries assigned to this user
    $stmt = $pdo->prepare("
        SELECT d.*, o.user_id, o.total_amount, u.username as customer_name
        FROM deliveries d
        JOIN orders o ON d.order_id = o.order_id
        JOIN users u ON o.user_id = u.user_id
        WHERE d.assigned_to = ? AND d.status = 'pending'
    ");
    $stmt->execute([$userData['userId']]);
    $deliveries = $stmt->fetchAll();
    
    http_response_code(200);
    echo json_encode($deliveries);
} else {
    http_response_code(405);
    echo json_encode(["message" => "Method not allowed"]);
}
?>