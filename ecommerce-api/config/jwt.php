<?php
require_once __DIR__ . '/../vendor/autoload.php';
use Firebase\JWT\JWT;
use Firebase\JWT\Key;

$secretKey = 'dadproject'; 

function generateJWT($userId, $username, $role) {
    global $secretKey;
    $issuedAt = time();
    $expire = $issuedAt + 3600; // 1 hour expiration
    
    $payload = [
        'iat' => $issuedAt,
        'exp' => $expire,
        'data' => [
            'userId' => $userId,
            'username' => $username,
            'role' => $role
        ]
    ];
    
    return JWT::encode($payload, $secretKey, 'HS256');
}

function validateJWT($jwt) {
    global $secretKey;
    try {
        $decoded = JWT::decode($jwt, new Key($secretKey, 'HS256'));
        return (array) $decoded->data;
    } catch (Exception $e) {
        return false;
    }
}
?>