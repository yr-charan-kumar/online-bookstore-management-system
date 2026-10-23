# Online Bookstore Management System

Spring Boot 3 · Java 17 · Spring Data JPA · PostgreSQL

## Run
1. Create DB: `CREATE DATABASE online_bookstore;`
2. Adjust credentials in `src/main/resources/application.properties` if needed.
3. `mvn spring-boot:run`  (server on http://localhost:9090)

## API
| Area | Endpoints |
|---|---|
| Users | POST /api/users/register, POST /api/users/login, GET /api/users, GET/PUT/DELETE /api/users/{id} |
| Books | POST/GET /api/books, GET/PUT/DELETE /api/books/{id}, GET /api/books/search/title?title=, /search/author?author=, /category/{c} |
| Cart | GET /api/cart/{userId}, POST /api/cart/{userId}/add?bookId=&quantity=, PUT /update?bookId=&quantity=, DELETE /remove/{bookId}, DELETE /clear |
| Orders | POST /api/orders/place/{userId}, GET /api/orders, GET /api/orders/{id}, GET /api/orders/user/{userId}, PUT /api/orders/{id}/status?status=, PUT /api/orders/{id}/cancel |

Order statuses: PLACED, SHIPPED, DELIVERED, CANCELLED. Placing an order deducts stock and empties the cart; cancelling restores stock.
