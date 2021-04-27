package com.evertix.financialwallet.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenAPIConfig {
    @Bean(name = "financialWalletOpenAPI")
    public OpenAPI financialWalletOpenAPI() {
        // http://localhost:8080/swagger-ui.html
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Financial-Wallet API").description("Open API Documentation"));
    }
}
