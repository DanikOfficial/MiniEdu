package com.miniedu.securitylib.service;

import com.miniedu.securitylib.exception.JwtValidationException;
import com.miniedu.securitylib.model.JwtTokenData;
import io.jsonwebtoken.Claims;

public interface JwtService {
    String generateToken(JwtTokenData data);

    Claims validateToken(String token) throws JwtValidationException;

    boolean isTokenExpired(String token);

    <T> T extractFromToken(String token, String field, Class<T> type) throws JwtValidationException;

    String extractSubjectFromToken(String token) throws JwtValidationException;

    String generateVerificationToken(JwtTokenData data);
}