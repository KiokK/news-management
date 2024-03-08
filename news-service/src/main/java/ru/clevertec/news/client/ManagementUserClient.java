package ru.clevertec.news.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.clevertec.news.dto.response.UserManagementDto;

@Component
@FeignClient(value = "auth-service", url = "http://localhost:9000/api/v1/users")
public interface ManagementUserClient {

    @GetMapping("/{username}")
    ResponseEntity<UserManagementDto> getUserByUsername(@PathVariable String username,
                                                        @RequestHeader(value = "Authorization") String authorizationHeader);

}
