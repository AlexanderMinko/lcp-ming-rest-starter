package com.lenovo.test;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

@AllArgsConstructor
public class JwtAuthorizationClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

  private String token;

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] bytes, ClientHttpRequestExecution execution)
      throws IOException {
    request.getHeaders().add(AUTHORIZATION, token);
    return execution.execute(request, bytes);
  }
}
