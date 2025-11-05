package com.example.studyup.config; // Config package


import org.springframework.context.annotation.Bean; // Expose beans
import org.springframework.context.annotation.Configuration; // Config class
import org.springframework.web.servlet.config.annotation.CorsRegistry; // CORS settings
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // MVC hook


@Configuration // Tell Spring this is a config class
public class CorsConfig {
    @Bean // Expose a WebMvcConfigurer bean to customize MVC
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) { // Configure CORS
                registry.addMapping("/**") // Apply to all paths
                        .allowedOrigins("http://localhost:3000") // Allow React dev server
                        .allowedMethods("GET","POST","PUT","DELETE","OPTIONS") // Common methods
                        .allowedHeaders("*") // Allow any headers
                        .allowCredentials(true); // Allow cookies/credentials
            }
        };
    }
}