package fr.cesi.cesizen.dto.auth;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank @Email @Size(max = 180) String email,
        @NotBlank @Size(min = 8, max = 100) String password,
        @NotBlank @Size(max = 80) String firstName,
        @NotBlank @Size(max = 80) String lastName,
        @AssertTrue(message = "Le consentement RGPD est obligatoire") boolean rgpdConsent
) {
}
