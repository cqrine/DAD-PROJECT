<?php
require_once __DIR__ . '/../../config/db.php';
require_once __DIR__ . '/../../config/jwt.php';

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PATCH, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization, X-HTTP-Method-Override");

// Handle OPTIONS preflight request
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

// PATCH override support
if ($_SERVER['REQUEST_METHOD'] === 'POST' &&
    isset($_SERVER['HTTP_X_HTTP_METHOD_OVERRIDE']) &&
    $_SERVER['HTTP_X_HTTP_METHOD_OVERRIDE'] === 'PATCH') {
    $_SERVER['REQUEST_METHOD'] = 'PATCH';
}

// JWT Authentication
$authHeader = $_SERVER['HTTP_AUTHORIZATION'] ?? '';
$token = str_replace('Bearer ', '', $authHeader);
$userData = validateJWT($token);

if (!$userData) {
    http_response_code(401);
    echo json_encode(["success" => false, "message" => "Unauthorized"]);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    // Get orders for this user
    $stmt = $pdo->prepare("SELECT * FROM orders WHERE user_id = ?");
    $stmt->execute([$userData['userId']]);
    $orders = $stmt->fetchAll();

    foreach ($orders as &$order) {
        $stmt = $pdo->prepare("
            SELECT oi.*, p.name 
            FROM order_items oi
            JOIN products p ON oi.product_id = p.product_id
            WHERE oi.order_id = ?
        ");
        $stmt->execute([$order['order_id']]);
        $order['items'] = $stmt->fetchAll();
    }

    echo json_encode([
        "success" => true,
        "orders" => $orders
    ]);

} elseif ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = json_decode(file_get_contents("php://input"));

    if (!empty($data->items) && is_array($data->items)) {
        try {
            $pdo->beginTransaction();

            $totalAmount = 0;
            foreach ($data->items as $item) {
                $stmt = $pdo->prepare("SELECT price FROM products WHERE product_id = ?");
                $stmt->execute([$item->product_id]);
                $product = $stmt->fetch();

                if (!$product) {
                    throw new Exception("Product not found");
                }

                $totalAmount += $product['price'] * $item->quantity;
            }

            $stmt = $pdo->prepare("INSERT INTO orders (user_id, total_amount) VALUES (?, ?)");
            $stmt->execute([$userData['userId'], $totalAmount]);
            $orderId = $pdo->lastInsertId();

            foreach ($data->items as $item) {
                $stmt = $pdo->prepare("
                    INSERT INTO order_items (order_id, product_id, quantity, price)
                    VALUES (?, ?, ?, (SELECT price FROM products WHERE product_id = ?))
                ");
                $stmt->execute([$orderId, $item->product_id, $item->quantity, $item->product_id]);

                $stmt = $pdo->prepare("UPDATE products SET stock = stock - ? WHERE product_id = ?");
                $stmt->execute([$item->quantity, $item->product_id]);
            }

            $stmt = $pdo->prepare("
                INSERT INTO deliveries (order_id, assigned_to)
                VALUES (?, (SELECT user_id FROM users WHERE role = 'delivery' ORDER BY RAND() LIMIT 1))
            ");
            $stmt->execute([$orderId]);

            $pdo->commit();

            echo json_encode([
                "success" => true,
                "message" => "Order created successfully",
                "order_id" => $orderId,
                "total_amount" => $totalAmount
            ]);
        } catch (Exception $e) {
            $pdo->rollBack();
            http_response_code(400);
            echo json_encode(["success" => false, "message" => "Order creation failed: " . $e->getMessage()]);
        }
    } else {
        http_response_code(400);
        echo json_encode(["success" => false, "message" => "Invalid order data"]);
    }

} elseif ($_SERVER['REQUEST_METHOD'] === 'PATCH') {
    $data = json_decode(file_get_contents("php://input"));

    if (empty($data->order_id) || empty($data->status) || empty($data->payment_method)) {
        http_response_code(400);
        echo json_encode(["success" => false, "message" => "Missing required fields"]);
        exit;
    }

    try {
        // Verify order belongs to user
        $stmt = $pdo->prepare("SELECT order_id FROM orders WHERE order_id = ? AND user_id = ?");
        $stmt->execute([$data->order_id, $userData['userId']]);
        
        if (!$stmt->fetch()) {
            http_response_code(403);
            echo json_encode(["success" => false, "message" => "Order not found or access denied"]);
            exit;
        }

        $stmt = $pdo->prepare("UPDATE orders SET status = ?, payment_method = ? WHERE order_id = ? AND user_id = ?");
        $stmt->execute([$data->status, $data->payment_method, $data->order_id, $userData['userId']]);

        echo json_encode([
            "success" => true,
            "message" => "Order updated successfully",
            "order_id" => $data->order_id
        ]);
    } catch (Exception $e) {
        http_response_code(500);
        echo json_encode(["success" => false, "message" => "Failed to update order"]);
    }

} else {
    http_response_code(405);
    echo json_encode(["success" => false, "message" => "Method not allowed"]);
}
?>