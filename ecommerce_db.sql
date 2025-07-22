-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 22, 2025 at 08:17 PM
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
  `delivery_user_id` int(11) NOT NULL,
  `status` enum('pending','delivered') DEFAULT 'pending',
  `delivery_date` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `deliveries`
--

INSERT INTO `deliveries` (`delivery_id`, `order_id`, `delivery_user_id`, `status`, `delivery_date`) VALUES
(57, 1020, 17, 'delivered', '2025-07-22 12:02:11'),
(58, 1021, 20, 'delivered', '2025-07-22 12:14:47'),
(59, 1022, 17, 'pending', NULL),
(60, 1023, 20, 'pending', NULL),
(61, 1024, 8, 'pending', NULL),
(62, 1025, 21, 'delivered', '2025-07-22 12:15:00'),
(63, 1026, 17, 'pending', NULL),
(64, 1027, 22, 'pending', NULL),
(65, 1028, 20, 'delivered', '2025-07-22 12:14:35');

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
  `payment_method` varchar(100) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `delivery_address` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `user_id`, `total_amount`, `status`, `created_at`, `payment_method`, `address`, `delivery_address`) VALUES
(1020, 19, 90.00, 'delivered', '2025-07-22 17:51:58', 'Cash', '2, Jalan Mewah, Johor', '2, Jalan Mewah, Johor'),
(1021, 5, 1315.00, 'delivered', '2025-07-22 18:09:34', 'Cash', '1, Jalan Perdana, Melaka', '1, Jalan Perdana, Melaka'),
(1022, 5, 1200.00, '', '2025-07-22 18:10:14', 'TnG e-Wallet', '1, Jalan Perdana, Melaka', '1, Jalan Perdana, Melaka'),
(1023, 5, 3200.00, '', '2025-07-22 18:10:29', 'Cash', '1, Jalan Perdana, Melaka', '1, Jalan Perdana, Melaka'),
(1024, 19, 90.00, '', '2025-07-22 18:11:35', 'Cash', '2, Jalan Mewah, Johor', '2, Jalan Mewah, Johor'),
(1025, 19, 25.00, 'delivered', '2025-07-22 18:11:59', 'Visa Card', '2, Jalan Mewah, Johor', '2, Jalan Mewah, Johor'),
(1026, 19, 45.00, '', '2025-07-22 18:12:17', 'Visa Card', '2, Jalan Mewah, Johor', '2, Jalan Mewah, Johor'),
(1027, 19, 45.00, '', '2025-07-22 18:12:31', 'Cash', '2, Jalan Mewah, Johor', '2, Jalan Mewah, Johor'),
(1028, 23, 90.00, 'delivered', '2025-07-22 18:12:55', 'Cash', '10, Jalan Melur, Kedah', '10, Jalan Melur, Kedah');

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
(70, 1020, 'P004', 1, 90.00),
(71, 1021, 'P001', 1, 1200.00),
(72, 1021, 'P004', 1, 90.00),
(73, 1021, 'P005', 1, 25.00),
(74, 1022, 'P001', 1, 1200.00),
(75, 1023, 'P003', 1, 3200.00),
(76, 1024, 'P004', 1, 90.00),
(77, 1025, 'P005', 1, 25.00),
(78, 1026, 'P002', 1, 45.00),
(79, 1027, 'P002', 1, 45.00),
(80, 1028, 'P004', 1, 90.00);

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
('P001', 'Smartphone', '5G-enabled Android phone', 1200.00, 40, '2025-07-17 22:17:33'),
('P002', 'Wireless Mouse', 'Ergonomic design, USB', 45.00, 91, '2025-07-17 22:17:33'),
('P003', 'Laptop', 'Intel i7, 16GB RAM, 512GB SSD', 3200.00, 12, '2025-07-17 22:17:33'),
('P004', 'Headphones', 'Noise cancelling over-ear', 90.00, 53, '2025-07-17 22:17:33'),
('P005', 'USB-C Charger', 'Fast charging 20W', 25.00, 177, '2025-07-17 22:17:33');

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
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `default_address` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `email`, `password`, `phone`, `role`, `created_at`, `default_address`) VALUES
(5, 'zurine', 'zurine@gmail.com', '$2y$10$z4oVk5M8KQo9qOxTHJUs2e5z2n.pXx3W2N26sqxE7Qcw8ohUXpime', '0138652418', 'customer', '2025-07-19 07:29:32', '1, Jalan Perdana, Melaka'),
(8, 'aishah', 'aishah@gmail.com', '$2y$10$OmJFNUHezfX6DoCHk2E.quQ/uhKlSFrRdGzXYe/KWUer7TTouR8IW', '012345634', 'delivery', '2025-07-21 14:44:31', NULL),
(14, 'alice', 'alice@example.com', '$2y$10$CKzUxeE5PRY6vVUg5A9zUOTM7VNBqPDXL3w8iW1qoz9TlhgYq7e.C', '0111000001', 'customer', '2025-07-22 06:24:27', '12 Jalan Bukit, Melaka'),
(15, 'bob', 'bob@example.com', '$2y$10$CKzUxeE5PRY6vVUg5A9zUOTM7VNBqPDXL3w8iW1qoz9TlhgYq7e.C', '0111000002', 'customer', '2025-07-22 06:24:27', '45 Lorong Mawar, KL'),
(17, 'lisha', 'lisha@gmail.com', '$2y$10$pDGz//ZFKhBDMay3dHHlMuqaYADyecqt7/mxuKZ/cmwLGy1Jx6.Hi', '012345653', 'delivery', '2025-07-22 12:28:50', NULL),
(18, 'alisha', 'alisha@gmail.com', '$2y$10$RA4JGVgRz5ir5K0dzMnAQeHM/slxm2v3bT1oJcKlAJMzPC4.TQSa6', '012345432', 'customer', '2025-07-22 12:37:41', NULL),
(19, 'ena', 'ena@email.com', '$2y$10$X4sl.TAxL.1sIq371wrRJeEesCcnLZM2/Im.pF9THkqfSPdwwCFiu', '012345243', 'customer', '2025-07-22 14:15:28', '2, Jalan Mewah, Johor'),
(20, 'zulaina', 'zulaina@email.com', '$2y$10$8O5zbmTrAdDDfRKGtm7NWOxUu5TXIBZZZdjt2RwifQbs8/5xNjxbm', '012635245', 'delivery', '2025-07-22 14:16:30', NULL),
(21, 'zulisha', 'zulisha@email.com', '$2y$10$Q1CuhU4kCDGEQKYDteUCiuSXJHX7cZTJBSmcVgZcwnMkEiEbawqxa', '012345243', 'delivery', '2025-07-22 14:25:45', NULL),
(22, 'faiz', 'faiz@email.com', '$2y$10$Du9OuaZZYlR24gAHQ9SwX.XxRXUHIOcPplcTApxIwYE7Qoe8WCB7O', '012547251', 'delivery', '2025-07-22 16:34:59', NULL),
(23, 'alia', 'alia@email.com', '$2y$10$9ypUjduSHMJAYVEEXWJazu/3xZn0dVRG2G21PwrJhwtw7J796gIr.', '012736523', 'customer', '2025-07-22 17:10:52', '10, Jalan Melur, Kedah');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `deliveries`
--
ALTER TABLE `deliveries`
  ADD PRIMARY KEY (`delivery_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `assigned_to` (`delivery_user_id`);

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
  MODIFY `delivery_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=66;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1029;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=81;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `deliveries`
--
ALTER TABLE `deliveries`
  ADD CONSTRAINT `deliveries_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  ADD CONSTRAINT `deliveries_ibfk_2` FOREIGN KEY (`delivery_user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `fk_order_items_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_order_items_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
