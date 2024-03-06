package ru.clevertec.news.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException;
import ru.clevertec.exceptionhandlerstarter.exception.RestControllerExceptionHandler;
import ru.clevertec.news.dto.request.CommentRequestDto;
import ru.clevertec.news.dto.request.CommentUpdateRequestDto;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.response.CommentAuthorResponseDto;
import ru.clevertec.news.dto.response.CommentResponseDto;
import ru.clevertec.news.dto.response.CommentsPageResponseDto;
import ru.clevertec.news.dto.response.UserManagementDto;
import ru.clevertec.news.model.Comment;
import ru.clevertec.news.service.impl.CommentServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.news.util.JsonConverterUtil.toJsonString;
import static ru.clevertec.news.util.domain.CommentAuthorResponseDtoTestData.getCommentAuthorResponseDto;
import static ru.clevertec.news.util.domain.CommentRequestDtoTestData.getCommentRequestDto;
import static ru.clevertec.news.util.domain.CommentResponseDtoTestData.getCommentResponseDto;
import static ru.clevertec.news.util.domain.CommentResponseDtoTestData.getListCommentResponseDto;
import static ru.clevertec.news.util.domain.Constants.NO_EXIST_ID;
import static ru.clevertec.news.util.domain.Constants.PAGE_NUMBER_0;
import static ru.clevertec.news.util.domain.Constants.PAGE_SIZE_20;
import static ru.clevertec.news.util.domain.Constants.STUB;
import static ru.clevertec.news.util.domain.UserManagementDtoTestData.getUserManagementDto;
import static ru.clevertec.news.util.domain.model.CommentTestData.COMMENT_ID;
import static ru.clevertec.news.util.domain.model.CommentTestData.getCommentBuilder;
import static ru.clevertec.news.util.domain.model.CommentTestData.getCommentList;
import static ru.clevertec.news.util.domain.model.NewsTestData.NEW_TEXT;

