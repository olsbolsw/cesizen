package fr.cesi.cesizen.dto.auth;

import fr.cesi.cesizen.domain.user.Role;

public record AuthResponse(
        String accessToken,
        String tokenType,
        Long userId,
        String email,
        String firstName,
        String lastName,
        Role role
) {
}
