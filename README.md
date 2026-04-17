# Sale Campaign Management

A Spring Boot REST API for managing products, sale campaigns with discounts, and scheduled price adjustments with history tracking.

## Overview

This application manages product inventory with sale campaign functionality. It allows creating campaigns with product-specific discounts, automatically applies and reverts discounts based on campaign start/end dates via scheduled jobs, and maintains price change history for all products.

## Features

* Product Management
* Sale Campaign Creation
* Campaign Discount Application
* Scheduled Campaign Activation/Deactivation
* Price History Tracking
* Pagination Support
* Batch Insert Operations

## Tech Stack

* Java 17
* Spring Boot 4.0.4
* Spring Data JPA
* Spring Web MVC
* MySQL

## System Design / How It Works

1. Products are saved with title, MRP, currentPrice, discount, and inventory
2. Campaigns are created with title, startDate, endDate, and list of product discounts
3. Scheduler runs every 20 seconds to check campaign dates
4. On campaign start date: applies discount to products, updates currentPrice, records price history, sets status to CURRENT
5. On campaign end date: reverts discount from products, updates currentPrice, records price history, sets status to PAST
6. Price calculation: newPrice = oldPrice × (1 - discount/100)
7. Missing products in campaign creation are collected and reported

## Project Structure

```text
com.example.salecampaignmanagement
├── SaleCampaignManagementApplication.java
├── ENUM
│   └── CampaignStatus.java
├── Controller
│   └── ProductController.java
├── Service
│   └── ProductService.java
├── Scheduler
│   └── CampaignScheduler.java
├── Repository
│   ├── ProductRepo.java
│   ├── CampaignRepo.java
│   ├── CampaignDiscountRepo.java
│   └── PriceHistoryRepo.java
├── Model
│   ├── Product.java
│   ├── Campaign.java
│   ├── CampaignDiscount.java
│   └── PriceHistory.java
└── DTO
    ├── CampaignDTO.java
    ├── DiscountDTO.java
    └── PaginationDTO.java
```

## Setup & Installation

1. Ensure Java 17 and Maven are installed
2. Create MySQL database
3. Configure `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```
4. Run `mvn spring-boot:run`

## API Endpoints

Base path: `http://localhost:8080/products`

### 1) Save Products

* **POST** `/products/saveAll`
* **Request Body**: Array of product objects

```http
POST /products/saveAll
[{"title":"Laptop","mrp":50000,"currentPrice":45000,"discount":10,"inventory":100}]
```

### 2) Get All Products

* **GET** `/products/findAll`

```http
GET /products/findAll
```

### 3) Get Products by Page

* **GET** `/products`
* **Query Params**: page, size

```http
GET /products?page=0&size=10
```

### 4) Create Campaign

* **POST** `/products/createCampaign`
* **Request Body**: Campaign details

```http
POST /products/createCampaign
{
  "title":"Summer Sale",
  "startDate":"2026-04-20",
  "endDate":"2026-04-30",
  "campaignDiscount":[
    {"productId":1,"discount":15}
  ]
}
```

## Database Schema

### `product`

* `product_id` (PK)
* `title`
* `mrp`
* `currentPrice`
* `discount`
* `inventory`

### `compaign`

* `compaign_id` (PK)
* `startDate`
* `endDate`
* `title`
* `status`

### `compaign_discount`

* `discountId` (PK)
* `compaign_id` (FK -> compaign)
* `product_id` (FK -> product)
* `discount`

### `price_history`

* `id` (PK)
* `product_id` (FK -> product)
* `oldPrice`
* `newPrice`
* `changedAt`

## Configuration Notes

* `spring.application.name=sale-campaign-management`
* `spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name`
* `spring.datasource.username=your_username`
* `spring.datasource.password=your_password`
* `spring.jpa.hibernate.ddl-auto=update`
* `spring.jpa.properties.hibernate.jdbc.batch_size=50`
* `spring.jpa.properties.hibernate.order_inserts=true`
* `spring.jpa.properties.hibernate.order_updates=true`

## Future Improvements

* Add authentication and authorization
* Implement campaign status filtering
* Add price history retrieval endpoint
* Support multiple discount types (percentage, fixed)
