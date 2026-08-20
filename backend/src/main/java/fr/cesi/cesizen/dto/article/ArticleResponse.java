package fr.cesi.cesizen.dto.article;

import java.time.Instant;

public record ArticleResponse(
        Long id,
        String title,
        String content,
        String category,
        boolean published,
        String authorName,
        Instant createdAt,
        Instant updatedAt
) {
}
