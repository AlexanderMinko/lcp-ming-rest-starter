package com.lenovo.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

@Configuration
@EnableConfigurationProperties(SwaggerConfigurationProperties.class)
public class SwaggerConfig {

    private static final String OAUTH = "oauth";

    @Bean
    public OpenAPI customOpenAPI(SwaggerConfigurationProperties properties) {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes(OAUTH, new SecurityScheme()
                    .type(SecurityScheme.Type.OAUTH2)
                    .flows(new OAuthFlows().authorizationCode(getOauthFlow(properties)))))
            .info(new Info()
                .title(properties.getApi().getTitle())
                .description(properties.getApi().getDescription())
                .version(properties.getApi().getVersion()));
    }

    @Bean
    public OpenApiCustomiser addAuth() {
        return openApi -> openApi.getPaths().values().stream()
            .map(PathItem::readOperations)
            .flatMap(Collection::stream)
            .forEach(operation -> operation
                .addSecurityItem(new SecurityRequirement().addList(OAUTH)));
    }

    private OAuthFlow getOauthFlow(SwaggerConfigurationProperties properties) {
        var authEndpoint = properties.getOauth().getAuthorizationEndpoint();
        var tokenEndpoint = properties.getOauth().getTokenEndpoint();
        var scopes = new Scopes().addString("openid", "Access Everything");
        return new OAuthFlow().authorizationUrl(authEndpoint).tokenUrl(tokenEndpoint).scopes(scopes);
    }
}
