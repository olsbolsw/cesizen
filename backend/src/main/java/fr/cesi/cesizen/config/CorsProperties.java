package fr.cesi.cesizen.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "cesizen.cors")
public record CorsProperties(List<String> allowedOrigins) {
}
