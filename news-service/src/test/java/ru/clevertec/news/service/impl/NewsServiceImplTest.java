package ru.clevertec.news.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.request.NewsRequestDto;
import ru.clevertec.news.dto.response.NewsPageResponseDto;
import ru.clevertec.news.dto.response.NewsResponseDto;
import ru.clevertec.news.exception.ValidationException;
import ru.clevertec.news.model.News;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.util.domain.NewsResponseDtoTestData;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.news.util.domain.Constants.NO_EXIST_ID;
import static ru.clevertec.news.util.domain.Constants.PAGE_NUMBER_0;
import static ru.clevertec.news.util.domain.Constants.PAGE_SIZE_2;
import static ru.clevertec.news.util.domain.Constants.STUB;
import static ru.clevertec.news.util.domain.NewsRequestDtoTestData.getNewsRequestDtoTestData;
import static ru.clevertec.news.util.domain.NewsResponseDtoTestData.getListNewsResponseDto;
import static ru.clevertec.news.util.domain.NewsResponseDtoTestData.getNewsResponseDto;
import static ru.clevertec.news.util.domain.model.NewsTestData.NEWS_ID;
import static ru.clevertec.news.util.domain.model.NewsTestData.NEW_TEXT;
import static ru.clevertec.news.util.domain.model.NewsTestData.getNewsBuilder;
import static ru.clevertec.news.util.domain.model.NewsTestData.getNewsList;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @InjectMocks
    private NewsServiceImpl newsService;

    @Mock
    private NewsRepository newsRepository;

    @Nested
    class Create {

        @Test
        void checkCreateShouldReturnCreatedDto() {
            //given
            NewsRequestDto newsDto = getNewsRequestDtoTestData();
            News createdNews = getNewsBuilder().build();
            News newNews = getNewsBuilder()
                    .id(null)
                    .createdAt(null)
                    .modifiedAt(null).build();
            NewsResponseDto expected = getNewsResponseDto(createdNews);

            //when
            when(newsRepository.save(newNews)).thenReturn(createdNews);
            NewsResponseDto actual = newsService.create(newsDto);

            //then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class FindById {

        @Test
        void checkFindByIdShouldReturnNewsResponseDto() {
            //given
            Long findId = NEWS_ID;
            News foundNews = getNewsBuilder().build();
            NewsResponseDto expected = getNewsResponseDto(foundNews);

            //when
            when(newsRepository.findById(findId)).thenReturn(Optional.of(foundNews));
            NewsResponseDto actual = newsService.findById(findId);

            //then
            assertEquals(expected, actual);
        }

        @Test
        void checkFindByIdShouldThrowEntityNotFoundException() {
            //given
            Long noExistId = NO_EXIST_ID;
            String expectedMessage = String.format(EntityNotFoundException.MESSAGE_WITH_ID, noExistId);

            //when
            when(newsRepository.findById(noExistId)).thenReturn(Optional.empty());

            //then
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> newsService.findById(noExistId));
            assertEquals(expectedMessage, exception.getMessage());
        }

    }

    @Nested
    class FindAll {

        @Test
        void checkFindAllShouldReturnNewsResponseDto() {
            //given
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_2);
            List<News> foundNewses = getNewsList();
            List<NewsResponseDto> expectedNews = getListNewsResponseDto(foundNewses);

            //when
            when(newsRepository.findAll(pageable)).thenReturn(new PageImpl(foundNewses));
            NewsPageResponseDto actual = newsService.findAll(pageable);

            //list
            assertAll(
                    () -> assertEquals(expectedNews, actual.news()),
                    () -> assertEquals(PAGE_NUMBER_0, actual.pageNumber()),
                    () -> assertEquals(PAGE_SIZE_2, actual.pageSize())
            );
        }
    }

    @Nested
    class FindAllByFilter {

        @Test
        void checkFindAllByFilter() {
            //given
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_2);
            Filter testFilter = new Filter(STUB, List.of("text"));
            List<News> newsList = getNewsList();
            List<NewsResponseDto> expectedList = NewsResponseDtoTestData.getListNewsResponseDto(newsList);

            //when
            when(newsRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(new PageImpl<>(newsList));
            NewsPageResponseDto actual = newsService.findAllByFilter(testFilter, pageable);

            //then
            assertAll(
                    () -> assertEquals(PAGE_SIZE_2, actual.pageSize()),
                    () -> assertEquals(PAGE_NUMBER_0, actual.pageNumber()),
                    () -> assertEquals(expectedList, actual.news()),
                    () -> verify(newsRepository, times(0)).findAll(pageable)
            );
        }
        @Test
        void checkFindAllByFilterThrowsValidationException() {
            //given
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_2);
            Filter testFilter = new Filter(STUB, List.of("t_t"));

            //then
            assertAll(
                    () -> assertThrows(ValidationException.class, () -> newsService.findAllByFilter(testFilter, pageable)),
                    () -> verify(newsRepository, times(0)).findAll(any(Specification.class), eq(pageable)),
                    () -> verify(newsRepository, times(0)).findAll(pageable)
            );
        }
    }

    @Nested
    class Update {

        @Test
        void checkUpdateShouldReturnUpdated() {
            //given
            Long newsId = NEWS_ID;
            News currentNews = getNewsBuilder().build();
            News modifiedNews = getNewsBuilder().text(NEW_TEXT).build();
            News updatedNews = getNewsBuilder().text(NEW_TEXT).modifiedAt(now()).build();
            NewsRequestDto modifiedDto = getNewsRequestDtoTestData(modifiedNews);
            NewsResponseDto expected = getNewsResponseDto(updatedNews);

            //when
            when(newsRepository.findById(newsId)).thenReturn(Optional.of(currentNews));
            when(newsRepository.save(modifiedNews)).thenReturn(updatedNews);
            NewsResponseDto actual = newsService.update(newsId, modifiedDto);

            //then
            assertEquals(expected, actual);
        }

        @Test
        void checkUpdateShouldThrowsEntityNotFoundException() {
            //given
            Long noExistId = NO_EXIST_ID;
            NewsRequestDto updateDto = getNewsRequestDtoTestData();

            //when
            when(newsRepository.findById(noExistId)).thenReturn(Optional.empty());

            //then
            assertAll(
                    () -> assertThrows(EntityNotFoundException.class, () -> newsService.update(noExistId, updateDto)),
                    () -> verify(newsRepository, times(0)).save(any())
            );
        }
    }

    @Nested
    class DeleteById {

        @Test
        void checkDeleteByIdVerify() {
            //given
            Long deleteId = NEWS_ID;

            //when
            doNothing().when(newsRepository)
                    .deleteById(deleteId);
            newsService.deleteById(deleteId);

            //then
            verify(newsRepository).deleteById(deleteId);
        }
    }
}
