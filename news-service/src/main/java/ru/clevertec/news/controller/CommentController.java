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
import ru.clevertec.news.controller.openapi.CommentControllerOpenApi;
import ru.clevertec.news.dto.request.CommentRequestDto;
import ru.clevertec.news.dto.request.CommentUpdateRequestDto;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.response.CommentAuthorResponseDto;
import ru.clevertec.news.dto.response.CommentResponseDto;
import ru.clevertec.news.dto.response.CommentsPageResponseDto;
import ru.clevertec.news.service.CommentService;

@RestController
@CustomMethodLog
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController implements CommentControllerOpenApi {

    private final CommentService commentService;

    @Override
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@Valid @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto createdComment = commentService.create(commentRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdComment);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable Long id) {
        CommentResponseDto foundComment = commentService.findById(id);

        return ResponseEntity.ok(foundComment);
    }

    @Override
    @GetMapping("/{id}/author")
    public ResponseEntity<CommentAuthorResponseDto> getCommentAuthorByCommentId(@PathVariable Long id) {
        CommentAuthorResponseDto foundComment = commentService.findCommentWithAuthorById(id);

        return ResponseEntity.ok(foundComment);
    }

    @Override
    @GetMapping
    public ResponseEntity<CommentsPageResponseDto> getAllComments(Pageable pageable) {

        return ResponseEntity.ok(commentService.findAll(pageable));
    }

    @Override
    @GetMapping("/filter")
    public ResponseEntity<CommentsPageResponseDto> getAllCommentsByFilter(@Valid Filter filter,
                                                                          Pageable pageable) {

        return ResponseEntity.ok(commentService.findAllByFilter(filter, pageable));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id,
                                                            @Valid @RequestBody CommentUpdateRequestDto dto) {

        return ResponseEntity.status(201)
                .body(commentService.update(id, dto));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteById(id);

        return ResponseEntity.status(204).build();
    }

}
