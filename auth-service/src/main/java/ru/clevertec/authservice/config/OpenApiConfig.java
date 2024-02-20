package ru.clevertec.authservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                description = "user-service",
                title = "News-management-api Documentation"),
        servers = @Server(
                description = "Local",
                url = "http://localhost:9000"
        ))
public class OpenApiConfig {
}
