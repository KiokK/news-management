package ru.clevertec.news.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.logginstarter.annotation.CustomMethodLog;
import ru.clevertec.news.controller.openapi.NewsControllerOpenApi;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.request.NewsRequestDto;
import ru.clevertec.news.dto.response.NewsPageResponseDto;
import ru.clevertec.news.dto.response.NewsResponseDto;
import ru.clevertec.news.dto.response.NewsWithCommentResponseDto;
import ru.clevertec.news.dto.response.NewsWithPageCommentsResponseDto;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.service.NewsService;

@RestController
@CustomMethodLog
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
public class NewsController implements NewsControllerOpenApi {

    private final NewsService newsService;
    private final CommentService commentService;

    @Override
    @PostMapping
    public ResponseEntity<NewsResponseDto> createNews(@Valid @RequestBody NewsRequestDto newsRequestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newsService.create(newsRequestDto));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<NewsResponseDto> getNewsById(@PathVariable Long id) {

        return ResponseEntity.ok(newsService.findById(id));
    }

    @Override
    @GetMapping("/{id}/comments")
    public ResponseEntity<NewsWithPageCommentsResponseDto> getNewsByIdWithComments(@PathVariable Long id,
                                                                                   Pageable pageable) {

        return ResponseEntity.ok(commentService.findAllByNewsId(id, pageable));
    }

    @Override
    @GetMapping("/{newsId}/comments/{commentId}")
    public ResponseEntity<NewsWithCommentResponseDto> getNewsByIdWithComment(@PathVariable Long newsId,
                                                                             @PathVariable Long commentId) {
        NewsWithCommentResponseDto foundComment = commentService.findByNewsIdAndCommentId(newsId, commentId);

        return ResponseEntity.ok(foundComment);
    }

    @Override
    @GetMapping
    public ResponseEntity<NewsPageResponseDto> getAllNews(Pageable pageable) {

        return ResponseEntity.ok(newsService.findAll(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<NewsPageResponseDto> getAllNewsByFilter(@Valid Filter filter, Pageable pageable) {

        return ResponseEntity.ok(newsService.findAllByFilter(filter, pageable));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<NewsResponseDto> updateNews(@PathVariable Long id, @Valid @RequestBody NewsRequestDto dto) {

        return ResponseEntity.status(201)
                .body(newsService.update(id, dto));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteById(id);

        return ResponseEntity.status(204).build();
    }

}
