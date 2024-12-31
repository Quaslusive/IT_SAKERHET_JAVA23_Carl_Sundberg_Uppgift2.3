# Capsule Management System

## Overview
The Capsule Management System is a secure application that allows users to register, log in, and manage encrypted messages ("capsules"). Each user can create, view, and decrypt their own capsules, with the server securely storing encrypted data.

## Features
- **User Registration**: Allows users to create an account with a hashed password.
- **Login**: Secure user authentication using JSON Web Tokens (JWT).
- **Create Capsules**: Users can submit messages that are encrypted and stored securely.
- **View Capsules**: View all capsules created by the logged-in user.
- **Decrypt Capsules**: Retrieve decrypted versions of the stored capsules.

## Technologies Used
- **Java**: Core language for client and server development.
- **Spring Boot**: Framework for the server-side application.
- **Gson**: Library for JSON parsing and serialization.
- **JWT (Nimbus)**: Secure user authentication with JSON Web Tokens.
- **MySQL**: Relational database for storing user and capsule data.
- **Maven**: Build and dependency management.

## Architecture
The project is divided into two main components:

1. **Server**
   - Provides RESTful endpoints for user management and capsule operations.
   - Handles encryption, authentication, and data persistence.

2. **Client**
   - Console-based client application.
   - Interacts with the server using HTTP requests.

## Endpoints
### Authentication Endpoints
- `POST /api/auth/register`
  - Request: `{ "email": "user@example.com", "password": "password123" }`
  - Response: `"User registered successfully"`

- `POST /api/auth/login`
  - Request: `{ "email": "user@example.com", "password": "password123" }`
  - Response: JWT Token

### Capsule Endpoints
- `POST /api/capsules/create`
  - Request: `{ "message": "Your secure message" }`
  - Response: `"Capsule created with ID: [id]"`

- `GET /api/capsules`
  - Response: List of all encrypted capsules for the authenticated user.

- `GET /api/capsules/decrypted`
  - Response: List of decrypted capsules for the authenticated user.

## Setup

### Prerequisites
- Java 17+
- MySQL 8.0+
- Maven 3.8+

### Database Configuration
Set up a MySQL database with the following schema:
```sql
CREATE DATABASE capsule_management;

CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE capsules (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  encrypted_message BLOB NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

Debuging:
```properties
email: test@test.com
password: test
```

