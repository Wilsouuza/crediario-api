package com.crediario.crediario_api.business.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Crediário API")
                        .version("1.o")
                        .description("API REST para um sistema de crediário")
                        .contact(new Contact()
                                .name("Wilson Palma Souza")
                                .url("https://github.com/Wilsouuza")));
    }
}
