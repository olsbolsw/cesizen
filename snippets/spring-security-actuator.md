# Spring Security: allow the health endpoint

Your Docker healthcheck calls `/actuator/health`. If Spring Security currently protects every endpoint, allow this health endpoint explicitly.

Typical Spring Security 6 style (adapt to your existing `SecurityFilterChain`):

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/actuator/health").permitAll()
    // keep your existing public endpoints here
    .anyRequest().authenticated()
)
```

Do not replace your full security configuration with this snippet. Merge only the matcher.
