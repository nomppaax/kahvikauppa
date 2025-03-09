package com.example.kahvikauppa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class securityConfiguration implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Sisäänkirjautumisen rakentamisessa käytetty ohjeita:
        // https://spring.io/guides/gs/securing-web
        // Login-sivulle kontrolleri
        registry.addViewController("/login").setViewName("login");
    }
}