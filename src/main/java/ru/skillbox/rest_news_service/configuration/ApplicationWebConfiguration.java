package ru.skillbox.rest_news_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.skillbox.rest_news_service.web.interceptors.AuthorControllerInterceptor;
import ru.skillbox.rest_news_service.web.interceptors.LoggingControllerInterceptor;

@Configuration
public class ApplicationWebConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingControllerInterceptor());
        registry.addInterceptor(clientControllerInterceptor()).addPathPatterns("/api/v1/client/**");
    }

    @Bean
    public LoggingControllerInterceptor loggingControllerInterceptor() {
        return new LoggingControllerInterceptor();
    }

    @Bean
    public AuthorControllerInterceptor clientControllerInterceptor() {
        return new AuthorControllerInterceptor();
    }
}
