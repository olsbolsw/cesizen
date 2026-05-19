package fr.cesi.cesizen.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cesizen.jwt")
public record JwtProperties(String secret, long expirationMs) {
}
