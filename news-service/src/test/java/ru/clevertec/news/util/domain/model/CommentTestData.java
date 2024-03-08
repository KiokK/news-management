package ru.clevertec.news.util.domain.model;

import ru.clevertec.news.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

import static ru.clevertec.news.util.domain.model.NewsTestData.getNewsBuilder;

public class CommentTestData {
    public static final Long COMMENT_ID = 1L;
    public static final String USERNAME = "user228";
    public static final String TEXT = "Test text. Bla-bla";
    public static final LocalDateTime CREATED_AT = LocalDateTime.of(2022, 2, 1, 10, 23, 44);
    public static final LocalDateTime MODIFIED_AT = LocalDateTime.of(2022, 2, 1, 10, 33, 44);

    public static final String NEW_TEXT = "Test text. NEW Bla-bla";

    public static Comment.CommentBuilder getCommentBuilder() {
        return Comment.builder()
                .id(COMMENT_ID)
                .username(USERNAME)
                .text(TEXT)
                .createdAt(CREATED_AT)
                .modifiedAt(MODIFIED_AT)
                .news(getNewsBuilder().build());
    }

    public static List<Comment> getCommentList() {
        return List.of(getCommentBuilder().build(), getCommentBuilder().id(2L).build());
    }

}
