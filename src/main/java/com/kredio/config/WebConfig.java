// src/main/java/com/bdcopre/config/WebConfig.java
package com.kredio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // o "/**" si tienes más rutas
                .allowedOrigins(
                        "http://localhost:53248",          // dev Flutter/React
                        "http://localhost:4200",           // si usas Angular
                        "https://tu-dominio.com",          // producción web
                        "https://app.tu-dominio.com"       // producción móvil/web
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")                   // o lista específica: "Authorization", "Content-Type"
                .allowCredentials(true)                // si usas cookies o auth con credenciales (opcional para JWT puro)
                .maxAge(3600);                         // cache preflight 1 hora
    }
}