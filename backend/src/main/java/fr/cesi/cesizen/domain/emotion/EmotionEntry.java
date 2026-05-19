package fr.cesi.cesizen.domain.emotion;

import fr.cesi.cesizen.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "emotion_entries", indexes = {
        @Index(name = "idx_emotion_user_date", columnList = "user_id, entry_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmotionEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EmotionType emotionType;

    @Column(nullable = false)
    private int intensity;

    @Column(length = 500)
    private String note;

    @Column(nullable = false)
    private LocalDate entryDate;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        if (entryDate == null) {
            entryDate = LocalDate.now();
        }
    }
}
