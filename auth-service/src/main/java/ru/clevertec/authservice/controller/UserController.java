package ru.clevertec.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.authservice.controller.openapi.UserControllerOpenApi;
import ru.clevertec.authservice.dto.request.UserRequestDto;
import ru.clevertec.authservice.dto.response.UserPageResponseDto;
import ru.clevertec.authservice.dto.response.UserResponseDto;
import ru.clevertec.authservice.service.UserService;
import ru.clevertec.logginstarter.annotation.CustomMethodLog;

@RestController
@CustomMethodLog
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerOpenApi {

    private final UserService userService;

    @Override
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(userRequestDto));
    }

    @Override
    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @Override
    @GetMapping
    public ResponseEntity<UserPageResponseDto> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }
}
