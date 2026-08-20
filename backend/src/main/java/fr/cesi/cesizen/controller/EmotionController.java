package fr.cesi.cesizen.controller;

import fr.cesi.cesizen.dto.emotion.EmotionEntryRequest;
import fr.cesi.cesizen.dto.emotion.EmotionEntryResponse;
import fr.cesi.cesizen.dto.emotion.EmotionStatsResponse;
import fr.cesi.cesizen.dto.emotion.EmotionTypeInfo;
import fr.cesi.cesizen.service.EmotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/emotions")
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionService emotionService;

    @GetMapping
    public List<EmotionEntryResponse> listMine() {
        return emotionService.findMine();
    }

    @GetMapping("/types")
    public List<EmotionTypeInfo> listTypes() {
        return emotionService.listAvailableTypes();
    }

    @GetMapping("/stats")
    public EmotionStatsResponse stats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return emotionService.getStats(start, end);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmotionEntryResponse create(@Valid @RequestBody EmotionEntryRequest request) {
        return emotionService.create(request);
    }

    @PutMapping("/{id}")
    public EmotionEntryResponse update(@PathVariable Long id,
                                       @Valid @RequestBody EmotionEntryRequest request) {
        return emotionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        emotionService.delete(id);
    }
}
