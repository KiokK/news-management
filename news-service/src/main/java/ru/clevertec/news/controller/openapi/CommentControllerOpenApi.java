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
import ru.clevertec.news.dto.request.CommentRequestDto;
import ru.clevertec.news.dto.request.CommentUpdateRequestDto;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.response.CommentAuthorResponseDto;
import ru.clevertec.news.dto.response.CommentResponseDto;
import ru.clevertec.news.dto.response.CommentsPageResponseDto;
import ru.clevertec.news.dto.response.ErrorResponseDto;

@Tag(name = "Comment")
public interface CommentControllerOpenApi {

    @Operation(
            method = "POST",
            description = "Create new comment",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentRequestDto.class),
                            examples = @ExampleObject("""
                                    {
                                         "text": "Good joke",
                                         "username": "kiok",
                                         "newsId": 1
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                              "id": 1,
                                              "text": "I sold my laptop... and bought ipad!",
                                              "username": "alya228",
                                              "newsId": 1,
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
                                                 "errorMessage": "text = must not be null"
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
                                                 "errorMessage": "Entity with username='user228' not found"
                                            }
                                            """)
                            )
                    )
            })
    ResponseEntity<CommentResponseDto> createComment(@Valid @RequestBody CommentRequestDto commentRequestDto);

    @Operation(
            method = "GET",
            description = "Get comment by id",
            parameters = {
                    @Parameter(name = "id", description = "Comment id", example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                              "id": 1,
                                              "text": "I sold my laptop... and bought ipad!",
                                              "username": "alya228",
                                              "newsId": 1,
                                              "createdAt": "2024-03-07T02:52:07",
                                              "modifiedAt": "2024-03-07T02:52:07"
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
                                              "errorMessage": "Entity with id='1324' not found",
                                              "errorCode": 404400
                                            }
                                            """)
                            )
                    )
            })
    ResponseEntity<CommentResponseDto> getCommentById(@PathVariable Long id);

    @Operation(
            method = "GET",
            description = "Get comment with author by commentId",
            parameters = {
                    @Parameter(name = "id", description = "Comment id", example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentAuthorResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                              "commentId": 1,
                                              "userId": 2,
                                              "text": "I sold my laptop... and bought ipad!",
                                              "username": "alya228",
                                              "email": "alya228@gmail.com",
                                              "newsId": 1,
                                              "createdAt": "2024-03-07T02:52:07",
                                              "modifiedAt": "2024-03-07T02:52:07"
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
                                              "errorMessage": "Entity with id='1324' not found",
                                              "errorCode": 404400
                                            }
                                            """)
                            )
                    )
            })
    ResponseEntity<CommentAuthorResponseDto> getCommentAuthorByCommentId(@PathVariable Long id);

    @Operation(
            method = "GET",
            description = "Get all comments by pageable info",
            parameters = {
                    @Parameter(name = "pageSize", description = "Page size", example = "2"),
                    @Parameter(name = "pageNumber", description = "Number page", example = "0"),
                    @Parameter(name = "sort", description = "Sort by field", example = "createdAt,asc")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentsPageResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                              "comments": [
                                                  {
                                                    "id": 1,
                                                    "text": "I sold my laptop... and bought ipad!",
                                                    "username": "alya228",
                                                    "newsId": 1,
                                                    "createdAt": "2024-03-07T02:52:07",
                                                    "modifiedAt": "2024-03-07T02:52:07"
                                                  },
                                                  {
                                                    "id": 2,
                                                    "text": "Ba-ha-ha",
                                                    "username": "alya228",
                                                    "newsId": 1,
                                                    "createdAt": "2024-03-07T02:52:07",
                                                    "modifiedAt": "2024-03-07T02:52:07"
                                                  }
                                                ],
                                                "pageSize": 2,
                                                "pageNumber": 0
                                            }
                                            """)
                            )
                    )
            })
    ResponseEntity<CommentsPageResponseDto> getAllComments(Pageable pageable);

    @Operation(
            method = "GET",
            description = "Get all comments by filter and pageable info",
            parameters = {
                    @Parameter(name = "pageSize", description = "Page size", example = "2"),
                    @Parameter(name = "pageNumber", description = "Comment page", example = "0"),
                    @Parameter(name = "sort", description = "Sort by field", example = "createdAt,asc"),
                    @Parameter(name = "fields", description = "Fields for searching", example = "text"),
                    @Parameter(name = "part", description = "Part of text for searching", example = "program")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentsPageResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
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
                                               "pageNumber": 0
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
                                                 "errorMessage": "Entity with part='part' not found"
                                            }
                                            """)
                            )
                    )
            })
    ResponseEntity<CommentsPageResponseDto> getAllCommentsByFilter(@Valid Filter filter, Pageable pageable);

    @Operation(
            method = "PUT",
            description = "Update Comment",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentRequestDto.class),
                            examples = @ExampleObject("""
                                    {
                                         "text": "New comment info."
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                              "id": 1,
                                              "text": "I sold my laptop... and bought ipad!",
                                              "username": "alya228",
                                              "newsId": 1,
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
    ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id,
                                                     @Valid @RequestBody CommentUpdateRequestDto dto);

    @Operation(
            method = "DELETE",
            description = "Delete comment by id",
            responses = {
                    @ApiResponse(
                            responseCode = "204"
                    )
            })
    ResponseEntity<Void> deleteComment(@PathVariable Long id);

}
