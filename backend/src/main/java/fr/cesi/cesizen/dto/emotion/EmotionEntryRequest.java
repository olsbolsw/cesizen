package fr.cesi.cesizen.dto.emotion;

import fr.cesi.cesizen.domain.emotion.EmotionType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EmotionEntryRequest(
        @NotNull EmotionType emotionType,
        @Min(1) @Max(10) int intensity,
        @Size(max = 500) String note,
        LocalDate entryDate
) {
}
