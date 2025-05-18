package com.miniedu.securitylib.service;

import com.miniedu.securitylib.config.JwtProperties;
import com.miniedu.securitylib.exception.JwtValidationException;
import com.miniedu.securitylib.model.JwtTokenData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

    private final JwtProperties jwtProperties;
    private Key secretKey;

    public JwtServiceImpl(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    public void init() {
        logger.debug("Initializing JWT Secret Key");
        secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    @Override
    public String generateToken(JwtTokenData data) {
        logger.info("Generating JWT token for subject: {}", data.subject());
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getExpiration());


        String token = Jwts.builder()
                .setSubject(data.subject())
                .addClaims(data.claims())
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();


        logger.debug("Token generated successfully with expiration at {}", expiry);

        return token;
    }

    @Override
    public Claims validateToken(String token) throws JwtValidationException {
        logger.debug("Validating JWT token");
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            logger.info("JWT token successfully validated for subject: {}", claims.getSubject());
            return claims;

        } catch (JwtException ex) {
            logger.warn("Token validation failed: {}", ex.getMessage());
            throw new JwtValidationException("Invalid or expired JWT token", ex);
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = validateToken(token);
            boolean expired = claims.getExpiration().before(new Date());
            logger.debug("Token expiration check for subject {}: expired={}", claims.getSubject(), expired);
            return expired;
        } catch (JwtValidationException e) {
            logger.warn("Token considered expired due to validation error: {}", e.getMessage());
            return true;
        }
    }

    @Override
    public <T> T extractFromToken(String token, String field, Class<T> type) throws JwtValidationException {
        Claims claims = validateToken(token);
        T value = claims.get(field, type);
        logger.debug("Extracted field '{}' of type {} from token for subject: {}", field, type.getSimpleName(), claims.getSubject());
        return value;
    }

    @Override
    public String extractSubjectFromToken(String token) {
        Claims claims = validateToken(token);
        logger.debug("Extracted subject from token: {}", claims.getSubject());
        return claims.getSubject();
    }

    @Override
    public String generateVerificationToken(JwtTokenData data) {
        logger.info("Generating verification token for subject: {}", data.subject());
        Date now = new Date();
        Date expiry = new Date(now.getTime() + Duration.ofDays(28).toMillis()); // 4 weeks

        String token = Jwts.builder()
                .setSubject(data.subject())
                .addClaims(data.claims())
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        logger.debug("Verification token generated successfully with 4-week expiration at {}", expiry);
        return token;
    }

}
