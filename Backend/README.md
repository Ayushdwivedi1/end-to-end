# End-to-End CRUD Application

This is a complete CRUD (Create, Read, Update, Delete) application built with Spring Boot and MySQL.

## Project Structure

```
Backend/
├── end-api/                    # REST API interfaces
│   └── src/
│       └── userRest.java
├── end-service/               # Service layer and implementations
│   └── src/
│       ├── rest/impl/
│       │   └── UserRestImpl.java
│       └── service/
│           ├── UserService.java
│           └── serviceImpl/
│               └── UserServiceImpl.java
└── end-microservice/          # Main Spring Boot application
    ├── src/main/java/com/ayush/end_to_end/
    │   ├── config/
    │   │   └── AppConfig.java
    │   ├── dto/
    │   │   ├── ApiResponse.java
    │   │   └── UserDto.java
    │   ├── entity/
    │   │   └── User.java
    │   ├── exception/
    │   │   ├── GlobalExceptionHandler.java
    │   │   ├── UserAlreadyExistsException.java
    │   │   └── UserNotFoundException.java
    │   ├── mapper/
    │   │   └── UserMapper.java
    │   └── repository/
    │       └── UserRepository.java
    ├── src/main/resources/
    │   └── application.properties
    └── pom.xml
```

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher

## Database Setup

1. Install MySQL if not already installed
2. Create a database (or it will be created automatically):
   ```sql
   CREATE DATABASE end_to_end_db;
   ```
3. Update database credentials in `application.properties` if needed:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=root
   ```

## Running the Application

1. Navigate to the microservice directory:
   ```bash
   cd Backend/end-microservice
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Endpoints

### User Management

#### Create User
```http
POST /users
Content-Type: application/json

{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "1234567890",
    "address": "123 Main St, City, State"
}
```

#### Get User by ID
```http
GET /users/{id}
```

#### Get User by Email
```http
GET /users/email/{email}
```

#### Get All Users
```http
GET /users
```

#### Get Active Users
```http
GET /users/active
```

#### Search Users by Name
```http
GET /users/search?name=John
```

#### Update User
```http
PUT /users/{id}
Content-Type: application/json

{
    "firstName": "John",
    "lastName": "Smith",
    "email": "john.smith@example.com",
    "phoneNumber": "1234567890",
    "address": "456 Oak St, City, State"
}
```

#### Delete User
```http
DELETE /users/{id}
```

#### Deactivate User
```http
PATCH /users/{id}/deactivate
```

#### Activate User
```http
PATCH /users/{id}/activate
```

## Response Format

All API responses follow this format:
```json
{
    "success": true,
    "message": "Operation completed successfully",
    "data": {
        // Response data
    },
    "timestamp": "2024-01-01T12:00:00"
}
```

## Features

- **Complete CRUD Operations**: Create, Read, Update, Delete users
- **Data Validation**: Input validation using Bean Validation
- **Exception Handling**: Global exception handler with proper HTTP status codes
- **Soft Delete**: Users can be deactivated instead of hard deleted
- **Search Functionality**: Search users by name
- **Database Integration**: MySQL with JPA/Hibernate
- **RESTful API**: Standard REST endpoints
- **DTO Pattern**: Data Transfer Objects for API communication
- **Mapper Pattern**: Clean separation between entities and DTOs

## Database Schema

The application automatically creates the following table:

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(15),
    address VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);
```

## Error Handling

The application includes comprehensive error handling:

- **400 Bad Request**: Validation errors
- **404 Not Found**: User not found
- **409 Conflict**: User already exists
- **500 Internal Server Error**: Unexpected errors

## Testing

You can test the API using tools like:
- Postman
- cURL
- Any REST client

Example cURL commands:

```bash
# Create a user
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","email":"john@example.com","phoneNumber":"1234567890"}'

# Get all users
curl http://localhost:8080/users

# Get user by ID
curl http://localhost:8080/users/1
``` 