package fr.cesi.cesizen.service;

import fr.cesi.cesizen.domain.emotion.EmotionEntry;
import fr.cesi.cesizen.domain.emotion.EmotionEntryRepository;
import fr.cesi.cesizen.domain.emotion.EmotionType;
import fr.cesi.cesizen.domain.user.User;
import fr.cesi.cesizen.domain.user.UserRepository;
import fr.cesi.cesizen.dto.emotion.EmotionEntryRequest;
import fr.cesi.cesizen.dto.emotion.EmotionEntryResponse;
import fr.cesi.cesizen.dto.emotion.EmotionStatsResponse;
import fr.cesi.cesizen.dto.emotion.EmotionTypeInfo;
import fr.cesi.cesizen.exception.ResourceNotFoundException;
import fr.cesi.cesizen.mapper.EmotionMapper;
import fr.cesi.cesizen.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmotionService {

    private final EmotionEntryRepository emotionEntryRepository;
    private final UserRepository userRepository;
    private final EmotionMapper emotionMapper;

    @Transactional(readOnly = true)
    public List<EmotionEntryResponse> findMine() {
        User user = getCurrentUserEntity();
        return emotionEntryRepository.findByUserOrderByEntryDateDescCreatedAtDesc(user).stream()
                .map(emotionMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmotionStatsResponse getStats(LocalDate start, LocalDate end) {
        User user = getCurrentUserEntity();
        LocalDate from = start != null ? start : LocalDate.now().minusDays(30);
        LocalDate to = end != null ? end : LocalDate.now();

        List<EmotionEntry> entries = emotionEntryRepository
                .findByUserAndEntryDateBetweenOrderByEntryDateAsc(user, from, to);

        Map<EmotionType, Long> countByEmotion = new EnumMap<>(EmotionType.class);
        for (EmotionType type : EmotionType.values()) {
            countByEmotion.put(type, 0L);
        }

        double intensitySum = 0;
        for (EmotionEntry entry : entries) {
            countByEmotion.merge(entry.getEmotionType(), 1L, Long::sum);
            intensitySum += entry.getIntensity();
        }

        double average = entries.isEmpty() ? 0 : intensitySum / entries.size();
        return new EmotionStatsResponse(entries.size(), average, countByEmotion);
    }

    @Transactional(readOnly = true)
    public List<EmotionTypeInfo> listAvailableTypes() {
        return Arrays.stream(EmotionType.values())
                .map(this::toTypeInfo)
                .toList();
    }

    @Transactional
    public EmotionEntryResponse create(EmotionEntryRequest request) {
        User user = getCurrentUserEntity();
        EmotionEntry entry = EmotionEntry.builder()
                .user(user)
                .emotionType(request.emotionType())
                .intensity(request.intensity())
                .note(request.note())
                .entryDate(request.entryDate() != null ? request.entryDate() : LocalDate.now())
                .build();
        return emotionMapper.toResponse(emotionEntryRepository.save(entry));
    }

    @Transactional
    public EmotionEntryResponse update(Long id, EmotionEntryRequest request) {
        User user = getCurrentUserEntity();
        EmotionEntry entry = emotionEntryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Entrée émotionnelle introuvable"));
        entry.setEmotionType(request.emotionType());
        entry.setIntensity(request.intensity());
        entry.setNote(request.note());
        if (request.entryDate() != null) {
            entry.setEntryDate(request.entryDate());
        }
        return emotionMapper.toResponse(emotionEntryRepository.save(entry));
    }

    @Transactional
    public void delete(Long id) {
        User user = getCurrentUserEntity();
        EmotionEntry entry = emotionEntryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Entrée émotionnelle introuvable"));
        emotionEntryRepository.delete(entry);
    }

    private EmotionTypeInfo toTypeInfo(EmotionType type) {
        return switch (type) {
            case JOYEUX -> new EmotionTypeInfo(type.name(), "Joyeux", "Humeur positive et légère");
            case CALME -> new EmotionTypeInfo(type.name(), "Calme", "Sérénité et équilibre intérieur");
            case STRESSE -> new EmotionTypeInfo(type.name(), "Stressé", "Tension ou pression ressentie");
            case TRISTE -> new EmotionTypeInfo(type.name(), "Triste", "Baisse d'humeur ou mélancolie");
            case ENERVE -> new EmotionTypeInfo(type.name(), "Énervé", "Irritabilité ou agacement");
            case ANXIEUX -> new EmotionTypeInfo(type.name(), "Anxieux", "Inquiétude ou appréhension");
            case FATIGUE -> new EmotionTypeInfo(type.name(), "Fatigué", "Épuisement physique ou mental");
            case MOTIVE -> new EmotionTypeInfo(type.name(), "Motivé", "Énergie et envie d'agir");
        };
    }

    private User getCurrentUserEntity() {
        Long userId = SecurityUtils.getCurrentUser().getId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
    }
}
