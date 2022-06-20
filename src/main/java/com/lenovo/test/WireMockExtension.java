package com.lenovo.test;

import static java.lang.String.format;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.reset;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class WireMockExtension implements BeforeEachCallback, AfterEachCallback, BeforeAllCallback {

  private static final String JWK_SET_URL = "/auth/realms/%s/protocol/openid-connect/certs";
  private static final String JWK_SET_FORMAT = "{\"keys\":[%s]}";

  private JwtProvider jwtProvider;

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    Stream.of(context.getTestMethod(), context.getTestClass())
        .map(Optional::orElseThrow)
        .map(declaration -> declaration.getAnnotation(InjectJwt.class))
        .filter(Objects::nonNull)
        .findFirst()
        .ifPresent(jwt -> {
          stubJwkSetResponse(jwt.realm());
          jwtProvider.applyToken(jwt);
        });
  }

  @Override
  public void afterEach(ExtensionContext context) {
    reset();
    jwtProvider.resetInterceptorsState();
  }

  @Override
  public void beforeAll(ExtensionContext context) {
    jwtProvider = SpringExtension.getApplicationContext(context).getBean(JwtProvider.class);
  }

  private void stubJwkSetResponse(String realm) {
    var jwkSetResponse = format(JWK_SET_FORMAT, jwtProvider.getJwk());
    stubFor(get(urlEqualTo(format(JWK_SET_URL, realm)))).setResponse(
        aResponse()
            .withBody(jwkSetResponse)
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .build());
  }
}
