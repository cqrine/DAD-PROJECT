
# Cepat Express – E-Commerce Delivery System

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

Cepat Express is a distributed e-commerce delivery system designed to simplify food ordering and last-mile delivery. It provides two desktop applications:
- **Customer App** – To browse, order, and track products.
- **Deliverer App** – To accept and complete delivery tasks.

---

## 💡 Commercial Value / Third-Party Integration

This system brings value to SMEs looking to digitalize delivery workflows. Integrated services:
- **XAMPP Authentication** – Secures user login and registration.
- **MySQL Database** – Stores user, product, order, and delivery data.

---

## 🏗️ System Architecture

### High-Level Diagram

*(You can include a diagram image here named `architecture.png`)*

---

## 🛠 Backend Application

### Technology Stack
- **Java** – Customer & Deliverer applications
- **PHP** – REST API
- **Java Swing** – GUI
- **XAMPP** – Server environment
- **MySQL** – Database
- **JSON & Java HTTP Libraries** – Data exchange

---

## 🔌 API Documentation

### 1. List of API Endpoints
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/register` | POST | Register new user |
| `/api/login` | POST | User login |
| `/api/products` | GET | Get product list |
| `/api/orders` | POST | Place an order |
| `/api/orders/{id}` | GET | View order |
| `/api/orders/update` | PUT | Update delivery status |

### 2. Example Request Body
```json
{
  "username": "john_doe",
  "password": "secure123",
  "email": "john@example.com"
}
```

### 3. Example Responses
```json
{
  "status": "success",
  "message": "Order placed successfully",
  "orderId": 1024
}
```

```json
{
  "status": "error",
  "message": "Invalid credentials"
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

**Purpose:** Provides GUI to browse products, order food, and track deliveries.

**Stack:**
- Java + Swing
- JSON for API exchange
- HTTPConnection for requests

**API Endpoints Used:**
- `/register.php`, `/login.php`, `/place_order.php`, `/get_orders.php`, `/update_payment_status.php`

---

### Deliverer App

**Purpose:** Allows delivery riders to log in, accept orders, and update statuses.

**Stack:**
- Java + Swing
- JSON + HTTP
- Integrates with backend endpoints

**API Endpoints Used:**
- `/register.php`, `/login.php`, `/get_orders_rider.php`, `/accept_order.php`, `/mark_delivered.php`

---

## 🗃️ Database Design

### ERD Relationships

- `users` ⇨ `orders` (1:N)
- `orders` ⇨ `order_items` (1:N)
- `orders` ⇨ `deliveries` (1:1)

### Main Tables

**`users`**
- `user_id`, `username`, `email`, `password`, `phone`, `role`, `created_at`

**`products`**
- `product_id`, `name`, `description`, `price`, `stock`, `created_at`

**`orders`**
- `order_id`, `user_id`, `total_amount`, `status`, `created_at`, `payment_method`

**`order_items`**
- `item_id`, `order_id`, `product_id`, `quantity`, `price`

**`deliveries`**
- `delivery_id`, `order_id`, `assigned_to`, `status`, `delivery_date`

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
| Payment | Must be valid method (TnG, FPX, Visa, Cash) |

### Deliverer App Validation
| Field | Rule |
|-------|------|
| Username, Password | Cannot be empty |
| Password | At least 6 characters |
| Confirm Password | Must match Password |
| Role | Must be "rider" |
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

> *(Insert visuals such as use case diagrams, ERD, and flowcharts in the `/images` folder and reference them here)*

---

## 📞 Contact

For further information, contact any of the group members or the supervising lecturer.
