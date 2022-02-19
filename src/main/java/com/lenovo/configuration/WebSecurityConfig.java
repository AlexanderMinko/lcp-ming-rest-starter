package com.lenovo.configuration;

import com.lenovo.properties.WebSecurityConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
@Import({BeanConfig.class})
@EnableConfigurationProperties(WebSecurityConfigProperties.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final WebSecurityConfigProperties webSecurityConfigProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        var unsecuredEndpoints = webSecurityConfigProperties.getAllUnsecuredEndpoints().toArray(String[]::new);
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .csrf()
            .disable()
            .cors()
            .and()
            .authorizeRequests()
            .antMatchers(unsecuredEndpoints).permitAll()
            .anyRequest().authenticated()
            .and()
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthenticationConverter);
    }
}
