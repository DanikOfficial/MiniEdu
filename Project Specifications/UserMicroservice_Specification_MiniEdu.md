
# 🧩 User Microservice Specification – MiniEdu

## ⚙️ Tech Stack

- Java 17+
- Spring Boot 3+
- Spring Security + JWT
- Spring Data JPA
- Hibernate Validator (Bean Validation)
- PostgreSQL (or H2 for development)
- Lombok (optional)
- Java Records for DTOs
- Docker (for containerizing)

---

## 🗂 Project Structure

```
com.miniedu.userservice
├── controller
├── dto
├── entity
├── repository
├── security
├── service
│   └── impl
├── exception
└── config
```

---

## 🧑‍💼 User Entity

### Fields:

- `UUID id`
- `String username`
- `String email`
- `String password`
- `UserRole role` (Enum: `USER`, `ADMIN`)
- `LocalDateTime createdAt`

---

## 📦 DTOs (Java `record`s)

- `RegisterRequest(String username, String email, String password)`
- `LoginRequest(String email, String password)`
- `UserResponse(UUID id, String username, String email, String role)`
- `AuthResponse(String token, UserResponse user)`

---

## 🧠 Service Layer

### `UserService` Interface

```java
UserResponse register(RegisterRequest request);
AuthResponse login(LoginRequest request);
UserResponse getCurrentUser(String token);
boolean existsByEmail(String email);
Optional<User> findByEmail(String email);
```

### `UserServiceImpl` implements `UserService`

Contains the business logic for each method.

---

## 🔐 Security Layer

### Components

- `JwtTokenProvider` – Generate and validate JWT tokens
- `JwtAuthenticationFilter` – Extract and verify token from requests
- `CustomUserDetailsService` – Load user from DB
- `SecurityConfig` – Configure HTTP security, password encoder, auth manager

---

## 🎯 Controller Layer

### `AuthController`

| Endpoint            | Method | Description                   |
|---------------------|--------|-------------------------------|
| `/api/auth/register`| POST   | Register a new user           |
| `/api/auth/login`   | POST   | Authenticate user and return JWT |
| `/api/auth/me`      | GET    | Return authenticated user info |

---

## 🗃 Repository

### `UserRepository extends JpaRepository<User, UUID>`

```java
Optional<User> findByEmail(String email);
boolean existsByEmail(String email);
```

---

## ⚠️ Exception Handling

### `exception` package

- `GlobalExceptionHandler` using `@ControllerAdvice`
- Custom exceptions:
  - `EmailAlreadyExistsException`
  - `InvalidCredentialsException`
  - `UserNotFoundException`

---

## 🔐 Security Details

- Use `BCryptPasswordEncoder` to hash passwords
- Use JWT for stateless authentication
- Each request should pass a `Bearer <token>` in headers
- Secure endpoints with `@PreAuthorize("hasRole('ADMIN')")` if needed
- Add roles to JWT claims and use them in Spring Security context

---

## ✅ Validation

- Use `@Valid` in controller methods
- Apply validation annotations to DTO fields:
  - `@NotBlank`
  - `@Email`
  - `@Size`

---

## 🚀 Optional Features (Good for Portfolio)

- ✅ Swagger/OpenAPI with `springdoc-openapi`
- ✅ Dockerfile for containerizing the microservice
- ✅ Testcontainers for integration tests
- ✅ GitHub Actions workflow for CI/CD
