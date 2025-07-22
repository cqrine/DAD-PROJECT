<?php
require_once __DIR__ . '/../../config/db.php';
require_once __DIR__ . '/../../config/jwt.php';

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

// Handle OPTIONS preflight
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
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

// Optional status filter from query string
$status = $_GET['status'] ?? null;

try {
    $sql = "
        SELECT 
            d.delivery_id,
            d.order_id,
            d.status AS delivery_status,
            o.status AS order_status,
            o.total_amount,
            o.created_at,
            o.delivery_address
        FROM deliveries d
        INNER JOIN orders o ON d.order_id = o.order_id
    ";

    $params = [];

   if ($status && $status !== 'all') {
    $sql .= " WHERE d.status = ?";
    $params[] = $status;
}

    $sql .= " ORDER BY d.delivery_id DESC";

    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
    $deliveries = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode([
        "success" => true,
        "deliveries" => $deliveries
    ]);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode([
        "success" => false,
        "message" => "DB Error: " . $e->getMessage()
    ]);
}
