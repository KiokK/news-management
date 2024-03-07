package ru.clevertec.authservice.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.clevertec.authservice.dto.request.UserRequestDto;
import ru.clevertec.authservice.dto.response.UserPageResponseDto;
import ru.clevertec.authservice.dto.response.UserResponseDto;
import ru.clevertec.authservice.model.User;
import ru.clevertec.authservice.reposiroty.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.authservice.util.Constants.NO_EXIST_USERNAME;
import static ru.clevertec.authservice.util.Constants.PAGE_NUMBER_0;
import static ru.clevertec.authservice.util.Constants.PAGE_SIZE_2;
import static ru.clevertec.authservice.util.UserRequestDtoTestData.getUserRequestDto;
import static ru.clevertec.authservice.util.UserResponseDtoTestData.getListUserResponseDto;
import static ru.clevertec.authservice.util.UserResponseDtoTestData.getUserResponseDto;
import static ru.clevertec.authservice.util.UserTestData.USERNAME;
import static ru.clevertec.authservice.util.UserTestData.USER_ID;
import static ru.clevertec.authservice.util.UserTestData.getUserBuilder;
import static ru.clevertec.authservice.util.UserTestData.getUserList;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Nested
    class Create {

        @Test
        void checkCreateShouldReturnCreatedDto() {
            //given
            UserRequestDto requestDtoTestData = getUserRequestDto();
            User createdUser = getUserBuilder().build();
            User newUser = getUserBuilder()
                    .id(null)
                    .createdAt(null)
                    .modifiedAt(null).build();
            UserResponseDto expected = getUserResponseDto();

            //when
            when(userRepository.save(newUser)).thenReturn(createdUser);
            UserResponseDto actual = userService.create(requestDtoTestData);

            //then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class FindByUsername {

        @Test
        void checkFindByUsernameShouldReturnUserDto() {
            //given
            String findUsername = USERNAME;
            User foundUser = getUserBuilder().build();
            UserResponseDto expected = getUserResponseDto();

            //when
            when(userRepository.findByUsername(findUsername)).thenReturn(Optional.of(foundUser));
            UserResponseDto actual = userService.findByUsername(findUsername);

            //then
            assertEquals(expected, actual);
        }

        @Test
        void checkFindByUsernameShouldThrowUsernameNotFoundException() {
            //when
            when(userRepository.findByUsername(NO_EXIST_USERNAME)).thenReturn(Optional.empty());

            //then
            UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                    () -> userService.findByUsername(NO_EXIST_USERNAME));
            assertEquals(NO_EXIST_USERNAME, exception.getMessage());
        }
    }

    @Nested
    class FindAll {

        @Test
        void checkFindAllShouldReturnPageResponseDto() {
            //given
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_2);
            List<User> foundUsers = getUserList();
            List<UserResponseDto> expectedUsers = getListUserResponseDto();

            //when
            when(userRepository.findAll(pageable)).thenReturn(new PageImpl(foundUsers));
            UserPageResponseDto actual = userService.findAll(pageable);

            //list
            assertAll(
                    () -> assertEquals(expectedUsers, actual.users()),
                    () -> assertEquals(PAGE_NUMBER_0, actual.pageNumber()),
                    () -> assertEquals(PAGE_SIZE_2, actual.pageSize())
            );
        }
    }

    @Nested
    class DeleteById {

        @Test
        void checkDeleteById() {
            //given
            Long deleteId = USER_ID;

            //when
            doNothing().when(userRepository)
                    .deleteById(deleteId);
            userService.deleteById(deleteId);

            //then
            verify(userRepository).deleteById(deleteId);
        }
    }
}
