Expense Tracker REST API

This project is a secure, scalable, and feature-rich RESTful API for a personal expense tracking application. It provides a complete backend solution with robust user authentication, data management for expenses and categories, and a clean, layered architecture built with Spring Boot.
Features

    User Authentication: Secure user registration and login functionality using a stateless, token-based system with JSON Web Tokens (JWT).

    Expense Management: Full CRUD (Create, Read, Update, Delete) operations for user expenses.

    Category Management: Full CRUD operations for expense categories. Users can manage their own custom categories.

    Data Aggregation: Endpoints to retrieve aggregated data, such as total expenses for the current month or for any given month/year.

    Role-Based Access Control: Secure endpoints ensuring users can only access and manage their own data.

    Global & Custom Categories: Supports default categories available to all users and custom categories created by individual users.

    Centralized Exception Handling: Provides consistent and meaningful error responses for all API endpoints.

    Input Validation: Server-side validation of all incoming data to ensure data integrity.

Architecture

The application follows a classic layered architecture pattern to ensure separation of concerns and maintainability:

    Controller Layer: Handles incoming HTTP requests, validates input, and delegates business logic to the service layer.

    Service Layer: Contains the core business logic of the application. It coordinates with the repository layer to perform operations.

    Repository Layer: Responsible for data access and persistence using Spring Data JPA.

    Security Layer: Manages authentication and authorization using Spring Security and a custom JWT implementation.

    Domain (Entity/DTO): Defines the data models (JPA Entities) and the data transfer objects (DTOs) used for API communication.

    Mapper: Uses MapStruct for high-performance, boilerplate-free mapping between Entities and DTOs.

Technologies Used

    Backend: Java 17, Spring Boot 3

    Security: Spring Security 6, JSON Web Tokens (JWT)

    Database: Spring Data JPA, Hibernate, MySQL

    API: Spring Web (REST Controllers)

    Tooling: Maven, Lombok

    Mapping: MapStruct

    Validation: Jakarta Bean Validation

API Endpoints

A detailed collection of the API endpoints is available via Postman or can be generated using Swagger/OpenAPI documentation.
Authentication (/api/v1/auth)

    POST /register: Register a new user.

    POST /login: Authenticate a user and receive a JWT.

Expenses (/api/v1/expenses)

    POST /: Create a new expense.

    GET /: Get all expenses for the current month for the authenticated user.

    GET /current-month/total: Get the total expense amount for the current month.

    GET /month/{year}/{month}: Get all expenses for a specific year and month.

    GET /{id}: Get a specific expense by its ID.

    PUT /{id}: Update an existing expense.

    DELETE /{id}: Delete an expense.

    GET /category/{categoryId}/expense: Get all expenses for a specific category.

Categories (/api/v1/category)

    POST /: Create a new category for the authenticated user.

    GET /: Get all categories (global and user-specific).

    GET /{id}: Get a specific category by its ID.

    PUT /{id}: Update a category.

    DELETE /{id}: Delete a category.

Getting Started
Prerequisites

    Java JDK 17 or later

    Maven 3.2+

    MySQL Server

Configuration

    Database:

        Create a MySQL database (e.g., expenseTracker).

        Update the src/main/resources/application.properties file with your database URL, username, and password.

    JWT Secret:

        The jwt.secret in application.properties is a Base64 encoded key. You can generate a new one using an online tool or the following Java code:

        import java.util.Base64;
        import javax.crypto.SecretKey;
        import io.jsonwebtoken.security.Keys;

        public class SecretKeyGenerator {
            public static void main(String[] args) {
                SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
                String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
                System.out.println(base64Key);
            }
        }

        For Production: It is highly recommended to externalize these properties using environment variables or a configuration server rather than leaving them in the properties file.

Running the Application

# Clone the repository
git clone [https://github.com/your-username/ExpenseTracker.git](https://github.com/your-username/ExpenseTracker.git)

# Navigate to the project directory
cd ExpenseTracker

# Build and run the application using Maven
./mvnw spring-boot:run

The application will start on port 5000.