package com.smm.adapterservice.config.documentation;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public GroupedOpenApi apiV1() {
        return buildVersionDocket(1);
    }

    private GroupedOpenApi buildVersionDocket(final Integer versionId) {

        final String apiVersion = String.format("adapter-service.api.v%s", versionId);
        final String producesVersion = String.format("application/%s+json", apiVersion);

        return GroupedOpenApi.builder()
                .packagesToExclude("org.springframework.boot")
                .pathsToMatch("/**")
                .group(apiVersion)
                .producesToMatch(producesVersion)
                .addOpenApiCustomiser(buildApiInfo(String.valueOf(versionId)))
                .build();
    }

    private OpenApiCustomiser buildApiInfo(final String version) {

        return openApi -> openApi.setInfo(buildVersionInfo(version));
    }

    private Info buildVersionInfo(final String version) {

        return new Info()
                .title("Adapter Service API")
                .description("Adapter Service API v" + version)
                .version(version);
    }
}