package fr.cesi.cesizen.dto.emotion;

import fr.cesi.cesizen.domain.emotion.EmotionType;

import java.util.Map;

public record EmotionStatsResponse(
        long totalEntries,
        double averageIntensity,
        Map<EmotionType, Long> countByEmotion
) {
}
