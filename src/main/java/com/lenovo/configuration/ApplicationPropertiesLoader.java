package com.lenovo.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Slf4j
public class ApplicationPropertiesLoader implements EnvironmentPostProcessor {

    private final YamlPropertySourceLoader propertySourceLoader = new YamlPropertySourceLoader();

    private static final String DEFAULT_AUTH_PROPERTIES = "defaultApplicationProperties";
    private static final String DEFAULT_AUTH_FILE = "app.yaml";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        var myFrameworkDefaults = new ClassPathResource(DEFAULT_AUTH_FILE);
        load(environment, myFrameworkDefaults);
    }

    private void load(ConfigurableEnvironment environment, Resource resource) {
        try {
            propertySourceLoader.load(DEFAULT_AUTH_PROPERTIES, resource)
                .forEach(propertySource -> environment.getPropertySources().addLast(propertySource));
        } catch (IOException e) {
            log.warn("Can't load auth properties from app.yaml file");
        }
    }
}
