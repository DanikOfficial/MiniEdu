# 📘 MiniEdu Microservices – Backend Architecture Overview

**MiniEdu** is a modular, backend-focused microservices-based educational platform designed for scalability, security, and asynchronous communication.

---

## 🧰 Tech Stack

### ☁️ Infrastructure & DevOps
- **Docker** – Containerization of microservices
- **Kubernetes** – Container orchestration and scaling
- **AWS (planned)** – Cloud hosting, S3 for media storage, SES for email delivery
- **Redis** – Distributed caching for tokens/sessions
- **Apache Kafka** – Asynchronous event-driven communication
- **PostgreSQL** – Relational database for persistent storage

### 🔧 Backend & Middleware
- **Java 17** – Primary language for microservices
- **Spring Boot** – Base framework for each microservice
- **Spring Security** – Secure access control using JWT
- **gRPC** – Fast inter-service communication
- **Custom Security SDK** – Shared JWT generation and validation
- **Lombok & Java Records** – Reduce boilerplate and ensure immutability

---

## 🧩 Microservices Overview

### 1. 🔐 User Microservice

Handles:
- User registration
- Login
- Email verification (via `isActive` flag)
- JWT generation using Security SDK

Responsibilities:
- Password hashing
- Stores user details in PostgreSQL
- Sends `UserRegisteredEvent` to Kafka for triggering verification emails

Endpoints:
- `POST /register`
- `POST /login`
- `GET /verify?token=...`

---

### 2. 🛡️ Security SDK (Shared Module)

Provides:
- `generateToken(userDetails)` – For issuing JWTs during login
- `validateToken(token)` – For authenticating API requests

Used by:
- User Microservice
- Enrollment Microservice
- Course Microservice

Integrated directly as a dependency in each service.

---

### 3. 📧 Email Microservice

Handles:
- Sending verification emails upon user registration

Responsibilities:
- Listens to `UserRegisteredEvent` from Kafka
- Sends emails using SMTP or AWS SES
- Generates email verification links

Communication:
- Kafka subscriber
- Optional REST endpoint for internal testing

---

### 4. 📝 Enrollment Microservice

Handles:
- Enrolling users into available courses

Responsibilities:
- Validates JWT using Security SDK
- Stores user-course relationships
- May emit enrollment events for analytics

Endpoints:
- `POST /enroll`
- `GET /my-enrollments`

---

### 5. 🎓 Course Microservice

Handles:
- Managing and serving course metadata and materials

Responsibilities:
- CRUD operations for courses
- Streams content (PDFs, videos, etc.)
- Validates JWT before granting access

Endpoints:
- `POST /courses`
- `GET /courses/:id`
- `GET /courses`

---

## 🔁 Inter-Service Communication

| Purpose                        | Type        | Protocol     |
|--------------------------------|-------------|--------------|
| Client ↔ Microservices         | REST        | HTTP         |
| Microservice ↔ Microservice    | Synchronous | gRPC         |
| Event Handling (e.g. emails)   | Async       | Kafka        |

---

## 🔐 JWT-Based Authentication & Security

### JWT Creation
- Occurs after successful login or registration
- Generated via `Security SDK` using user details

### JWT Validation
- Included in `Authorization: Bearer <token>` header
- Validated inside each microservice using `Security SDK`

Security SDK ensures:
- Token signature integrity
- Expiration and issuer validation

---

## 🔄 User Registration Flow

### 1. User submits form → User Microservice
- Validates and stores user
- Sets `isActive = false`
- Emits `UserRegisteredEvent` to Kafka

### 2. Email Microservice receives event
- Generates a secure verification link
- Sends email to the user

### 3. User clicks link → User Microservice
- Verifies token from the link
- Sets `isActive = true`

---

## ✅ Microservice Summary

| Microservice         | Role                             | Uses JWT (Security SDK) | Uses Kafka |
|----------------------|----------------------------------|--------------------------|------------|
| User Service         | User auth, registration, tokens  | ✅                       | ✅         |
| Email Service        | Sends verification emails        | ❌                       | ✅         |
| Enrollment Service   | Manages enrollments              | ✅                       | Optional   |
| Course Service       | Hosts course data/content        | ✅                       | Optional   |
| Security SDK         | Shared JWT logic                 | —                        | —          |

---

## 📁 Project Structure Suggestion


