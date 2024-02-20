package ru.clevertec.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.authservice.dto.request.UserRequestDto;
import ru.clevertec.authservice.dto.response.UserPageResponseDto;
import ru.clevertec.authservice.dto.response.UserResponseDto;
import ru.clevertec.authservice.mapper.UserMapper;
import ru.clevertec.authservice.model.User;
import ru.clevertec.authservice.reposiroty.UserRepository;
import ru.clevertec.authservice.service.UserService;
import ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    @Transactional
    public UserResponseDto create(UserRequestDto dto) {
        return Optional.of(dto)
                .map(userMapper::toUser)
                .map(userRepository::save)
                .map(userMapper::toUserResponseDto)
                .orElseThrow();
    }

    @Override
    public UserResponseDto findByUsername(String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username)
                .map(userMapper::toUserResponseDto)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public UserPageResponseDto findAll(Pageable pageable) {
        List<User> users =  userRepository.findAll(pageable).toList();

        return userMapper.toUserPageResponseDto(users, pageable);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
