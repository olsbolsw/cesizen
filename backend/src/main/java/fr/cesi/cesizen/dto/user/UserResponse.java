package fr.cesi.cesizen.dto.user;

import fr.cesi.cesizen.domain.user.Role;

import java.time.Instant;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        Role role,
        boolean active,
        boolean rgpdConsent,
        Instant createdAt
) {
}
