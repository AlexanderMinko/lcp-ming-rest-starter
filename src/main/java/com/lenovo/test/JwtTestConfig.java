package com.lenovo.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.reactive.server.WebTestClientBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.util.UriComponentsBuilder;

public class JwtTestConfig {

  @Bean
  JwtAuthorizationExchangeFilterFunction jwtAuthorizationExchangeFilterFunction() {
    return new JwtAuthorizationExchangeFilterFunction();
  }

  @Bean
  WebTestClientBuilderCustomizer authorizationFilterWebTestClientBuilderCustomizer(
      JwtAuthorizationExchangeFilterFunction filter) {
    return builder -> builder.filter(filter).build();
  }

  @Bean
  JwtProvider jwtTest(
      @Value("${security.oauth2.providers.keycloak.base-uri}") String keycloakBaseUri,
      @Value("${security.oauth2.providers.keycloak.issuer-path}") String issuerPath,
      TestRestTemplate rest,
      JwtAuthorizationExchangeFilterFunction authorizationFilter) {
    return new JwtProvider(UriComponentsBuilder.fromUriString(keycloakBaseUri).path(issuerPath).build().toString(),
        rest, authorizationFilter);
  }
}
