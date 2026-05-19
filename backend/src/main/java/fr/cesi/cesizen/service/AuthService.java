package fr.cesi.cesizen.service;

import fr.cesi.cesizen.domain.user.Role;
import fr.cesi.cesizen.domain.user.User;
import fr.cesi.cesizen.domain.user.UserRepository;
import fr.cesi.cesizen.dto.auth.AuthResponse;
import fr.cesi.cesizen.dto.auth.LoginRequest;
import fr.cesi.cesizen.dto.auth.RegisterRequest;
import fr.cesi.cesizen.exception.BusinessException;
import fr.cesi.cesizen.exception.ErrorCode;
import fr.cesi.cesizen.security.JwtService;
import fr.cesi.cesizen.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BusinessException(ErrorCode.CONFLICT, HttpStatus.CONFLICT,
                    "Cet email est déjà utilisé");
        }

        User user = User.builder()
                .email(request.email().trim().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.password()))
                .firstName(request.firstName().trim())
                .lastName(request.lastName().trim())
                .role(Role.USER)
                .active(true)
                .rgpdConsent(true)
                .rgpdConsentAt(Instant.now())
                .build();

        userRepository.save(user);
        return buildAuthResponse(new UserPrincipal(user));
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email().trim().toLowerCase(),
                        request.password()
                )
        );

        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED,
                        "Identifiants invalides"));

        return buildAuthResponse(new UserPrincipal(user));
    }

    private AuthResponse buildAuthResponse(UserPrincipal principal) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND,
                        "Utilisateur introuvable"));
        String token = jwtService.generateToken(principal);
        return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }
}
