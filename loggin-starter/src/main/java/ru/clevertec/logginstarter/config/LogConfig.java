package ru.clevertec.logginstarter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.logginstarter.aspect.CustomLogAspect;

@Configuration
@ConditionalOnProperty(value = "logging.status", havingValue = "enabled")
public class LogConfig {

    @Bean
    @ConditionalOnMissingBean
    public CustomLogAspect customLogAspect() {
        return new CustomLogAspect();
    }
}
