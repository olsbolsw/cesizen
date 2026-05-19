package fr.cesi.cesizen.controller;

import fr.cesi.cesizen.dto.article.ArticleRequest;
import fr.cesi.cesizen.dto.article.ArticleResponse;
import fr.cesi.cesizen.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/published")
    public List<ArticleResponse> listPublished() {
        return articleService.findPublished();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ArticleResponse> listAll() {
        return articleService.findAll();
    }

    @GetMapping("/{id}")
    public ArticleResponse getById(@PathVariable Long id) {
        return articleService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleResponse create(@Valid @RequestBody ArticleRequest request) {
        return articleService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ArticleResponse update(@PathVariable Long id, @Valid @RequestBody ArticleRequest request) {
        return articleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        articleService.delete(id);
    }
}
