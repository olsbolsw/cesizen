package fr.cesi.cesizen.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ArticleRequest(
        @NotBlank @Size(max = 200) String title,
        @NotBlank String content,
        @Size(max = 100) String category,
        boolean published
) {
}
