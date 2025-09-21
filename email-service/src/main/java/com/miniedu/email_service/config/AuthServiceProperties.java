package com.miniedu.email_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "miniedu.auth-service")
@Getter
@Setter
public class AuthServiceProperties {
    private String baseUrl;
    private Paths paths = new Paths();

    @Getter
    @Setter
    public static class Paths {
        private String verification;
        private String passwordReset;
    }
}
