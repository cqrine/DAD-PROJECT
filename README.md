
# Pantas Express – E-Commerce Delivery System

## Universiti Teknikal Malaysia Melaka  
**Faculty:** Fakulti Teknologi Maklumat dan Komunikasi  
**Course:** BITP 3123 - Distributed Application Development  
**Lecturer:** Muhammad Faheem Mohd Ezani  

### Group Members  
| Name | Matric No | Class |
|------|-----------|-------|
| Nur Alia Munirah Binti Abd Rahim | B032310357 | S2/G1 |
| Zurine Aishah Binti Zulkifli | B032310428 | S2/G1 |
| Nabila Batrisyia Binti Mohd Marzuki | B032310619 | S2/G1 |
| Syaznur Aida Qareen Binti Sahrul Hapidz | B032310594 | S2/G1 |

---

## 📌 Contents

- [Introduction](#introduction)
- [Project Overview](#project-overview)
- [Commercial Value / Third-Party Integration](#commercial-value--third-party-integration)
- [System Architecture](#system-architecture)
- [Backend Application](#backend-application)
- [API Documentation](#api-documentation)
- [Frontend Applications](#frontend-applications)
- [Database Design](#database-design)
- [Business Logic & Data Validation](#business-logic--data-validation)

---

## 🧩 Introduction

### Project Overview

Pantas Express is a distributed e-commerce delivery system designed to simplify products ordering and last-mile delivery. It provides two desktop applications:
- **Customer App** – To sign up, login, browse products, order products, make payment, view receipt.
- **Deliverer App** – To track and complete delivery tasks.

---

## 💡 Commercial Value / Third-Party Integration

This system brings value to SMEs looking to digitalize delivery workflows. Integrated services:
- **XAMPP Authentication** – Secures user login and registration.
- **MySQL Database** – Stores user, product, order, and delivery data.

---

## 🏗️ System Architecture

### High-Level Diagram

<img width="610" height="318" alt="system architecture" src="https://github.com/user-attachments/assets/695e7221-21d8-4009-89f6-283b1caa4d11" />


---

## 🛠 Backend Application

### Technology Stack
- **Java** – Customer & Delivery applications
- **PHP** – REST API
- **Java Swing** – GUI
- **XAMPP** – Server environment
- **MySQL** – Database
- **JSON & Java HTTP Libraries** – Data exchange

---

## 🔌 API Documentation

### 1. List of API Endpoints
| Endpoint                            | Method | Description                                              |
| ----------------------------------- | ------ | -------------------------------------------------------- |
| `/api/users/register.php`           | POST   | Register a new user (customer/deliverer)                 |
| `/api/login.php`                    | POST   | Authenticate a user and return a JWT                     |
| `/api/products/index.php`           | GET    | Retrieve list of available products                      |
| `/api/orders/index.php`             | POST   | Place a new order with items                             |
| `/api/orders/pay.php`               | POST   | Make payment for order                                   |
| `/api/deliveries/assigned.php`      | GET    | Get deliveries assigned to the logged-in delivery staff  |
| `/api/deliveries/update_status.php` | PATCH  | Update delivery status that marked as delivered          |
| `/api/deliveries/mark_delivered.php`| POST   | Mark orders as delivered by deliverer                    |


### 2. Example Request Body
```json
{
  "username": "ena",
  "password": "abc12345"
}
```

### 3. Example Responses
```json
{
    "success": true,
    "message": "Login successful",
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3NTMyMDg3NDgsImV4cCI6MTc1MzIxMjM0OCwiZGF0YSI6eyJ1c2VySWQiOjE5LCJ1c2VybmFtZSI6ImVuYSIsInJvbGUiOiJjdXN0b21lciJ9fQ.jl2n0WT9KL-nwdUp1YVcdGp3Sup5_sfnpqNnQzunv84",
    "user_id": 19,
    "role": "customer"
}
```

```json
{
    "success": false,
    "message": "Invalid username or password"
}
```

### 4. Security Measures
- Authentication & token-based session
- HTTPS enforcement
- Rate limiting
- Role-based access
- SQL injection & XSS prevention

---

## 💻 Frontend Applications

### Customer App

**Purpose:** Provides GUI to sign up, login, browse products, order products, add items to cart, make payment, and view receipt.

**Stack:**
- Java + Swing
- JSON for API exchange
- HTTPConnection for requests
- Integrates with backend endpoints


**API Endpoints Used:**
- `/users/register.php`, `/login.php`, `/products/index.php`, `/orders/index.php`, `orders/pay.php`

---

### Delivery App

**Purpose:** Allows deliverers to sign up, log in, browse orders with different statuses, and update statuses.

**Stack:**
- Java + Swing
- JSON + HTTP
- Integrates with backend endpoints

**API Endpoints Used:**
- `users/register.php`, `/login.php`, `deliveries/assigned.php`, `deliveries/mark_delivered.php`, `deliveries/update_status.php`

---

## 🗃️ Database Design

### ERD Relationships

- `users` ⇨ `orders` (1:N)
- `users` ⇨ `deliveries` (1:1)
- `orders` ⇨ `order_items` (1:N)
- `orders` ⇨ `deliveries` (1:1)
- `products` ⇨ `order_items` (1:N)


### Main Tables

**`users`**
- `user_id`, `username`, `email`, `password`, `phone`, `role`, `created_at`, `default_address`

**`products`**
- `product_id`, `name`, `description`, `price`, `stock`, `created_at`

**`orders`**
- `order_id`, `user_id`, `total_amount`, `status`, `created_at`, `payment_method`, `address`, `delivery_address`

**`order_items`**
- `item_id`, `order_id`, `product_id`, `quantity`, `price`

**`deliveries`**
- `delivery_id`, `order_id`, `delivery_user_id`, `status`, `delivery_date`

---

## 🔐 Business Logic & Data Validation

### Customer App Validation
| Field | Rule |
|-------|------|
| Username | Cannot be empty |
| Password | At least 6 characters, includes letters, numbers, symbols |
| Confirm Password | Must match Password |
| Email | Cannot be empty |
| Phone | 10–11 digits, valid format |
| Role | Must be "customer" |
| Address | Cannot be empty |
| Payment | Must be valid method (TnG, FPX, Visa, Cash) |

### Delivery App Validation
| Field | Rule |
|-------|------|
| Username, Password | Cannot be empty |
| Password | At least 6 characters, includes letters, numbers, symbols |
| Confirm Password | Must match Password |
| Role | Must be "delivery" |
| Order ID | Must be valid int |

### Backend Validation
- Sanitized inputs
- Role enforcement
- SQL Injection protection
- JSON structure validation
- Error handling responses

---

> **📂 Note:** All API responses use JSON format and expect proper HTTP headers. Use Java libraries such as `org.json` to parse and build requests and responses.

---

## 📸 Screenshots & Diagrams

![ecommerce_db_erd](https://github.com/user-attachments/assets/ed979011-c6ed-44e2-b1a1-8363526da9a8)
<img width="691" height="702" alt="UseCaseDiagram" src="https://github.com/user-attachments/assets/54a19a1b-f4a8-4759-9ea9-d5610fc9f3d4" />


---

## Setup and Configuration
### 1. Download Project Files:
- DeliveryApp (Java frontend for delivery staff)
- CustomerApp (Java frontend for customers)
- ecommerce-api (PHP backend API)
- ecommerce_db.sql (Database file)

### 2. Configure Backend (XAMPP):
- Move the ecommerce-api folder to xampp/htdocs/.
- Open XAMPP and start Apache and MySQL.
- Click MySQL Admin to open phpMyAdmin.
- Create a new database and import the ecommerce_db.sql file

### 3. Open Java Frontends:
- Launch Eclipse.
- Open the DeliveryApp and CustomerApp projects in Eclipse.

### 4. Configure API URLs:
- Open the ApiConfig.java file in both apps.
- To run locally on the same laptop as the server:
```json
BASE_URL = "http://localhost/ecommerce-api/api/";
```
- To run on different devices on the same Wi-Fi network:
Replace localhost with your server machine's IPv4 address (e.g., 192.168.1.100):
```java
BASE_URL = "http://192.168.1.100/ecommerce-api/api/";
```
> **📂 Note:** Ensure all devices are connected to the same Wi-Fi network. Allow Apache through the firewall on the server machine. Test the API by visiting http://[IPv4]/ecommerce-api/ from a browser on other devices.
  




