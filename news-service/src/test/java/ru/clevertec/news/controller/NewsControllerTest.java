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
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.request.NewsRequestDto;
import ru.clevertec.news.dto.response.NewsPageResponseDto;
import ru.clevertec.news.dto.response.NewsResponseDto;
import ru.clevertec.news.dto.response.NewsWithCommentResponseDto;
import ru.clevertec.news.dto.response.NewsWithPageCommentsResponseDto;
import ru.clevertec.news.service.impl.CommentServiceImpl;
import ru.clevertec.news.service.impl.NewsServiceImpl;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.news.util.JsonConverterUtil.toJsonString;
import static ru.clevertec.news.util.domain.Constants.NO_EXIST_ID;
import static ru.clevertec.news.util.domain.Constants.PAGE_NUMBER_0;
import static ru.clevertec.news.util.domain.Constants.PAGE_SIZE_20;
import static ru.clevertec.news.util.domain.Constants.STUB;
import static ru.clevertec.news.util.domain.NewsPageResponseDtoTestData.getNewsPageResponseDto;
import static ru.clevertec.news.util.domain.NewsRequestDtoTestData.getNewsRequestDtoTestData;
import static ru.clevertec.news.util.domain.NewsResponseDtoTestData.getNewsResponseDto;
import static ru.clevertec.news.util.domain.NewsWithCommentResponseDtoTestData.getNewsWithCommentResponseDto;
import static ru.clevertec.news.util.domain.NewsWithPageCommentsResponseDtoTestData.getNewsWithPageCommentsResponseDto;
import static ru.clevertec.news.util.domain.model.CommentTestData.COMMENT_ID;
import static ru.clevertec.news.util.domain.model.NewsTestData.NEWS_ID;
import static ru.clevertec.news.util.domain.model.NewsTestData.getNewsBuilder;

