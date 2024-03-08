//package ru.clevertec.news.service;
//
//import com.github.tomakehurst.wiremock.junit5.WireMockTest;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestConstructor;
//import ru.clevertec.news.dto.response.CommentAuthorResponseDto;
//import ru.clevertec.news.dto.response.UserManagementDto;
//
//import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson;
//import static com.github.tomakehurst.wiremock.client.WireMock.containing;
//import static com.github.tomakehurst.wiremock.client.WireMock.get;
//import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@RequiredArgsConstructor
//@WireMockTest(httpPort = 9000)
////@AutoConfigureMockMvc(addFilters = false)
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
//public class CommentServiceWithUserClientTest {
//
//    private final CommentService commentService;
//
//    @Test
//    @SneakyThrows
//    void checkFindCommentWithAuthorByIdWithMockingUserClient() {
//        //given
//        long commentId = 7L;
//        String username = "kiok";
//        String email = "kiok@mail.ru";
//        UserManagementDto userManagementDto = new UserManagementDto(1L, username, email);
//
//        //when
//        stubFor(get("/api/v1/users/"+username)
//                .willReturn(okForJson(userManagementDto)));
//        CommentAuthorResponseDto actual = commentService.findCommentWithAuthorById(commentId);
//
//        //then
//        assertAll(
//                () -> assertEquals(commentId, actual.commentId()),
//                () -> assertEquals(username, actual.username()),
//                () -> assertEquals(email, actual.email())
//        );
//    }
//}
