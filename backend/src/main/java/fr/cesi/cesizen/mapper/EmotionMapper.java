package fr.cesi.cesizen.mapper;

import fr.cesi.cesizen.domain.emotion.EmotionEntry;
import fr.cesi.cesizen.dto.emotion.EmotionEntryResponse;
import org.springframework.stereotype.Component;

@Component
public class EmotionMapper {

    public EmotionEntryResponse toResponse(EmotionEntry entry) {
        return new EmotionEntryResponse(
                entry.getId(),
                entry.getEmotionType(),
                entry.getIntensity(),
                entry.getNote(),
                entry.getEntryDate(),
                entry.getCreatedAt()
        );
    }
}
