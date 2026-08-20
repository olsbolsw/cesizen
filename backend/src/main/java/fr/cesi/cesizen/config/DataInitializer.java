package fr.cesi.cesizen.config;

import fr.cesi.cesizen.domain.article.Article;
import fr.cesi.cesizen.domain.article.ArticleRepository;
import fr.cesi.cesizen.domain.user.Role;
import fr.cesi.cesizen.domain.user.User;
import fr.cesi.cesizen.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

/**
 * Jeu de données de démonstration (profil dev uniquement).
 */
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Profile("!test")
    CommandLineRunner seedData() {
        return args -> {
            User admin = userRepository.findByEmailIgnoreCase("admin@cesizen.fr")
                    .orElseGet(() -> userRepository.save(User.builder()
                            .email("admin@cesizen.fr")
                            .passwordHash(passwordEncoder.encode("Admin123!"))
                            .firstName("Admin")
                            .lastName("CESIZen")
                            .role(Role.ADMIN)
                            .active(true)
                            .rgpdConsent(true)
                            .rgpdConsentAt(Instant.now())
                            .build()));

            if (articleRepository.count() == 0) {
                articleRepository.save(Article.builder()
                        .title("Bienvenue sur CESIZen")
                        .content("CESIZen vous accompagne dans la gestion du stress et le suivi de vos émotions.")
                        .category("Bien-être")
                        .published(true)
                        .author(admin)
                        .build());
            }
        };
    }
}
