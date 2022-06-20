package com.lenovo.configuration;

import com.lenovo.model.events.Event;
import com.lenovo.service.EventProducerService;
import com.lenovo.service.LcpJwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.web.reactive.function.client.WebClient;

public class BeanConfig {

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    WebClient webClient(
        ClientRegistrationRepository clientRegistrationRepository,
        OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository) {
        var oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
            clientRegistrationRepository, oAuth2AuthorizedClientRepository);
        oauth2.setDefaultClientRegistrationId("keycloak");
        return WebClient.builder()
            .apply(oauth2.oauth2Configuration())
            .build();
    }

    @Bean
    WebConfig webConfig() {
        return new WebConfig();
    }

    @Bean
    LcpJwtService lcpJwtService() {
        return new LcpJwtService();
    }

    @Bean
    EventProducerService eventProducerService(KafkaTemplate<String, Event> kafkaTemplate) {
        return new EventProducerService(kafkaTemplate);
    }
}
