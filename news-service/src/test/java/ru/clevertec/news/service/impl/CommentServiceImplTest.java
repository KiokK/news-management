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
import ru.clevertec.news.client.ManagementUserClient;
import ru.clevertec.news.dto.request.CommentUpdateRequestDto;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.response.CommentResponseDto;
import ru.clevertec.news.dto.response.CommentsPageResponseDto;
import ru.clevertec.news.dto.response.NewsWithCommentResponseDto;
import ru.clevertec.news.dto.response.NewsWithPageCommentsResponseDto;
import ru.clevertec.news.model.Comment;
import ru.clevertec.news.model.News;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.repository.NewsRepository;

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
import static ru.clevertec.news.util.domain.CommentResponseDtoTestData.getCommentResponseDto;
import static ru.clevertec.news.util.domain.CommentResponseDtoTestData.getListCommentResponseDto;
import static ru.clevertec.news.util.domain.Constants.NO_EXIST_ID;
import static ru.clevertec.news.util.domain.Constants.PAGE_NUMBER_0;
import static ru.clevertec.news.util.domain.Constants.PAGE_SIZE_2;
import static ru.clevertec.news.util.domain.Constants.PAGE_SIZE_3;
import static ru.clevertec.news.util.domain.Constants.STUB;
import static ru.clevertec.news.util.domain.NewsWithCommentResponseDtoTestData.getNewsWithCommentResponseDto;
import static ru.clevertec.news.util.domain.NewsWithPageCommentsResponseDtoTestData.getNewsWithPageCommentsResponseDto;
import static ru.clevertec.news.util.domain.model.CommentTestData.COMMENT_ID;
import static ru.clevertec.news.util.domain.model.CommentTestData.getCommentBuilder;
import static ru.clevertec.news.util.domain.model.CommentTestData.getCommentList;
import static ru.clevertec.news.util.domain.model.NewsTestData.NEWS_ID;
import static ru.clevertec.news.util.domain.model.NewsTestData.NEW_TEXT;
import static ru.clevertec.news.util.domain.model.NewsTestData.getNewsBuilder;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private ManagementUserClient userClient;

    @Nested
    class Create {

//        @Test
//        void checkCreateShouldReturnCommentResponseDto() {
//            //given
//            CommentRequestDto createDto = getCommentRequestDto();
//            Comment newComment = getCommentBuilder()
//                    .id(null)
//                    .createdAt(null)
//                    .modifiedAt(null)
//                    .build();
//            String username = newComment.getUsername();
//            Comment createdComment = getCommentBuilder().build();
//            CommentResponseDto expected = getCommentResponseDto();
//
//            //when
//            when(userClient.getUserByUsername(username, ""))
//                    .thenReturn(ResponseEntity.ok(getUserManagementDto(username)));
//            when(commentRepository.save(newComment))
//                    .thenReturn(createdComment);
//            CommentResponseDto actual = commentService.create(createDto);
//
//            //then
//            assertEquals(expected, actual);
//        }

//        @Test
//        void checkCreateShouldThrowEntityNotFound() {
//            //given
//            CommentRequestDto createDto = getCommentRequestDto();
//            Comment newComment = getCommentBuilder()
//                    .id(null)
//                    .createdAt(null)
//                    .modifiedAt(null)
//                    .build();
//
//            //when
//            when(userClient.getUserByUsername(newComment.getUsername(), ""))
//                    .thenThrow(EntityNotFoundException.class);
//
//            //then
//            assertAll(
//                    () -> assertThrows(EntityNotFoundException.class, () -> commentService.create(createDto)),
//                    () -> verify(commentRepository, times(0)).save(any())
//            );
//        }
    }

    @Nested
    class FindById {

        @Test
        void checkFindByIdShouldReturnCommentResponseDto() {
            //given
            long findId = COMMENT_ID;
            Comment findComment = getCommentBuilder().build();
            CommentResponseDto expected = getCommentResponseDto();

            //when
            when(commentRepository.findById(findId))
                    .thenReturn(Optional.of(findComment));
            CommentResponseDto actual = commentService.findById(findId);

            //then
            assertEquals(expected, actual);
        }

        @Test
        void checkFindByIdShouldThrowEntityNotFoundException() {
            //given
            long noExistId = NO_EXIST_ID;

            //when
            when(commentRepository.findById(noExistId))
                    .thenReturn(Optional.empty());

            //then
            assertThrows(EntityNotFoundException.class, () -> commentService.findById(noExistId));
        }
    }

    @Nested
    class FindCommentWithAuthorById {

//        @Test
//        void checkFindCommentWithAuthorByIdShouldReturnDto() {
//            //given
//            Comment comment = getCommentBuilder().build();
//            long commentId = comment.getId();
//            String username = comment.getUsername();
//            UserManagementDto userManagementDto = getUserManagementDto(username);
//
//            //when
//            when(commentRepository.findById(commentId))
//                    .thenReturn(Optional.of(comment));
//            when(userClient.getUserByUsername(username, ""))
//                    .thenReturn(ResponseEntity.ok(userManagementDto));
//            CommentAuthorResponseDto actual = commentService.findCommentWithAuthorById(commentId);
//
//            //then
//            assertAll(
//                    () -> assertEquals(commentId, actual.commentId()),
//                    () -> assertEquals(username, actual.username()),
//                    () -> assertEquals(comment.getNews().getId(), actual.newsId())
//            );
//        }

        @Test
        void checkFindCommentWithAuthorByIdShouldThrowEntityNotFoundException() {
            //given
            long commentId = 1L;

            //when
            when(commentRepository.findById(commentId))
                    .thenReturn(Optional.empty());

            //then
            assertAll(
                    () -> assertThrows(EntityNotFoundException.class,
                            () -> commentService.findCommentWithAuthorById(commentId)),
                    () -> verify(userClient, times(0)).getUserByUsername(any(), any())
            );
        }
    }

    @Nested
    class FindByNewsIdAndCommentId {

        @Test
        void checkFindByNewsIdAndCommentIdShouldReturnNewsWithCommentResponseDto() {
            //given
            long newsId = NEWS_ID;
            long commentId = COMMENT_ID;
            NewsWithCommentResponseDto expected = getNewsWithCommentResponseDto();
            Comment foundComment = getCommentBuilder().build();

            //when
            when(commentRepository.findById(commentId))
                    .thenReturn(Optional.of(foundComment));
            NewsWithCommentResponseDto actual = commentService.findByNewsIdAndCommentId(newsId, commentId);

            //then
            assertEquals(expected, actual);
        }

        @Test
        void checkFindByNewsIdAndCommentIdShouldThrowEntityNotFoundByNews() {
            //given
            long newsIdNoExist = NO_EXIST_ID;
            long commentId = COMMENT_ID;
            Comment foundComment = getCommentBuilder().build();

            //when
            when(commentRepository.findById(commentId))
                    .thenReturn(Optional.of(foundComment));

            //then
            assertThrows(EntityNotFoundException.class,
                    () -> commentService.findByNewsIdAndCommentId(newsIdNoExist, commentId));
        }

        @Test
        void checkFindByNewsIdAndCommentIdShouldThrowEntityNotFoundByComment() {
            //given
            long newsId = NEWS_ID;
            long commentIdNoExist = NO_EXIST_ID;

            //when
            when(commentRepository.findById(commentIdNoExist))
                    .thenReturn(Optional.empty());

            //then
            assertThrows(EntityNotFoundException.class,
                    () -> commentService.findByNewsIdAndCommentId(newsId, commentIdNoExist));

        }
    }

    @Nested
    class FindAll {

        @Test
        void checkFindAllShouldReturnCommentsPageResponseDto() {
            //given
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_2);
            List<Comment> comments = getCommentList();
            List<CommentResponseDto> expectedList = getListCommentResponseDto(comments);

            //when
            when(commentRepository.findAll(pageable))
                    .thenReturn(new PageImpl<>(comments));
            CommentsPageResponseDto actual = commentService.findAll(pageable);

            //then
            assertAll(
                    () -> assertEquals(expectedList, actual.comments()),
                    () -> assertEquals(pageable.getPageNumber(), actual.pageNumber()),
                    () -> assertEquals(pageable.getPageSize(), actual.pageSize())
            );
        }
    }

    @Nested
    class FindAllByNewsId {

        @Test
        void checkFindAllByNewsIdShouldReturnNewsWithPageCommentsResponseDto() {
            //given
            long findNewsId = NEWS_ID;
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_3);
            News foundNews = getNewsBuilder().build();
            List<Comment> foundNewsesComments = getCommentList();
            foundNews.setComments(foundNewsesComments);
            NewsWithPageCommentsResponseDto expected = getNewsWithPageCommentsResponseDto(foundNews, pageable);

            //when
            when(newsRepository.findById(findNewsId))
                    .thenReturn(Optional.of(foundNews));
            when(commentRepository.findAllByNews_Id(findNewsId, pageable))
                    .thenReturn(new PageImpl<>(foundNewsesComments));
            NewsWithPageCommentsResponseDto actual = commentService.findAllByNewsId(findNewsId, pageable);

            //then
            assertEquals(expected, actual);
        }

        @Test
        void checkFindAllByNewsIdShouldThrowEntityNotFoundException() {
            //given
            long noExistFindNewsId = NEWS_ID;
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_3);
            News foundNews = getNewsBuilder().build();
            List<Comment> foundNewsesComments = getCommentList();
            foundNews.setComments(foundNewsesComments);

            //when
            when(newsRepository.findById(noExistFindNewsId))
                    .thenReturn(Optional.empty());

            //then
            assertAll(
                    () -> assertThrows(EntityNotFoundException.class,
                            () -> commentService.findAllByNewsId(noExistFindNewsId, pageable)),
                    () -> verify(commentRepository, times(0)).findAllByNews_Id(any(), any())
            );
        }
    }

    @Nested
    class FindAllByFilter {

        @Test
        void checkFindAllByFilterShouldReturnFiltered() {
            //given
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_2);
            Filter testFilter = new Filter(STUB, List.of("text"));
            List<Comment> comments = getCommentList();
            List<CommentResponseDto> expectedList = getListCommentResponseDto(comments);

            //when
            when(commentRepository.findAll(any(Specification.class), eq(pageable)))
                    .thenReturn(new PageImpl<>(comments));
            CommentsPageResponseDto actual = commentService.findAllByFilter(testFilter, pageable);

            //then
            assertAll(
                    () -> assertEquals(PAGE_SIZE_2, actual.pageSize()),
                    () -> assertEquals(PAGE_NUMBER_0, actual.pageNumber()),
                    () -> assertEquals(expectedList, actual.comments()),
                    () -> verify(commentRepository, times(0)).findAll(pageable)
            );
        }
    }

    @Nested
    class Update {

        @Test
        void checkUpdateShouldReturnUpdatedCommentResponseDto() {
            //given
            long testId = COMMENT_ID;
            String updateText = NEW_TEXT;
            CommentUpdateRequestDto updateDto = new CommentUpdateRequestDto(updateText);
            Comment currentComment = getCommentBuilder().build();
            Comment updateComment = getCommentBuilder()
                    .text(updateText)
                    .build();
            Comment savedComment = getCommentBuilder()
                    .text(updateText)
                    .modifiedAt(now())
                    .build();
            CommentResponseDto expected = getCommentResponseDto(savedComment);

            //when
            when(commentRepository.findById(testId))
                    .thenReturn(Optional.of(currentComment));
            when(commentRepository.save(updateComment))
                    .thenReturn(savedComment);
            CommentResponseDto actual = commentService.update(testId, updateDto);

            //then
            assertEquals(expected, actual);
        }

        @Test
        void checkUpdateShouldThrowEntityNotFoundException() {
            //given
            long testId = COMMENT_ID;
            CommentUpdateRequestDto updateDto = new CommentUpdateRequestDto(NEW_TEXT);

            //when
            when(commentRepository.findById(testId))
                    .thenReturn(Optional.empty());

            //then
            assertAll(
                    () -> assertThrows(EntityNotFoundException.class, () -> commentService.update(testId, updateDto)),
                    () -> verify(commentRepository, times(0)).save(any())
            );
        }
    }

    @Nested
    class DeleteById {

        @Test
        void checkDeleteByIdVerify() {
            //given
            long testId = COMMENT_ID;

            //when
            doNothing().when(commentRepository).deleteById(testId);
            commentService.deleteById(testId);

            //then
            verify(commentRepository).deleteById(testId);

        }
    }
}
