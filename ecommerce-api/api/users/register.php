<?php
require_once __DIR__ . '/../../config/db.php';

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

// Handle OPTIONS preflight request
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

$data = json_decode(file_get_contents("php://input"));

if (!empty($data->username) && !empty($data->email) && !empty($data->password)) {
    // Check if username or email already exists
    $stmt = $pdo->prepare("SELECT * FROM users WHERE username = ? OR email = ?");
    $stmt->execute([$data->username, $data->email]);
    
    if ($stmt->rowCount() > 0) {
        http_response_code(400);
        echo json_encode(["success" => false, "message" => "Username or email already exists"]);
        exit;
    }

// Prepare values
    $hashedPassword = password_hash($data->password, PASSWORD_DEFAULT);
    $phone = $data->phone ?? null;
    $role = isset($data->role) && in_array($data->role, ['customer', 'delivery']) ? $data->role : 'customer';
    $defaultAddress = isset($data->default_address) ? $data->default_address : null;

    // Insert new user
    $stmt = $pdo->prepare("INSERT INTO users (username, email, password, phone, role, default_address) VALUES (?, ?, ?, ?, ?, ?)");

    if ($stmt->execute([$data->username, $data->email, $hashedPassword, $phone, $role, $defaultAddress])) {
        http_response_code(201);
        echo json_encode([
            "success" => true,
            "message" => "User registered successfully",
            "user_id" => $pdo->lastInsertId(),
	"role" => $role
        ]);
    } else {
        http_response_code(500);
        echo json_encode(["success" => false, "message" => "Registration failed"]);
    }
} else {
    http_response_code(400);
    echo json_encode(["success" => false, "message" => "Required fields are missing"]);
}
?>