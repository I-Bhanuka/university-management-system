package com.example.UniversityManagementSystem.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition( // Meta data configuration for the API documentation
        info = @Info(
                title = "University Management System API", // Title of the API
                version = "1.0", // Version of the API
                description = "API documentation for the University Management System" // Description of the API
        )
)
@SecurityScheme(
        name = "bearerAuth", // Name of the security scheme
        type = SecuritySchemeType.HTTP, // Type of the security scheme (HTTP)
        scheme = "bearer", // Scheme used for authentication (Bearer token)
        bearerFormat = "JWT" // Format of the bearer token (JWT)
)
public class SwaggerConfig {
}
