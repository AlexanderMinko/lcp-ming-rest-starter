package com.lenovo.service;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

public class LcpJwtService {

    public Optional<Jwt> getJwt() {
        return Optional.of(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .filter(JwtAuthenticationToken.class::isInstance)
            .map(JwtAuthenticationToken.class::cast)
            .map(JwtAuthenticationToken::getToken);
    }

    public String getEmail() {
        return getJwt().map(jwt -> jwt.getClaimAsString("email")).orElse(null);
    }

    public String getUserId() {
        return getJwt().map(jwt -> jwt.getClaimAsString("sub")).orElse(null);
    }

}
