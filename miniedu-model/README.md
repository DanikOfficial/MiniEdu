# MiniEdu Model Library

This is a shared Maven library for the MiniEdu Microservices Project.

## Purpose

The `miniedu-model` library contains common data models and event classes used for communication between microservices, such as Kafka events.

Sharing these classes ensures:

- Consistent data contracts across services
- Reduced duplication of code
- Easier maintenance and evolution of shared types

## Included Event Classes

- `PasswordResetEvent` — event data for password reset requests
- `UserRegisteredEvent` — event data for user registration events
- `EmailEvent` — common interface for email-related events

## Usage

1. Build and install the library locally:

   ```bash
   mvn clean install

### 1. Add this dependency to your microservices’ pom.xml:

```xml
<dependency>
  <groupId>com.miniedu.model</groupId>
  <artifactId>miniedu-model</artifactId>
  <version>1.0.0</version>
</dependency>

```

### 2. Import and use the shared classes in your code:

```java
import com.miniedu.model.PasswordResetEvent;
import com.miniedu.model.UserRegisteredEvent;
```

## Requirements
- Java 17 or higher
- Maven 3.6+
