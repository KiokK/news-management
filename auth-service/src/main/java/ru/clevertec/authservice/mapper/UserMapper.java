package ru.clevertec.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Pageable;
import ru.clevertec.authservice.dto.request.UserRequestDto;
import ru.clevertec.authservice.dto.response.UserPageResponseDto;
import ru.clevertec.authservice.dto.response.UserResponseDto;
import ru.clevertec.authservice.model.User;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponseDto toUserResponseDto(User user);

    User toUser(UserRequestDto user);

    @Mapping(target = "users", source = "users")
    UserPageResponseDto toUserPageResponseDto(List<User> users, Pageable pageable);
}
