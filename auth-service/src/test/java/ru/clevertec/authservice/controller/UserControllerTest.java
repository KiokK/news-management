package ru.clevertec.authservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import ru.clevertec.authservice.dto.request.UserRequestDto;
import ru.clevertec.authservice.dto.response.UserPageResponseDto;
import ru.clevertec.authservice.dto.response.UserResponseDto;
import ru.clevertec.authservice.exception.UserEntityException;
import ru.clevertec.authservice.service.impl.UserServiceImpl;
import ru.clevertec.exceptionhandlerstarter.exception.RestControllerExceptionHandler;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.authservice.util.Constants.PAGE_NUMBER_0;
import static ru.clevertec.authservice.util.Constants.PAGE_SIZE_2;
import static ru.clevertec.authservice.util.JsonConverterUtil.toJsonString;
import static ru.clevertec.authservice.util.UserRequestDtoTestData.getUserRequestDto;
import static ru.clevertec.authservice.util.UserResponseDtoTestData.getListUserResponseDto;
import static ru.clevertec.authservice.util.UserResponseDtoTestData.getUserResponseDto;
import static ru.clevertec.authservice.util.UserResponseDtoTestData.getUserResponseDtoWithBlankName;
import static ru.clevertec.authservice.util.UserTestData.USERNAME;
import static ru.clevertec.exceptionhandlerstarter.domain.ErrorCodeConstants.REQUEST_CONFLICT;

@RequiredArgsConstructor
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private final UserServiceImpl userService;

    @SpyBean
    private final RestControllerExceptionHandler exceptionHandler;

    private final String REQUEST_MAPPING = "/api/v1/users";

    @Nested
    class CreateUser {

        @Test
        @SneakyThrows
        void checkCreateUserShouldReturnUserResponseDto() {
            //given
            UserRequestDto createDto = getUserRequestDto();
            String jsonRequest = toJsonString(createDto);
            UserResponseDto expectedDto = getUserResponseDto();

            //when
            when(userService.create(any()))
                    .thenReturn(expectedDto);

            //then
            mockMvc.perform(post(REQUEST_MAPPING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpectAll(
                            status().isCreated(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.id").value(expectedDto.id().toString())
                    );
        }

        @Test
        @SneakyThrows
        void checkCreateUserShouldReturnErrorResponseDtoBecauseConflict() {
            //given
            UserRequestDto createDto = getUserRequestDto();
            String jsonRequest = toJsonString(createDto);

            //when
            when(userService.create(createDto))
                    .thenThrow(new UserEntityException());

            //then
            mockMvc.perform(post(REQUEST_MAPPING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpectAll(
                            status().isConflict(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").value(REQUEST_CONFLICT)
                    );
        }

        @Test
        @SneakyThrows
        void checkCreateUserShouldReturnErrorDto() {
            //given
            UserResponseDto noValidCreateDto = getUserResponseDtoWithBlankName();
            String jsonRequest = toJsonString(noValidCreateDto);

            //when//then
            mockMvc.perform(post(REQUEST_MAPPING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").isNotEmpty()
                    );
            verify(userService, times(0)).create(any());
        }
    }

    @Nested
    class GetUserByUsername {

        @Test
        @SneakyThrows
        void checkGetUserByUsernameShouldReturnCorrectResponse() {
            //given
            UserResponseDto responseDto = getUserResponseDto();
            String testUsername = responseDto.username();

            //when
            when(userService.findByUsername(testUsername))
                    .thenReturn(responseDto);
            //then
            mockMvc.perform(get(REQUEST_MAPPING + "/" + testUsername))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.id").value(responseDto.id()),
                            jsonPath("$.email").value(responseDto.email()),
                            jsonPath("$.username").value(responseDto.username())
                    );
        }

        @Test
        @SneakyThrows
        void checkGetUserByUsernameShouldReturnErrorDto() {
            //given
            String noExistUsername = USERNAME;

            //when
            when(userService.findByUsername(noExistUsername))
                    .thenThrow(UsernameNotFoundException.class);
            //then
            mockMvc.perform(get(REQUEST_MAPPING + "/" + noExistUsername))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").isNotEmpty()
                    );
        }
    }

    @Nested
    class GetAllUsers {

        @Test
        @SneakyThrows
        void checkGetAllUsers() {
            //given
            List<UserResponseDto> users = getListUserResponseDto();
            UserPageResponseDto userPageResponseDto = new UserPageResponseDto(users, PAGE_SIZE_2, PAGE_NUMBER_0);
            LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
            requestParams.add("pageNumber", String.valueOf(PAGE_NUMBER_0));
            requestParams.add("pageSize", String.valueOf(PAGE_SIZE_2));

            //when
            when(userService.findAll(any(Pageable.class)))
                    .thenReturn(userPageResponseDto);

            //then
            mockMvc.perform(get(REQUEST_MAPPING)
                    )
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.users.size()").value(userPageResponseDto.users().size()),
                            jsonPath("$.pageNumber").value(userPageResponseDto.pageNumber()),
                            jsonPath("$.pageSize").value(userPageResponseDto.pageSize())
                    );
        }
    }
}
