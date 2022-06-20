package com.lenovo.test;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

final class JwtAuthorizationExchangeFilterFunction implements ExchangeFilterFunction {

  @Getter
  @Setter
  private String token;

  @Override
  public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
    var processedRequest = Optional.ofNullable(token)
        .map(jwt -> ClientRequest.from(request)
            .headers(headers -> headers.add(AUTHORIZATION, jwt))
            .build())
        .orElse(request);

    return next.exchange(processedRequest);
  }
}
