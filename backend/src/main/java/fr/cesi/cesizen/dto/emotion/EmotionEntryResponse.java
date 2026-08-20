package fr.cesi.cesizen.dto.emotion;

import fr.cesi.cesizen.domain.emotion.EmotionType;

import java.time.Instant;
import java.time.LocalDate;

public record EmotionEntryResponse(
        Long id,
        EmotionType emotionType,
        int intensity,
        String note,
        LocalDate entryDate,
        Instant createdAt
) {
}
