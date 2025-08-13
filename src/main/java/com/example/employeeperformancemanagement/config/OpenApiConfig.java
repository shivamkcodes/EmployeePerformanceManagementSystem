package com.example.employeeperformancemanagement.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI employeePerformanceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Employee Performance Management API")
                        .description("APIs for managing employees, departments, projects, and performance reviews")
                        .version("v1.0.0")
                        .contact(new Contact().name("Your Team").email("team@example.com"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Repository")
                        .url("https://example.com/repo"));
    }
}


