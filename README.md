# Library Management System

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue)
![License](https://img.shields.io/badge/License-Apache%202.0-blue)

A comprehensive RESTful API for managing a library system built with Spring Boot. This project demonstrates modern Java development practices, including REST APIs, JPA/Hibernate, validation, exception handling, and comprehensive testing.

## 🚀 Features

- **Book Management**: CRUD operations for books with search functionality
- **Member Management**: Register and manage library members with different membership types
- **Loan Management**: Borrow and return books with automatic fine calculation
- **Fine Calculation**: Strategy pattern implementation for different membership types
  - Regular members: $1.00 per day late
  - Premium members: 5 days grace period, then $0.50 per day late
- **RESTful API**: Well-designed REST endpoints with proper HTTP status codes
- **API Documentation**: Swagger/OpenAPI documentation for all endpoints
- **Validation**: Bean validation for request data
- **Exception Handling**: Global exception handler with consistent error responses
- **Pagination**: Support for paginated results
- **Logging**: Comprehensive logging with SLF4J
- **Testing**: Unit tests, integration tests, and controller tests

## 🛠️ Technologies Used

- **Java 17**
- **Spring Boot 4.0.1**
- **Spring Data JPA** - Database operations
- **H2 Database** - In-memory database for development
- **Swagger/OpenAPI** - API documentation
- **Bean Validation** - Request validation
- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework
- **Maven** - Build tool

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+ (or use the included Maven wrapper)

## 🔧 Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd library-spring
   ```

2. **Build the project**
   ```bash
   ./mvnw clean install
   ```
   Or on Windows:
   ```bash
   mvnw.cmd clean install
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```
   Or on Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```

4. **Access the application**
   - API Base URL: `http://localhost:8081`
   - Swagger UI: `http://localhost:8081/swagger-ui.html`
   - H2 Console: `http://localhost:8081/h2-console`
     - JDBC URL: `jdbc:h2:mem:librarydb`
     - Username: `sa`
     - Password: (leave empty)

## 📚 API Endpoints

### Books

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/books` | Create a new book |
| GET | `/api/books` | Get all books (with pagination) |
| GET | `/api/books/{id}` | Get a book by ID |
| PUT | `/api/books/{id}` | Update a book |
| DELETE | `/api/books/{id}` | Delete a book |
| GET | `/api/books/search/title?q={query}` | Search books by title |
| GET | `/api/books/search/author?q={query}` | Search books by author |

### Members

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/members` | Register a new member |
| GET | `/api/members` | Get all members (with pagination) |
| GET | `/api/members/{id}` | Get a member by ID |
| PUT | `/api/members/{id}` | Update a member |
| DELETE | `/api/members/{id}` | Delete a member |

### Loans

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/loans` | Borrow a book (create a loan) |
| POST | `/api/loans/{id}/return` | Return a book (complete a loan) |
| GET | `/api/loans` | Get all loans (with pagination) |
| GET | `/api/loans/{id}` | Get a loan by ID |
| GET | `/api/loans/member/{memberId}` | Get all loans for a member |
| GET | `/api/loans/member/{memberId}/active` | Get active loans for a member |

## 📖 API Usage Examples

### Create a Book

```bash
curl -X POST http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "genre": "TECHNOLOGY",
    "totalCopies": 5
  }'
```

### Register a Member

```bash
curl -X POST http://localhost:8081/api/members \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "membershipType": "PREMIUM"
  }'
```

### Borrow a Book

```bash
curl -X POST http://localhost:8081/api/loans \
  -H "Content-Type: application/json" \
  -d '{
    "memberId": "member-id-here",
    "bookId": "book-id-here"
  }'
```

### Return a Book

```bash
curl -X POST http://localhost:8081/api/loans/{loanId}/return
```

## 🏗️ Project Structure

```
library-spring/
├── src/
│   ├── main/
│   │   ├── java/com/example/library/
│   │   │   ├── config/          # Configuration classes (Swagger, etc.)
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Custom exceptions and handlers
│   │   │   ├── fine/            # Fine calculation strategies
│   │   │   ├── model/           # Entity classes
│   │   │   ├── repository/      # JPA repositories
│   │   │   └── service/         # Business logic services
│   │   └── resources/
│   │       └── application.properties
│   └── test/                    # Test classes
├── pom.xml
└── README.md
```

## 🧪 Running Tests

```bash
./mvnw test
```

Or on Windows:
```bash
mvnw.cmd test
```

## 🎯 Key Design Patterns

1. **Repository Pattern**: Abstraction of data access layer
2. **Service Layer Pattern**: Business logic separation
3. **DTO Pattern**: Data transfer objects for API requests/responses
4. **Strategy Pattern**: Fine calculation based on membership type
5. **Exception Handling**: Global exception handler with consistent error responses

## 📝 Code Quality Features

- ✅ Comprehensive JavaDoc comments
- ✅ Input validation using Bean Validation
- ✅ Proper exception handling
- ✅ Logging at appropriate levels
- ✅ Unit and integration tests
- ✅ RESTful API design principles
- ✅ Clean code practices

## 🔍 Testing Your Knowledge

This project demonstrates:

1. **Spring Boot Fundamentals**
   - Dependency injection
   - Auto-configuration
   - Spring Data JPA
   - REST controllers

2. **Java Best Practices**
   - Object-oriented design
   - Exception handling
   - Design patterns
   - Clean code principles

3. **API Design**
   - RESTful principles
   - HTTP status codes
   - Request/Response DTOs
   - API documentation

4. **Database Operations**
   - JPA entities
   - Repository pattern
   - Transactions
   - Relationships

5. **Testing**
   - Unit tests
   - Integration tests
   - Mocking
   - Test coverage

## 🚀 Future Enhancements

- [ ] Add authentication and authorization
- [ ] Add email notifications
- [ ] Add book reservations
- [ ] Add reporting features
- [ ] Add database migrations with Flyway
- [ ] Add Docker support
- [ ] Add CI/CD pipeline

## 📄 License

This project is open source and available under the Apache License 2.0.

## 👤 Author

Developed as a portfolio project demonstrating Java and Spring Boot expertise.

---

**Note**: This is a demonstration project for learning and portfolio purposes. For production use, additional security, error handling, and performance optimizations would be required.
