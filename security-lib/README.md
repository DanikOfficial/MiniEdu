# security-lib

`security-lib` is a lightweight Spring Boot library for handling JSON Web Tokens (JWT) within the MiniEdu microservices ecosystem. It provides JWT token generation, validation, and easy integration via Spring Boot auto-configuration.

---

## Features

- Generate JWT tokens with custom claims
- Validate and parse JWT tokens securely
- Custom exception handling for JWT validation errors
- Spring Boot auto-configuration for seamless setup
- Configurable properties for secret keys and token expiration
- Unit tested core implementation

---

## Getting Started

### Configuration

Add the following properties to your microservice configuration (`application.yml` or `application.properties`):

```yaml
jwt:
  secret: your-secure-secret-key
  expiration-ms: 3600000  # Token expiration in milliseconds (e.g., 1 hour)
```


### Usage
Inject the `JwtService` to generate and validate tokens:

```java
@Service
public class AuthService {

    private final JwtService jwtService;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public JwtTokenData validateToken(String token) throws JwtValidationException {
        return jwtService.validateToken(token);
    }
}
```


### Exception Handling

`JwtValidationException` is thrown when token validation fails due to expiration, malformation, or other errors. Make sure to catch and handle this exception appropriately.
