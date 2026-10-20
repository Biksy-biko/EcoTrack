package com.example.ecotrack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Libera todas as rotas da API
                .allowedOrigins("http://127.0.0.1:5500", "http://localhost:5500", "http://localhost:3000") // Permite o front-end local
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Métodos permitidos
                .allowedHeaders("*") // Permite todos os headers (como Content-Type, Authorization, etc)
                .allowCredentials(true); // Permite envio de cookies/credenciais se necessário
    }
}
