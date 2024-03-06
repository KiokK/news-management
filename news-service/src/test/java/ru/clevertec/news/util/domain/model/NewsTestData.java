package ru.clevertec.news.util.domain.model;

import ru.clevertec.news.model.News;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NewsTestData {

    public static final Long NEWS_ID = 1L;
    public static final String TITLE = "Test title";
    public static final String TEXT = "Test text. Bla-bla";
    public static final String NEW_TEXT = "Test text. NEW Bla-bla";
    public static final LocalDateTime CREATED_AT = LocalDateTime.of(2022, 1, 1, 10, 23, 44);
    public static final LocalDateTime MODIFIED_AT = LocalDateTime.of(2022, 1, 1, 10, 33, 44);

    public static News.NewsBuilder getNewsBuilder() {
        return News.builder()
                .id(NEWS_ID)
                .text(TEXT)
                .title(TITLE)
                .createdAt(CREATED_AT)
                .modifiedAt(MODIFIED_AT)
                .comments(new ArrayList<>());
    }

    public static List<News> getNewsList() {
        return List.of(getNewsBuilder().build(), getNewsBuilder().id(2L).build());
    }
}
