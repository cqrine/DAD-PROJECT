<?php
require_once __DIR__ . '/../../config/db.php';
require_once __DIR__ . '/../../config/jwt.php';

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: PATCH, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization, X-HTTP-Method-Override");

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' &&
    ($_SERVER['HTTP_X_HTTP_METHOD_OVERRIDE'] ?? '') === 'PATCH') {
    $_SERVER['REQUEST_METHOD'] = 'PATCH';
}

$authHeader = $_SERVER['HTTP_AUTHORIZATION'] ?? '';
$token = str_replace('Bearer ', '', $authHeader);
$userData = validateJWT($token);

if (!$userData || $userData['role'] !== 'delivery') {
    http_response_code(403);
    echo json_encode(["success" => false, "message" => "Forbidden"]);
    exit;
}

$data = json_decode(file_get_contents("php://input"));
if (!empty($data->delivery_id) && !empty($data->status)) {
    $stmt = $pdo->prepare("UPDATE deliveries SET status = ?, delivery_date = NOW() WHERE delivery_id = ? AND assigned_to = ?");
    $success = $stmt->execute([$data->status, $data->delivery_id, $userData['userId']]);

    if ($success) {
        echo json_encode(["success" => true, "message" => "Delivery updated"]);
    } else {
        http_response_code(500);
        echo json_encode(["success" => false, "message" => "Update failed"]);
    }
} else {
    http_response_code(400);
    echo json_encode(["success" => false, "message" => "Invalid input"]);
}
