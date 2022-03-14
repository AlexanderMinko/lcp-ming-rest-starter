package com.lenovo.configuration;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Validated
@Getter
@ConfigurationProperties(prefix = "swagger")
public class SwaggerConfigurationProperties {
    @Valid
    @NestedConfigurationProperty
    private final ApiInfo api = new ApiInfo();
    @Valid
    @NestedConfigurationProperty
    private final Oauth oauth = new Oauth();

    @Getter
    @Setter
    public static class ApiInfo {

        @NotBlank
        private String title;

        @NotBlank
        private String description;

        @NotBlank
        private String version;
    }

    @Getter
    @Setter
    public static class Oauth {

        @NotBlank
        @URL
        private String authorizationEndpoint;

        @NotBlank
        @URL
        private String tokenEndpoint;
    }

}
