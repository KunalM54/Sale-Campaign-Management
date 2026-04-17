# Project Title
Sale Campaign Management

## Overview
Sale Campaign Management is a Spring Boot backend project to manage products and run date-based discount campaigns.  
It supports campaign creation, automatic campaign start/end processing, product price updates, and price history tracking.

## Features
- Product management APIs (`saveAll`, `findAll`, paginated fetch).
- Campaign creation with multiple product discounts in one request.
- Campaign status lifecycle: `UPCOMING` → `CURRENT` → `PAST`.
- Scheduler-based campaign processing (runs every 20 seconds).
- Automatic discount apply on campaign start date.
- Automatic discount revert on campaign end date.
- Price history tracking whenever prices change due to campaign actions.
- Pagination support for product listing.

## Tech Stack
- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Scheduling (`@EnableScheduling`, `@Scheduled`)
- MySQL Connector/J
- Maven

## System Design / How It Works
1. Products are created and stored in the `product` table.
2. Campaign is created through `/products/createCampaign` with:
   - `title`
   - `startDate`
   - `endDate`
   - list of `{ productId, discount }`
3. Campaign is saved with initial status `UPCOMING`.
4. Scheduler (`CampaignScheduler`) runs every 20 seconds:
   - Finds campaigns with today as `startDate` and status `UPCOMING`, then applies discount.
   - Finds campaigns with today as `endDate` and status `CURRENT`, then reverts discount.
5. While applying discount:
   - product discount value is increased
   - current price is recalculated
   - price change is stored in `price_history`
   - campaign status becomes `CURRENT`
6. While reverting discount:
   - product discount value is reduced
   - current price is recalculated back
   - price change is stored in `price_history`
   - campaign status becomes `PAST`

## Project Structure
```text
src/main/java/com/sale_compaign_project/Sale/Compaign/Management
├── Controller
│   └── ProductController
├── Service
│   └── ProductService
├── Scheduler
│   └── CampaignScheduler
├── Model
│   ├── Product
│   ├── Campaign
│   ├── CampaignDiscount
│   └── PriceHistory
├── Repository
│   ├── ProductRepo
│   ├── CampaignRepo
│   ├── CampaignDiscountRepo
│   └── PriceHistoryRepo
├── DTO
│   ├── CampaignDTO
│   ├── DiscountDTO
│   └── PaginationDTO
└── ENUM
    └── CampaignStatus
```

## Setup & Installation
1. Install:
   - JDK 17
   - Maven
   - MySQL
2. Clone/open the project.
3. Configure datasource properties for your local MySQL.
4. Build and run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
5. Server starts with default Spring Boot port unless configured otherwise.

## API Endpoints
Base path: `/products`

1. `POST /products/saveAll`  
   Save multiple products.

2. `GET /products/findAll`  
   Fetch all products.

3. `GET /products`  
   Paginated products using Spring `Pageable` query params (`page`, `size`, `sort`).

4. `POST /products/createCampaign`  
   Create campaign with discounts.

### Campaign Request Body (`/products/createCampaign`)
```json
{
  "title": "Festival Sale",
  "startDate": "2026-04-10",
  "endDate": "2026-04-15",
  "campaignDiscount": [
    { "productId": 1, "discount": 10 },
    { "productId": 2, "discount": 15 }
  ]
}
```

## Database Schema
### 1) `product`
- `product_id` (PK)
- `title`
- `mrp`
- `current_price`
- `discount`
- `inventory`

### 2) `compaign`
- `compaign_id` (PK)
- `start_date`
- `end_date`
- `title`
- `status` (`UPCOMING`, `CURRENT`, `PAST`)

### 3) `compaign_discount`
- `discount_id` (PK)
- `compaign_id` (FK → `compaign.compaign_id`)
- `product_id` (FK → `product.product_id`)
- `discount`

### 4) `price_history`
- `id` (PK)
- `product_id` (FK → `product.product_id`)
- `old_price`
- `new_price`
- `changed_at`

### Relationships
- One `Campaign` → Many `CampaignDiscount`
- One `Product` → Many `CampaignDiscount`
- One `Product` → Many `PriceHistory`

## Configuration Notes
- Scheduling is enabled in application class using `@EnableScheduling`.
- Campaign scheduler cron is currently:
  - `*/20 * * * * *` (every 20 seconds).
- Campaign queries are date-based and status-based:
  - start: `start_date = today` and status `UPCOMING`
  - end: `end_date = today` and status `CURRENT`
- MySQL dependency is included in `pom.xml`.

## Future Improvements
- Add request validation for campaign dates and discount ranges.
- Add global exception handling for cleaner API error responses.
- Add API for campaign details/history retrieval.
- Add unit/integration test coverage.
- Externalize scheduler cron and other runtime settings into configuration.
