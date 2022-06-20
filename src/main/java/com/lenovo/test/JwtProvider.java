package com.lenovo.test;

import static java.time.temporal.ChronoUnit.HOURS;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.client.ClientHttpRequestInterceptor;

public class JwtProvider {
  public static final String REALM_ACCESS = "realm_access";
  public static final String ROLES_ELEMENT = "roles";
  public static final String EMAIL = "email";
  public static final String PREFERRED_USERNAME = "preferred_username";

  private static final String KID = "KSW2mBCtfsper9b76PWrt9H_4sOScsrGpzAs-etRSgE";
  private static final KeyPair KEY_PAIR = generateKeyPair();

  private final Algorithm algorithm;
  @Getter
  private final JWK jwk;
  private final String tokenInfoUri;
  private final List<ClientHttpRequestInterceptor> interceptors;
  private final JwtAuthorizationExchangeFilterFunction authorizationFilter;

  JwtProvider(
      String tokenInfoUri,
      TestRestTemplate restTemplate,
      JwtAuthorizationExchangeFilterFunction authorizationFilter) {
    this.algorithm = generateAlgorithm();
    this.jwk = generateJwk();
    this.tokenInfoUri = tokenInfoUri;
    this.interceptors = restTemplate.getRestTemplate().getInterceptors();
    this.authorizationFilter = authorizationFilter;
  }

  public String generateToken(InjectJwt jwt) {
    var now = Instant.now();
    var token = JWT.create()
        .withKeyId(KID)
        .withIssuedAt(Date.from(now))
        .withExpiresAt(Date.from(now.plus(1, HOURS)))
        .withSubject(jwt.subject())
        .withIssuer(fromUriString(tokenInfoUri).build(jwt.realm()).toString())
        .withClaim(EMAIL, StringUtils.trimToNull(jwt.email()))
        .withClaim(PREFERRED_USERNAME, jwt.username())
        .withClaim(REALM_ACCESS, Map.of(ROLES_ELEMENT, List.of(jwt.roles())));
    return token.sign(algorithm);
  }

  void applyToken(InjectJwt jwt) {
    var token = BEARER.getValue() + SPACE + generateToken(jwt);
    interceptors.add(new JwtAuthorizationClientHttpRequestInterceptor(token));
    authorizationFilter.setToken(token);
  }

  void resetInterceptorsState() {
    interceptors.removeIf(JwtAuthorizationClientHttpRequestInterceptor.class::isInstance);
    authorizationFilter.setToken(null);
  }

  private Algorithm generateAlgorithm() {
    return Algorithm.RSA256(
        (RSAPublicKey) KEY_PAIR.getPublic(),
        (RSAPrivateKey) KEY_PAIR.getPrivate());
  }

  private JWK generateJwk() {
    return new RSAKey.Builder((RSAPublicKey) KEY_PAIR.getPublic())
        .keyID(KID)
        .algorithm(new com.nimbusds.jose.Algorithm("RS256"))
        .keyUse(KeyUse.SIGNATURE)
        .build();
  }

  @SneakyThrows
  private static KeyPair generateKeyPair() {
    var generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048);
    return generator.generateKeyPair();
  }
}
