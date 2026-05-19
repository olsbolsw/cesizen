package fr.cesi.cesizen.domain.article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByPublishedTrueOrderByCreatedAtDesc();

    List<Article> findAllByOrderByCreatedAtDesc();
}