@RequiredArgsConstructor
@WebMvcTest(NewsController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class NewsControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private final NewsServiceImpl newsService;

    @MockBean
    private final CommentServiceImpl commentService;

    @SpyBean
    private final RestControllerExceptionHandler exceptionHandler;

    private final String REQUEST_MAPPING = "/api/v1/news";

    @Nested
    class CreateNews {

        @Test
        @SneakyThrows
        void checkCreateNewsShouldReturnCorrectResponse() {
            //given
            NewsRequestDto createDto = getNewsRequestDtoTestData();
            String jsonRequest = toJsonString(createDto);
            NewsResponseDto expectedDto = getNewsResponseDto();

            //when
            when(newsService.create(createDto))
                    .thenReturn(expectedDto);

            //then
            mockMvc.perform(post(REQUEST_MAPPING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpectAll(
                            status().isCreated(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.id").value(expectedDto.id().toString()),
                            jsonPath("$.title").value(expectedDto.title()),
                            jsonPath("$.text").value(expectedDto.text())
                    );
        }

        @Test
        @SneakyThrows
        void checkCreateNewsShouldReturnErrorDto() {
            //given
            NewsRequestDto createDto = new NewsRequestDto("", null);
            String notValidJsonRequest = toJsonString(createDto);
            NewsResponseDto expectedDto = getNewsResponseDto();

            //when
            when(newsService.create(createDto))
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
    }

    @Nested
    class GetNewsById {

        @Test
        @SneakyThrows
        void checkGetNewsByIdShouldReturnCorrectResponse() {
            //given
            long foundId = NEWS_ID;
            NewsResponseDto expectedDto = getNewsResponseDto();

            //when
            when(newsService.findById(foundId))
                    .thenReturn(expectedDto);

            //then
            mockMvc.perform(get(REQUEST_MAPPING+"/"+foundId))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.id").value(expectedDto.id().toString()),
                            jsonPath("$.title").value(expectedDto.title()),
                            jsonPath("$.text").value(expectedDto.text())
                    );
        }

        @Test
        @SneakyThrows
        void checkGetNewsByIdShouldReturnErrorDto() {
            //when
            when(newsService.findById(NO_EXIST_ID))
                    .thenThrow(EntityNotFoundException.class);

            //then
            mockMvc.perform(get(REQUEST_MAPPING+"/"+NO_EXIST_ID))
                    .andExpectAll(
                            status().isNotFound(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").isNotEmpty()
                    );
        }
    }

    @Nested
    class GetNewsByIdWithComments {

        @Test
        @SneakyThrows
        void checkGetNewsByIdWithCommentsShouldReturnCorrectDto() {
            //given
            long foundId = NEWS_ID;
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_20);
            NewsWithPageCommentsResponseDto foundNews = getNewsWithPageCommentsResponseDto(getNewsBuilder().build(), pageable);

            //when
            when(commentService.findAllByNewsId(foundId, pageable))
                    .thenReturn(foundNews);

            //then
            mockMvc.perform(get(String.format("%s/%s/comments", REQUEST_MAPPING, foundId)))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.id").value(foundNews.id()),
                            jsonPath("$.comments.size()").value(foundNews.comments().size()),
                            jsonPath("$.text").value(foundNews.text()),
                            jsonPath("$.title").value(foundNews.title()),
                            jsonPath("$.pageNumber").value(foundNews.pageNumber()),
                            jsonPath("$.pageSize").value(foundNews.pageSize())
                    );
        }

        @Test
        @SneakyThrows
        void checkGetNewsByIdWithCommentsShouldReturnErrorDto() {
            //given
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_20);

            //when
            when(commentService.findAllByNewsId(NO_EXIST_ID, pageable))
                    .thenThrow(EntityNotFoundException.class);

            //then
            mockMvc.perform(get(String.format("%s/%s/comments", REQUEST_MAPPING, NO_EXIST_ID)))
                    .andExpectAll(
                            status().isNotFound(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").isNotEmpty()
                    );
        }
    }

    @Nested
    class GetNewsByIdWithComment {

        @Test
        @SneakyThrows
        void checkGetNewsByIdWithCommentShouldReturnCorrectDto() {
            //given
            long newsId = NEWS_ID;
            long commentId = COMMENT_ID;
            NewsWithCommentResponseDto foundComment = getNewsWithCommentResponseDto();

            //when
            when(commentService.findByNewsIdAndCommentId(newsId, commentId))
                    .thenReturn(foundComment);

            //then
            mockMvc.perform(get(String.format("%s/%s/comments/%s", REQUEST_MAPPING, newsId, commentId)))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.news.id").value(foundComment.news().id()),
                            jsonPath("$.comment.id").value(foundComment.comment().id()),
                            jsonPath("$.comment.text").value(foundComment.comment().text()),
                            jsonPath("$.news.text").value(foundComment.news().text()),
                            jsonPath("$.news.title").value(foundComment.news().title())
                    );
        }

        @Test
        @SneakyThrows
        void checkGetNewsByIdWithCommentShouldReturnErrorDtoNoFoundNews() {
            //when
            when(commentService.findByNewsIdAndCommentId(NO_EXIST_ID, COMMENT_ID))
                    .thenThrow(EntityNotFoundException.class);

            //then
            mockMvc.perform(get(String.format("%s/%s/comments/%s", REQUEST_MAPPING, NO_EXIST_ID, COMMENT_ID)))
                    .andExpectAll(
                            status().isNotFound(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").isNotEmpty()
                    );
        }

        @Test
        @SneakyThrows
        void checkGetNewsByIdWithCommentShouldReturnErrorDtoNoFoundComment() {
            //when
            when(commentService.findByNewsIdAndCommentId(NEWS_ID, NO_EXIST_ID))
                    .thenThrow(EntityNotFoundException.class);

            //then
            mockMvc.perform(get(String.format("%s/%s/comments/%s", REQUEST_MAPPING, NEWS_ID, NO_EXIST_ID)))
                    .andExpectAll(
                            status().isNotFound(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").isNotEmpty()
                    );
        }
    }

    @Nested
    class GetAllNews {

        @Test
        @SneakyThrows
        void checkGetAllNews() {
            //given
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_20);
            NewsPageResponseDto expected = getNewsPageResponseDto(pageable);

            //when
            when(newsService.findAll(pageable))
                    .thenReturn(expected);

            //then
            mockMvc.perform(get(REQUEST_MAPPING))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.news.size()").value(expected.news().size()),
                            jsonPath("$.pageNumber").value(expected.pageNumber()),
                            jsonPath("$.pageSize").value(expected.pageSize())
                    );
        }
    }

    @Nested
    class GetAllNewsByFilter {

        @Test
        @SneakyThrows
        void checkGetAllNewsByFilter() {
            //given
            Filter filter = new Filter(STUB, List.of("text", "title"));
            PageRequest pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_20);
            NewsPageResponseDto expected = getNewsPageResponseDto(pageable);
            LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
            requestParams.add("part", STUB);
            requestParams.add("fields", "text,title");
            requestParams.add("pageNumber", String.valueOf(PAGE_NUMBER_0));
            requestParams.add("pageSize", String.valueOf(PAGE_SIZE_20));

            //when
            when(newsService.findAllByFilter(filter, pageable))
                    .thenReturn(expected);

            //then
            mockMvc.perform(get(REQUEST_MAPPING+"/filter")
                                    .params(requestParams))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.news.size()").value(expected.news().size()),
                            jsonPath("$.pageNumber").value(expected.pageNumber()),
                            jsonPath("$.pageSize").value(expected.pageSize())
                    );
        }
    }

    @Nested
    class UpdateNews {

        @Test
        @SneakyThrows
        void checkUpdateNewsShouldReturnUpdatedDto() {
            //given
            long testId = NEWS_ID;
            NewsRequestDto updateDto = getNewsRequestDtoTestData();
            String jsonRequest = toJsonString(updateDto);
            NewsResponseDto expectedDto = getNewsResponseDto();

            //when
            when(newsService.update(testId, updateDto))
                    .thenReturn(expectedDto);

            //then
            mockMvc.perform(put(REQUEST_MAPPING + "/" + testId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpectAll(
                            status().is2xxSuccessful(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.id").value(expectedDto.id().toString()),
                            jsonPath("$.title").value(expectedDto.title()),
                            jsonPath("$.text").value(expectedDto.text())
                    );
        }

        @Test
        @SneakyThrows
        void checkUpdateNewsShouldReturnErrorDto() {
            //given
            NewsRequestDto updateDto = getNewsRequestDtoTestData();
            String jsonRequest = toJsonString(updateDto);

            //when
            when(newsService.update(NO_EXIST_ID, updateDto))
                    .thenThrow(EntityNotFoundException.class);

            //then
            mockMvc.perform(put(REQUEST_MAPPING + "/" + NO_EXIST_ID)
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
    class DeleteNews {

        @Test
        @SneakyThrows
        void checkDeleteNewsShouldReturnCorrectStatus() {
            //when
            doNothing().when(newsService).deleteById(NEWS_ID);

            //then
            mockMvc.perform(delete(REQUEST_MAPPING + "/" + NEWS_ID))
                    .andExpect(status().isNoContent());
        }
    }
}
