package com.jose.demoia.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server prodServer = new Server();
        prodServer.setUrl("https://demo-ia-production.up.railway.app");
        prodServer.setDescription("Servidor de Producción");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Servidor Local");

        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Actrices")
                        .description("API REST para la gestión de actrices")
                        .version("1.0.0"))
                .servers(List.of(prodServer, localServer));
    }
}
