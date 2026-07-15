package com.labestages.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Swagger / OpenAPI.
 * Interface accessible sur : http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI labeStagesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LabéStages API")
                        .description("API REST de gestion des étudiants, encadreurs et thèmes de projets de fin de cycle — Université de Labé")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Département Informatique — Université de Labé")
                        )
                );
    }
}
