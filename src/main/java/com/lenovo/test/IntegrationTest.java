package com.lenovo.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "mongock.enabled=false",
        "wiremock.server.host=http://localhost:${wiremock.server.port}",
        "KEYCLOAK_HOST=${wiremock.server.host}",
    })
@ExtendWith(WireMockExtension.class)
@AutoConfigureWireMock(port = 0)
@Import(JwtTestConfig.class)
@Inherited
public @interface IntegrationTest {
}
