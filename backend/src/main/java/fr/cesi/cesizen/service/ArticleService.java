package fr.cesi.cesizen.service;

import fr.cesi.cesizen.domain.article.Article;
import fr.cesi.cesizen.domain.article.ArticleRepository;
import fr.cesi.cesizen.domain.user.User;
import fr.cesi.cesizen.domain.user.UserRepository;
import fr.cesi.cesizen.dto.article.ArticleRequest;
import fr.cesi.cesizen.dto.article.ArticleResponse;
import fr.cesi.cesizen.exception.BusinessException;
import fr.cesi.cesizen.exception.ErrorCode;
import fr.cesi.cesizen.exception.ResourceNotFoundException;
import fr.cesi.cesizen.mapper.ArticleMapper;
import fr.cesi.cesizen.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;

    @Transactional(readOnly = true)
    public List<ArticleResponse> findPublished() {
        return articleRepository.findByPublishedTrueOrderByCreatedAtDesc().stream()
                .map(articleMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> findAll() {
        return articleRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(articleMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ArticleResponse findById(Long id) {
        Article article = getArticleOrThrow(id);
        if (!article.isPublished() && !isCurrentUserAdmin()) {
            throw new BusinessException(ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN,
                    "Article non accessible");
        }
        return articleMapper.toResponse(article);
    }

    @Transactional
    public ArticleResponse create(ArticleRequest request) {
        User author = getCurrentUserEntity();
        Article article = Article.builder()
                .title(request.title().trim())
                .content(request.content().trim())
                .category(request.category())
                .published(request.published())
                .author(author)
                .build();
        return articleMapper.toResponse(articleRepository.save(article));
    }

    @Transactional
    public ArticleResponse update(Long id, ArticleRequest request) {
        Article article = getArticleOrThrow(id);
        article.setTitle(request.title().trim());
        article.setContent(request.content().trim());
        article.setCategory(request.category());
        article.setPublished(request.published());
        return articleMapper.toResponse(articleRepository.save(article));
    }

    @Transactional
    public void delete(Long id) {
        Article article = getArticleOrThrow(id);
        articleRepository.delete(article);
    }

    private Article getArticleOrThrow(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article introuvable"));
    }

    private User getCurrentUserEntity() {
        Long userId = SecurityUtils.getCurrentUser().getId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
    }

    private boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }
}
