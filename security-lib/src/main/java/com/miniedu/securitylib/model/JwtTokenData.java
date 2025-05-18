package com.miniedu.securitylib.model;

import java.util.Map;

public record JwtTokenData(String subject, Map<String, Object> claims) {
}
