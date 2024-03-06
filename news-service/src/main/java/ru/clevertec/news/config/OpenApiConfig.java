package ru.clevertec.news.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                description = "The project is a task",
                title = "News-management-api Documentation",
                termsOfService = "Term of service"
        ),
        servers = @Server(
                description = "Local",
                url = "http://localhost:8082"
        )
)
public class OpenApiConfig {

}
