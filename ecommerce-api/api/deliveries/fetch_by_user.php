<?php
require_once '../../config/database.php';
require_once '../../auth/verify_token.php';

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    $user_id = $_GET['user_id'] ?? null;
    if (!$user_id) {
        echo json_encode(['success' => false, 'message' => 'user_id required']);
        exit;
    }

    $stmt = $pdo->prepare("
        SELECT d.delivery_id, o.order_id, u.username AS customer_name, o.status
        FROM deliveries d
        JOIN orders o ON d.order_id = o.order_id
        JOIN users u ON o.user_id = u.user_id
        WHERE d.assigned_to = ?
    ");
    $stmt->execute([$user_id]);
    $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
    echo json_encode(['success' => true, 'data' => $data]);
}