@RequiredArgsConstructor
@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CommentControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private final CommentServiceImpl commentService;

    @SpyBean
    private final RestControllerExceptionHandler exceptionHandler;

    private final String REQUEST_MAPPING = "/api/v1/comments";

    @Nested
    class CreateComment {

        @Test
        @SneakyThrows
        void checkCreateCommentShouldReturnCorrectResponse() {
            //given
            CommentRequestDto createDto = getCommentRequestDto();
            String jsonRequest = toJsonString(createDto);
            CommentResponseDto expectedDto = getCommentResponseDto();

            //when
            when(commentService.create(any()))
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
        void checkCreateCommentShouldReturnErrorDto() {
            //given
            CommentRequestDto createDto = new CommentRequestDto("", null, 2L);
            String notValidJsonRequest = toJsonString(createDto);
            CommentResponseDto expectedDto = getCommentResponseDto();

            //when
            when(commentService.create(any()))
                    .thenReturn(expectedDto);

            //then
            mockMvc.perform(post(REQUEST_MAPPING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(notValidJsonRequest))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorMessage").isNotEmpty(),
                            jsonPath("$.errorCode").isNotEmpty()
                    );
        }

        @Test
        @SneakyThrows
        void checkCreateCommentShouldReturnErrorDtoWhenUserByUsernameNotFound() {
            //given
            CommentRequestDto createDto = getCommentRequestDto();
            String jsonRequest = toJsonString(createDto);

            //when
            when(commentService.create(any()))
                    .thenThrow(EntityNotFoundException.class);

            //then
            mockMvc.perform(post(REQUEST_MAPPING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").isNotEmpty()
                    );
        }
    }

    @Nested
    class GetCommentById {

        @Test
        @SneakyThrows
        void checkGetCommentByIdShouldReturnCorrectDto() {
            //given
            CommentResponseDto responseDto = getCommentResponseDto();
            long testId = responseDto.id();

            //when
            when(commentService.findById(testId))
                    .thenReturn(responseDto);
            //then
            mockMvc.perform(get(REQUEST_MAPPING + "/" + testId))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.id").value(responseDto.id()),
                            jsonPath("$.newsId").value(responseDto.newsId())
                    );
        }

        @Test
        @SneakyThrows
        void checkGetCommentByIdShouldReturnNotFoundDto() {
            //given
            long testId = NO_EXIST_ID;

            //when
            when(commentService.findById(testId))
                    .thenThrow(EntityNotFoundException.class);

            //then
            mockMvc.perform(get(REQUEST_MAPPING + "/" + testId))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").isNotEmpty()
                    );
        }
    }

    @Nested
    class GetCommentAuthorByCommentId {

        @Test
        @SneakyThrows
        void checkGetCommentAuthorByCommentIdShouldReturnCorrectResponse() {
            //given
            Comment comment = getCommentBuilder().build();
            UserManagementDto userDto = getUserManagementDto(comment.getUsername());
            long testCommentId = comment.getId();
            CommentAuthorResponseDto responseDto = getCommentAuthorResponseDto(comment, userDto);

            //when
            when(commentService.findCommentWithAuthorById(testCommentId))
                    .thenReturn(responseDto);

            //then
            mockMvc.perform(get(REQUEST_MAPPING + "/" + testCommentId + "/author"))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.newsId").value(comment.getNews().getId()),
                            jsonPath("$.username").value(userDto.username()),
                            jsonPath("$.email").value(userDto.email())
                    );
        }

        @Test
        @SneakyThrows
        void checkGetCommentAuthorByCommentIdShouldReturnNotFoundResponse() {
            //when
            when(commentService.findCommentWithAuthorById(NO_EXIST_ID))
                    .thenThrow(EntityNotFoundException.class);

            //then
            mockMvc.perform(get(REQUEST_MAPPING + "/" + NO_EXIST_ID + "/author"))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").isNotEmpty()
                    );
        }
    }

    @Nested
    class GetAllComments {

        @Test
        @SneakyThrows
        void checkGetAllComments() {
            //given
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_20);
            List<CommentResponseDto> comments = getListCommentResponseDto(getCommentList());
            CommentsPageResponseDto expected = new CommentsPageResponseDto(comments, PAGE_SIZE_20, PAGE_NUMBER_0);

            //when
            when(commentService.findAll(pageable))
                    .thenReturn(expected);

            //then
            mockMvc.perform(get(REQUEST_MAPPING))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.pageSize").value(expected.pageSize()),
                            jsonPath("$.pageNumber").value(expected.pageNumber()),
                            jsonPath("$.comments.size()").value(comments.size())
                    );
        }
    }

    @Nested
    class GetAllCommentsByFilter {

        @Test
        @SneakyThrows
        void checkGetAllCommentsByFilter() {
            //given
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_20);
            List<CommentResponseDto> comments = getListCommentResponseDto(getCommentList());
            CommentsPageResponseDto expected = new CommentsPageResponseDto(comments, PAGE_SIZE_20, PAGE_NUMBER_0);
            LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
            requestParams.add("part", STUB);
            requestParams.add("fields", "text");
            requestParams.add("pageNumber", String.valueOf(PAGE_NUMBER_0));
            requestParams.add("pageSize", String.valueOf(PAGE_SIZE_20));

            //when
            when(commentService.findAllByFilter(any(), eq(pageable)))
                    .thenReturn(expected);

            //then
            mockMvc.perform(get(REQUEST_MAPPING + "/filter")
                            .params(requestParams))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.pageSize").value(expected.pageSize()),
                            jsonPath("$.pageNumber").value(expected.pageNumber()),
                            jsonPath("$.comments.size()").value(comments.size())
                    );
        }

        @Test
        @SneakyThrows
        void checkGetAllCommentsByFilterShouldCatchValidationFilterException() {
            //given
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_20);
            List<CommentResponseDto> comments = getListCommentResponseDto(getCommentList());
            CommentsPageResponseDto expected = new CommentsPageResponseDto(comments, PAGE_SIZE_20, PAGE_NUMBER_0);

            //when
            when(commentService.findAllByFilter(any(Filter.class), eq(pageable)))
                    .thenReturn(expected);

            //then
            mockMvc.perform(get(REQUEST_MAPPING + "/filter"))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").isNotEmpty(),
                            jsonPath("$.errorMessage").value("{fields=must not be null}")
                    );
        }
    }

    @Nested
    class UpdateComment {

        @Test
        @SneakyThrows
        void checkUpdateCommentShouldReturnCorrectResponse() {
            //given
            long testId = COMMENT_ID;
            CommentUpdateRequestDto testDto = new CommentUpdateRequestDto(NEW_TEXT);
            String jsonDtoRequest = toJsonString(testDto);
            CommentResponseDto expectedDto = getCommentResponseDto(NEW_TEXT);

            //when
            when(commentService.update(testId, testDto))
                    .thenReturn(expectedDto);

            //then
            mockMvc.perform(put(REQUEST_MAPPING + "/" + testId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonDtoRequest))
                    .andExpectAll(
                            status().is2xxSuccessful(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.id").value(expectedDto.id()),
                            jsonPath("$.text").value(expectedDto.text()),
                            jsonPath("$.newsId").value(expectedDto.newsId())
                    );
        }

        @Test
        @SneakyThrows
        void checkUpdateCommentShouldReturnNotFoundResponse() {
            //given
            long testId = NO_EXIST_ID;
            CommentUpdateRequestDto testDto = new CommentUpdateRequestDto(NEW_TEXT);
            String jsonDtoRequest = toJsonString(testDto);

            //when
            when(commentService.update(testId, testDto))
                    .thenThrow(EntityNotFoundException.class);

            //then
            mockMvc.perform(put(REQUEST_MAPPING + "/" + testId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonDtoRequest))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").isNotEmpty()
                    );
        }

        @Test
        @SneakyThrows
        void checkUpdateCommentShouldReturnNotValidResponse() {
            //given
            long testId = COMMENT_ID;
            CommentUpdateRequestDto testDto = new CommentUpdateRequestDto(null);
            String noValidJsonDtoRequest = toJsonString(testDto);

            //when//then
            mockMvc.perform(put(REQUEST_MAPPING + "/" + testId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(noValidJsonDtoRequest))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").isNotEmpty()
                    );
            verify(commentService, times(0)).update(any(), any());
        }
    }


    @Nested
    class DeleteComment {

        @Test
        @SneakyThrows
        void checkDeleteCommentShouldReturnCorrectStatus() {
            //given
            long testId = COMMENT_ID;

            //when
            doNothing().when(commentService).deleteById(testId);

            //then
            mockMvc.perform(delete(REQUEST_MAPPING + "/" + testId))
                    .andExpect(status().isNoContent());
        }
    }
}
