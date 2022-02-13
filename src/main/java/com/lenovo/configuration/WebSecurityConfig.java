package com.lenovo.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
@Import({BeanConfig.class})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests((requests) -> requests
                .anyRequest()
                .permitAll())
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .oauth2Client()
            .and()
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthenticationConverter);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v3/api-docs",
            "/configuration/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/",
            "/webjars/**",
            "/api-docs/**");
    }
}
