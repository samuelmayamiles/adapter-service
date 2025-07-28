package com.smm.adapterservice.config.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "config.environment", name = "swagger-uri")
public class OpenApiConfig {

    @Value("${config.environment.swagger-uri}")
    private String serverUri;

    @Bean
    public OpenAPI openApi() {

        return new OpenAPI()
                .addServersItem(buildServer())
                .info(buildInfo());
    }

    private Server buildServer() {

        final Server server = new Server();
        server.setUrl(serverUri);
        return server;
    }

    private Info buildInfo() {

        return new Info()
                .title("Adapter Service API")
                .description("Adapter Service API");
    }
}
