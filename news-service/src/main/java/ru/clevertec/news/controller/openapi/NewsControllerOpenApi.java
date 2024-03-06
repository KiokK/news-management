package ru.clevertec.news.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.request.NewsRequestDto;
import ru.clevertec.news.dto.response.ErrorResponseDto;
import ru.clevertec.news.dto.response.NewsPageResponseDto;
import ru.clevertec.news.dto.response.NewsResponseDto;
import ru.clevertec.news.dto.response.NewsWithCommentResponseDto;
import ru.clevertec.news.dto.response.NewsWithPageCommentsResponseDto;

@Tag(name = "News")
public interface NewsControllerOpenApi {

    @Operation(
            method = "POST",
            description = "Create new news",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NewsRequestDto.class),
                            examples = @ExampleObject("""
                                            {
                                                 "title": "The best title",
                                                 "text": "Bla-bla-bla text."
                                            }
                                            """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NewsResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                                 "id": 1,
                                                 "title": "As a programmer, I would make a UDP joke",
                                                 "text": "But you might not get it.",
                                                 "createdAt": "2024-02-14'T'01:25:18",
                                                 "modifiedAt": "2024-02-14'T'01:25:18"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                                 "errorCode": 400400,
                                                 "errorMessage": "title = must not be null"
                                            }
                                            """)
                            )
                    )
            })
    ResponseEntity<NewsResponseDto> createNews(@Valid @RequestBody NewsRequestDto newsRequestDto);

    @Operation(
            method = "GET",
            description = "Get news by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NewsResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                                 "id": 1,
                                                 "title": "As a programmer, I would make a UDP joke",
                                                 "text": "But you might not get it.",
                                                 "createdAt": "2024-02-14'T'01:25:18",
                                                 "modifiedAt": "2024-02-14'T'01:25:18"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                                 "errorCode": 400400,
                                                 "errorMessage": "title = must not be null"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                                 "errorCode": 404000,
                                                 "errorMessage": "Entity with id='1' not found"
                                            }
                                            """)
                            )
                    )
            })
    ResponseEntity<NewsResponseDto> getNewsById(@PathVariable Long id);

    @Operation(
            method = "GET",
            description = "Get news with comments by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NewsWithPageCommentsResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                              "id": 1,
                                              "text": "I sold my laptop.",
                                              "title": "Today I made my first money as a Programmer",
                                              "comments": [
                                                {
                                                  "id": 1,
                                                  "text": "I sold my laptop... and bought ipad!",
                                                  "username": "alya228",
                                                  "newsId": 1,
                                                  "createdAt": "2024-03-02T19:51:41",
                                                  "modifiedAt": "2024-03-02T19:51:41"
                                                }
                                              ],
                                              "pageSize": 1,
                                              "pageNumber": 0,
                                              "createdAt": "2024-03-02T19:51:41",
                                              "modifiedAt": "2024-03-02T19:51:41"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                                 "errorCode": 400400,
                                                 "errorMessage": "pageSize = the size of the page to be returned, must be greater than 0"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                                 "errorCode": 404000,
                                                 "errorMessage": "Entity with id='1' not found"
                                            }
                                            """)
                            )
                    )
            })
    ResponseEntity<NewsWithPageCommentsResponseDto> getNewsByIdWithComments(@PathVariable Long id, Pageable pageable);

    @Operation(
            method = "GET",
            description = "Get news by id with comment by commentId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NewsWithCommentResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                              "comment": {
                                                "id": 1,
                                                "text": "I sold my laptop... and bought ipad!",
                                                "username": "alya228",
                                                "newsId": 1,
                                                "createdAt": "2024-03-02T19:51:41",
                                                "modifiedAt": "2024-03-02T19:51:41"
                                              },
                                              "news": {
                                                "id": 1,
                                                "text": "I sold my laptop.",
                                                "title": "Today I made my first money as a Programmer",
                                                "createdAt": "2024-03-02T19:51:41",
                                                "modifiedAt": "2024-03-02T19:51:41"
                                              }
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                                 "errorCode": 404000,
                                                 "errorMessage": "Entity with id='1' not found"
                                            }
                                            """)
                            )
                    )
            })
    ResponseEntity<NewsWithCommentResponseDto> getNewsByIdWithComment(@PathVariable Long newsId,
                                                                      @PathVariable Long commentId);

    @Operation(
            method = "GET",
            description = "Get all news by pageable info",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NewsPageResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                              "news": [
                                                {
                                                  "id": 3,
                                                  "text": "But you might not get it.",
                                                  "title": "As a programmer, I would make a UDP joke",
                                                  "createdAt": "2024-03-02T19:51:41",
                                                  "modifiedAt": "2024-03-02T19:51:41"
                                                },
                                                {
                                                  "id": 4,
                                                  "text": "A programmer walks into a bar, holds up two fingers, and says ''I’ll have three beers please.''",
                                                  "title": "A programmer walks into a bar,",
                                                  "createdAt": "2024-03-02T19:51:41",
                                                  "modifiedAt": "2024-03-02T19:51:41"
                                                }
                                              ],
                                              "pageSize": 2,
                                              "pageNumber": 1
                                            }
                                            """)
                            )
                    )
            })
    ResponseEntity<NewsPageResponseDto> getAllNews(Pageable pageable);

    @Operation(
            method = "GET",
            description = "Get all news by filter and pageable info",
            parameters = {
                    @Parameter(name = "pageSize", description = "Page size", example = "2"),
                    @Parameter(name = "pageNumber", description = "News page", example = "0"),
                    @Parameter(name = "sort", description = "Sort by field", example = "title,asc"),
                    @Parameter(name = "fields", description = "Fields for searching", example = "text,title"),
                    @Parameter(name = "part", description = "Part of title or text for searching", example = "program")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NewsPageResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                              "news": [
                                                {
                                                  "id": 3,
                                                  "text": "But you might not get it.",
                                                  "title": "As a programmer, I would make a UDP joke",
                                                  "createdAt": "2024-03-02T19:51:41",
                                                  "modifiedAt": "2024-03-02T19:51:41"
                                                },
                                                {
                                                  "id": 4,
                                                  "text": "A programmer walks into a bar, holds up two fingers, and says ''I’ll have three beers please.''",
                                                  "title": "A programmer walks into a bar,",
                                                  "createdAt": "2024-03-02T19:51:41",
                                                  "modifiedAt": "2024-03-02T19:51:41"
                                                }
                                              ],
                                              "pageSize": 2,
                                              "pageNumber": 1
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                                 "errorCode": 400400,
                                                 "errorMessage": "pageNumber = zero-based page number, must not be negative"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                                 "errorCode": 404000,
                                                 "errorMessage": "Entity with part='1' not found"
                                            }
                                            """)
                            )
                    )
            })
    ResponseEntity<NewsPageResponseDto> getAllNewsByFilter(@Valid Filter filter, Pageable pageable);

    @Operation(
            method = "PUT",
            description = "Update news",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NewsRequestDto.class),
                            examples = @ExampleObject("""
                                            {
                                                 "title": "The best NEW title",
                                                 "text": "New bla-bla-bla text."
                                            }
                                            """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NewsResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                                 "id": 1,
                                                 "title": "As a programmer, I would make a UDP joke",
                                                 "text": "But you might not get it.",
                                                 "createdAt": "2024-02-14'T'01:25:18",
                                                 "modifiedAt": "2024-02-14'T'01:25:18"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                                 "errorCode": 400400,
                                                 "errorMessage": "title = must not be null"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                                 "errorCode": 404000,
                                                 "errorMessage": "Entity with id='1' not found"
                                            }
                                            """)
                            )
                    )
            })
    ResponseEntity<NewsResponseDto> updateNews(@PathVariable Long id, @Valid @RequestBody NewsRequestDto dto);

    @Operation(
            method = "DELETE",
            description = "Delete news by id",
            responses = {
                    @ApiResponse(
                            responseCode = "204"
                    )
            })
    ResponseEntity<Void> deleteNews(@PathVariable Long id);

}
