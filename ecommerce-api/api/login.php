<?php
require_once __DIR__ . '/../config/db.php';
require_once __DIR__ . '/../config/jwt.php';

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

// Handle OPTIONS preflight request
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

ob_start(); // Capture any accidental output

$data = json_decode(file_get_contents("php://input"));

$response = [];

if (!empty($data->username) && !empty($data->password)) {
    $stmt = $pdo->prepare("SELECT * FROM users WHERE username = ?");
    $stmt->execute([$data->username]);
    $user = $stmt->fetch();

    if ($user && password_verify($data->password, $user['password'])) {
        $token = generateJWT($user['user_id'], $user['username'], $user['role']);
        $response = [
            "success" => true,
            "message" => "Login successful",
            "token" => $token,
            "user_id" => $user['user_id'],
            "role" => $user['role']
        ];
    } else {
        http_response_code(401);
        $response = ["success" => false, "message" => "Invalid username or password"];
    }
} else {
    http_response_code(400);
    $response = ["success" => false, "message" => "Username and password are required"];
}

// Clean up any extra output and ensure only valid JSON is sent
ob_clean();
echo json_encode($response);
exit;