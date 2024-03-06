package ru.clevertec.news.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.response.NewsPageResponseDto;
import ru.clevertec.news.service.impl.NewsServiceImpl;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class IntegrationNewsServiceTest {

    private final NewsServiceImpl newsService;

    @ParameterizedTest
    @MethodSource("argsProviderForNewsFilter")
    void checkFindAllByFilter(String part, Integer expectedSize, Integer pageSize, Integer pageNumber) {
        //given
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Filter filter = new Filter(part, List.of("text", "title"));

        //when
        NewsPageResponseDto actual = newsService.findAllByFilter(filter, pageable);

        //then
        assertAll(
                () -> assertEquals(expectedSize, actual.news().size()),
                () -> assertEquals(pageNumber, actual.pageNumber()),
                () -> assertEquals(pageSize, actual.pageSize())
        );
    }

    static Stream<Arguments> argsProviderForNewsFilter() {
        return Stream.of(
                Arguments.of("programmers", 1, 2, 0),
                Arguments.of("programmer", 8, 10, 0),
                Arguments.of("programmer", 4, 4, 0),
                Arguments.of("Becau", 1, 2, 0)
        );
    }
}
