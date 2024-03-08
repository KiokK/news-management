package ru.clevertec.authservice.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.clevertec.authservice.dto.request.UserRequestDto;
import ru.clevertec.authservice.dto.response.UserPageResponseDto;
import ru.clevertec.authservice.dto.response.UserResponseDto;
import ru.clevertec.exceptionhandlerstarter.domain.ErrorResponseDto;

@Tag(name = "User")
@SecurityRequirement(name = "jwt")
public interface UserControllerOpenApi {

    @Operation(
            method = "POST",
            description = "Create new user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRequestDto.class),
                            examples = @ExampleObject("""
                                    {
                                      "username": "kirieshka",
                                      "email": "kirieshka@flint.aga",
                                      "password": "12345Qw_",
                                      "role": "ADMIN"
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                              "id": 1,
                                              "username": "kirieshka",
                                              "email": "kirieshka@flint.aga"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Not valid",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                              "errorMessage": "{username=must be like ^(?=[a-zA-Z0-9._]{4,20}$)(?!.*[_.]{2})[^_.].*[^_.]$}",
                                              "errorCode": 400000
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
                                              "errorMessage": "Entity not found",
                                              "errorCode": 404400
                                            }
                                            """)
                            )
                    )
            })
    ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto);

    @Operation(
            method = "GET",
            description = "Get user by username",
            parameters = {
                    @Parameter(name = "username", description = "User username", example = "kiok")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                              "id": 1,
                                              "username": "kiok",
                                              "email": "kiok@mail.ru"
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
                                              "errorMessage": "Entity with username='bibibi' not found",
                                              "errorCode": 404400
                                            }
                                            """)
                            )
                    )
            })
    ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username);

    @Operation(
            method = "GET",
            description = "Get all users with pageable info",
            parameters = {
                    @Parameter(name = "pageSize", description = "Page size", example = "2"),
                    @Parameter(name = "pageNumber", description = "Number page", example = "0"),
                    @Parameter(name = "sort", description = "Sort by field", example = "username,desc")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserPageResponseDto.class),
                                    examples = @ExampleObject("""
                                            {
                                               "users": [
                                                 {
                                                   "id": 5,
                                                   "username": "HoRoShiDen",
                                                   "email": "HoRoShiDen@gmail.com"
                                                 },
                                                 {
                                                   "id": 6,
                                                   "username": "aifhijaio_12",
                                                   "email": "aifhijaio12@gmail.com"
                                                 }
                                               ],
                                               "pageSize": 2,
                                               "pageNumber": 0
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
                                              "errorMessage": "Entity not found",
                                              "errorCode": 404400
                                            }
                                            """)
                            )
                    )
            })
    ResponseEntity<UserPageResponseDto> getAllUsers(Pageable pageable);

}
