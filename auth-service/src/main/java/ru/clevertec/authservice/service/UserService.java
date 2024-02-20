package ru.clevertec.authservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.authservice.dto.request.UserRequestDto;
import ru.clevertec.authservice.dto.response.UserPageResponseDto;
import ru.clevertec.authservice.dto.response.UserResponseDto;
import ru.clevertec.authservice.model.User;
import ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException;

public interface UserService {

    /**
     * @param dto объект, содержащий данные для создания пользователя
     * @return созданный объект с информацией о пользователе {@link User}
     */
    UserResponseDto create(UserRequestDto dto);

    /**
     * @param username имя пользователя для поиска объекта пользователя
     * @return объект с информацией о найденном пользователе
     * @throws EntityNotFoundException если объект не найден
     */
    UserResponseDto findByUsername(String username) throws EntityNotFoundException;

    /**
     * Метод постраничного поиска объектов списка пользователей
     *
     * @param pageable объект для разбивки информации на страницы
     * @return объект с информацией о странице и списком пользователей
     */
    UserPageResponseDto findAll(Pageable pageable);

    /**
     * @param id идентификатор для удаления объекта {@link User}
     */
    void deleteById(Long id);
}
