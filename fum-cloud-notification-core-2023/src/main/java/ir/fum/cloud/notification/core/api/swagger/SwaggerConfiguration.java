package ir.fum.cloud.notification.core.api.swagger;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import ir.fum.cloud.notification.core.api.config.NotificationOperationCustomizer;
import ir.fum.cloud.notification.core.configuration.DocsConfiguration;
import ir.fum.cloud.notification.core.configuration.ProjectConfiguration;
import ir.fum.cloud.notification.core.domain.model.helper.NotificationHeader;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * Configuration class for swagger
 */

@Configuration
@RequiredArgsConstructor
public class SwaggerConfiguration {

    private final DocsConfiguration docsConfiguration;
    private final ProjectConfiguration projectConfiguration;

    @Bean
    public GroupedOpenApi publicApi() {

        return GroupedOpenApi.builder()
                .group("general")
                .pathsToExclude(getPrivatePaths())
                .addOpenApiCustomiser(publicApiCustomizer())
                .addOperationCustomizer(new NotificationOperationCustomizer())
                .build();
    }

    @Bean
    public GroupedOpenApi api() {

        return GroupedOpenApi.builder()
                .group("private")
                .pathsToMatch(getPrivatePaths())
                .addOpenApiCustomiser(privateApiCustomizer())
                .addOperationCustomizer(new NotificationOperationCustomizer())
                .build();
    }

    /**
     * Define an API group that'll include specific version.
     * Can be helpful in versioning the APIs.
     */
//    @Bean
//    public GroupedOpenApi hideApis() {
//        return GroupedOpenApi.builder().group("general")
//                .pathsToExclude("/v3/**")
//                .pathsToMatch("/v1/**", "/v2/**")
//                .build();
//    }
    @Bean
    public OpenApiCustomiser publicApiCustomizer() {

        return openApi -> openApi
                .addSecurityItem(new SecurityRequirement()
                        .addList(NotificationHeader.NOTIFICATION_TOKEN)
                )
                .servers(getDocsServers())
                .getComponents()
                .addSecuritySchemes(NotificationHeader.NOTIFICATION_TOKEN, new SecurityScheme()
                        .in(SecurityScheme.In.HEADER)
                        .type(SecurityScheme.Type.APIKEY)
                        .name(NotificationHeader.NOTIFICATION_TOKEN))
                ;
    }

    @Bean
    public OpenApiCustomiser privateApiCustomizer() {

        return openApi -> openApi
                .addSecurityItem(new SecurityRequirement()
                        .addList(NotificationHeader.NOTIFICATION_TOKEN)
                )
                .servers(getDocsServers())
                .getComponents()
                .addSecuritySchemes(NotificationHeader.NOTIFICATION_TOKEN, new SecurityScheme()
                        .in(SecurityScheme.In.HEADER)
                        .type(SecurityScheme.Type.APIKEY)
                        .name(NotificationHeader.NOTIFICATION_TOKEN))
                ;
    }

    private @NotNull List<Server> getDocsServers() {
        return Collections.singletonList(
                new Server()
                        .url(projectConfiguration.getProjectUrlConfig())
                        .description(" [" + docsConfiguration.getSwaggerTitle() + "]")
        );
    }

    private @NotNull String[] getPrivatePaths() {
        return docsConfiguration.getSwaggerPrivatePaths().split(",");
    }

}
