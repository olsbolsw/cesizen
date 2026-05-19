package fr.cesi.cesizen.domain.emotion;

import fr.cesi.cesizen.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmotionEntryRepository extends JpaRepository<EmotionEntry, Long> {

    List<EmotionEntry> findByUserOrderByEntryDateDescCreatedAtDesc(User user);

    List<EmotionEntry> findByUserAndEntryDateBetweenOrderByEntryDateAsc(
            User user, LocalDate start, LocalDate end);

    Optional<EmotionEntry> findByIdAndUser(Long id, User user);
}
