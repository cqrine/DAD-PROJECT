-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 20, 2025 at 12:07 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ecommerce_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `deliveries`
--

CREATE TABLE `deliveries` (
  `delivery_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `assigned_to` int(11) NOT NULL,
  `status` enum('pending','awaiting_dispatch','in_transit','delivered') DEFAULT 'pending',
  `delivery_date` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `deliveries`
--

INSERT INTO `deliveries` (`delivery_id`, `order_id`, `assigned_to`, `status`, `delivery_date`) VALUES
(1, 1, 2, 'pending', NULL),
(2, 2, 3, 'in_transit', NULL),
(3, 3, 2, 'pending', NULL),
(4, 4, 3, 'pending', NULL),
(5, 5, 2, 'pending', NULL),
(6, 6, 3, 'pending', NULL),
(7, 7, 2, 'pending', NULL),
(8, 8, 3, 'pending', NULL),
(9, 9, 3, 'pending', NULL),
(10, 10, 2, 'pending', NULL),
(11, 11, 2, 'pending', NULL),
(12, 12, 3, 'pending', NULL),
(13, 13, 3, 'pending', NULL),
(14, 14, 3, 'pending', NULL),
(15, 15, 3, 'pending', NULL),
(16, 16, 3, 'pending', NULL),
(17, 17, 2, 'pending', NULL),
(18, 18, 2, 'pending', NULL),
(19, 19, 2, 'pending', NULL),
(20, 20, 3, 'pending', NULL),
(21, 21, 2, 'pending', NULL),
(22, 22, 3, 'pending', NULL),
(23, 23, 3, 'pending', NULL),
(24, 24, 2, 'pending', NULL),
(25, 25, 2, 'pending', NULL),
(26, 26, 3, 'pending', NULL),
(27, 27, 2, 'pending', NULL),
(28, 28, 2, 'pending', NULL),
(29, 29, 2, 'pending', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `status` enum('pending','shipped','delivered') DEFAULT 'pending',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `payment_method` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `user_id`, `total_amount`, `status`, `created_at`, `payment_method`) VALUES
(1, 1, 1245.00, 'pending', '2025-07-17 22:17:33', NULL),
(2, 1, 90.00, 'shipped', '2025-07-17 22:17:33', NULL),
(3, 5, 1200.00, 'pending', '2025-07-19 09:21:28', NULL),
(4, 5, 45.00, 'pending', '2025-07-19 09:40:54', NULL),
(5, 5, 3200.00, 'pending', '2025-07-19 09:41:28', NULL),
(6, 5, 90.00, 'pending', '2025-07-19 09:48:09', NULL),
(7, 5, 25.00, 'pending', '2025-07-19 09:53:31', NULL),
(8, 5, 3200.00, 'pending', '2025-07-19 11:01:19', NULL),
(9, 5, 25.00, 'pending', '2025-07-19 11:02:06', NULL),
(10, 5, 1200.00, 'pending', '2025-07-19 11:13:47', NULL),
(11, 5, 25.00, 'pending', '2025-07-19 16:20:26', NULL),
(12, 5, 25.00, 'pending', '2025-07-19 17:50:16', NULL),
(13, 5, 90.00, 'pending', '2025-07-19 17:54:37', NULL),
(14, 5, 25.00, 'pending', '2025-07-20 07:23:16', NULL),
(15, 5, 90.00, 'pending', '2025-07-20 07:28:00', NULL),
(16, 5, 90.00, 'pending', '2025-07-20 07:54:21', NULL),
(17, 5, 90.00, 'pending', '2025-07-20 08:03:28', NULL),
(18, 5, 25.00, 'pending', '2025-07-20 08:04:54', NULL),
(19, 5, 90.00, 'pending', '2025-07-20 08:06:12', NULL),
(20, 5, 25.00, 'pending', '2025-07-20 08:29:12', NULL),
(21, 5, 90.00, 'pending', '2025-07-20 08:51:20', NULL),
(22, 5, 45.00, 'pending', '2025-07-20 08:58:02', NULL),
(23, 5, 3200.00, 'pending', '2025-07-20 09:04:20', NULL),
(24, 5, 25.00, 'pending', '2025-07-20 09:10:57', NULL),
(25, 5, 45.00, '', '2025-07-20 09:13:40', 'Cash'),
(26, 5, 90.00, 'pending', '2025-07-20 09:19:17', NULL),
(27, 5, 25.00, 'pending', '2025-07-20 09:21:17', NULL),
(28, 5, 25.00, 'pending', '2025-07-20 09:24:08', NULL),
(29, 5, 25.00, 'pending', '2025-07-20 09:59:28', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `item_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` varchar(10) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`item_id`, `order_id`, `product_id`, `quantity`, `price`) VALUES
(1, 1, 'P001', 1, 1200.00),
(2, 1, 'P002', 1, 45.00),
(3, 2, 'P004', 1, 90.00),
(4, 3, 'P001', 1, 1200.00),
(5, 4, 'P002', 1, 45.00),
(6, 5, 'P003', 1, 3200.00),
(7, 6, 'P004', 1, 90.00),
(8, 7, 'P005', 1, 25.00),
(9, 8, 'P003', 1, 3200.00),
(10, 9, 'P005', 1, 25.00),
(11, 10, 'P001', 1, 1200.00),
(12, 11, 'P005', 1, 25.00),
(13, 12, 'P005', 1, 25.00),
(14, 13, 'P004', 1, 90.00),
(15, 14, 'P005', 1, 25.00),
(16, 15, 'P004', 1, 90.00),
(17, 16, 'P004', 1, 90.00),
(18, 17, 'P004', 1, 90.00),
(19, 18, 'P005', 1, 25.00),
(20, 19, 'P004', 1, 90.00),
(21, 20, 'P005', 1, 25.00),
(22, 21, 'P004', 1, 90.00),
(23, 22, 'P002', 1, 45.00),
(24, 23, 'P003', 1, 3200.00),
(25, 24, 'P005', 1, 25.00),
(26, 25, 'P002', 1, 45.00),
(27, 26, 'P004', 1, 90.00),
(28, 27, 'P005', 1, 25.00),
(29, 28, 'P005', 1, 25.00),
(30, 29, 'P005', 1, 25.00);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock` int(11) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `name`, `description`, `price`, `stock`, `created_at`) VALUES
('P001', 'Smartphone', '5G-enabled Android phone', 1200.00, 48, '2025-07-17 22:17:33'),
('P002', 'Wireless Mouse', 'Ergonomic design, USB', 45.00, 97, '2025-07-17 22:17:33'),
('P003', 'Laptop', 'Intel i7, 16GB RAM, 512GB SSD', 3200.00, 17, '2025-07-17 22:17:33'),
('P004', 'Headphones', 'Noise cancelling over-ear', 90.00, 67, '2025-07-17 22:17:33'),
('P005', 'USB-C Charger', 'Fast charging 20W', 25.00, 189, '2025-07-17 22:17:33');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `role` enum('customer','delivery') DEFAULT 'customer',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `email`, `password`, `phone`, `role`, `created_at`) VALUES
(1, 'customer1', 'customer1@example.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '0123456789', 'customer', '2025-07-17 22:17:33'),
(2, 'delivery1', 'delivery1@example.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '0123456789', 'delivery', '2025-07-17 22:17:33'),
(3, 'delivery2', 'delivery2@example.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '0123456789', 'delivery', '2025-07-17 22:17:33'),
(4, 'newuser', 'newuser@example.com', '$2y$10$ZSb89otaeZe.w1BoaGxNruFkgzs2aYWeID11WztTGiMAFqy9/EeBa', '0123456789', 'customer', '2025-07-17 22:30:32'),
(5, 'zurine', 'zurine@gmail.com', '$2y$10$z4oVk5M8KQo9qOxTHJUs2e5z2n.pXx3W2N26sqxE7Qcw8ohUXpime', '0138652418', 'customer', '2025-07-19 07:29:32');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `deliveries`
--
ALTER TABLE `deliveries`
  ADD PRIMARY KEY (`delivery_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `assigned_to` (`assigned_to`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `deliveries`
--
ALTER TABLE `deliveries`
  MODIFY `delivery_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `deliveries`
--
ALTER TABLE `deliveries`
  ADD CONSTRAINT `deliveries_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  ADD CONSTRAINT `deliveries_ibfk_2` FOREIGN KEY (`assigned_to`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  ADD CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
