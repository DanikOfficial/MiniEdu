package com.miniedu.securitylib.service;

import com.miniedu.securitylib.config.JwtProperties;
import com.miniedu.securitylib.exception.JwtValidationException;
import com.miniedu.securitylib.model.JwtTokenData;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceImplTest {

    private JwtServiceImpl jwtService;

    @BeforeEach
    void setup() {

        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret("ThisIsASecretKeyForJWTs1234567890"); // At Least 256 bits
        jwtProperties.setExpiration(3600000L); // 1 hour
        jwtProperties.setIssuer("miniedu");

        jwtService = new JwtServiceImpl(jwtProperties);
        jwtService.init(); // Manually calling postConstruct
    }

    @Test
    void testGenerateAndValidateToken() throws JwtValidationException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "admin");
        claims.put("userId", 123);

        JwtTokenData data = new JwtTokenData("testuser@example.com", claims);

        String token = jwtService.generateToken(data);
        assertNotNull(token);

        Claims extracted = jwtService.validateToken(token);

        assertEquals("testuser@example.com", extracted.getSubject());
        assertEquals("admin", extracted.get("role"));
        assertEquals(123, extracted.get("userId", Integer.class));
        assertEquals("miniedu", extracted.getIssuer());
    }

    @Test
    void testIsTokenExpiredReturnsFalseForValidToken() {
        JwtTokenData tokenData = new JwtTokenData("test@example.com", Map.of());
        String token = jwtService.generateToken(tokenData);
        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    void testExtractFromToken() throws JwtValidationException {
        Map<String, Object> claims = Map.of("username", "john_doe");
        JwtTokenData tokenData = new JwtTokenData("test@example.com", claims);

        String token = jwtService.generateToken(tokenData);
        String username = jwtService.extractFromToken(token, "username", String.class);

        assertEquals("john_doe", username);
    }

    @Test
    void testExtractFromSubject() throws JwtValidationException {
        Map<String, Object> claims = Map.of("username", "jane_doe");
        JwtTokenData tokenData = new JwtTokenData("user@example.com", claims);

        String token = jwtService.generateToken(tokenData);
        String subject = jwtService.extractSubjectFromToken(token);

        assertEquals("user@example.com", subject);
    }

    @Test
    void testValidateTokenThrowsOnInvalidToken() {
        String invalidToken = "someInvalidToken";

        assertThrows(JwtValidationException.class, () -> jwtService.validateToken(invalidToken));
    }

    @Test
    void testIsTokenExpiredReturnsTrueForExpiredToken() throws InterruptedException {
        JwtProperties props = new JwtProperties();
        props.setSecret("my-very-secure-secret-key-that-is-long-enough");
        props.setExpiration(1L); // expire immediately
        props.setIssuer("miniedu");

        JwtServiceImpl shortLivedService = new JwtServiceImpl(props);
        shortLivedService.init();

        String token = shortLivedService.generateToken(new JwtTokenData("user@example.com", Map.of()));
        Thread.sleep(5);

        assertTrue(shortLivedService.isTokenExpired(token));
    }
}
