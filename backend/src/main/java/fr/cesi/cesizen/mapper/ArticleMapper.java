package fr.cesi.cesizen.mapper;

import fr.cesi.cesizen.domain.article.Article;
import fr.cesi.cesizen.dto.article.ArticleResponse;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    public ArticleResponse toResponse(Article article) {
        String authorName = article.getAuthor().getFirstName() + " " + article.getAuthor().getLastName();
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getCategory(),
                article.isPublished(),
                authorName,
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }
}
