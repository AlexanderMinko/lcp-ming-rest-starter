package com.lenovo.properties;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.SetUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Getter
@Setter
@ConfigurationProperties("application.web-security-config")
public class WebSecurityConfigProperties {

    private Set<String> csrfIgnoringUrl;
    private Set<String> customUnsecuredEndpoints;
    private Set<String> commonUnsecuredEndpoints;

    public Set<String> getAllUnsecuredEndpoints() {
        return SetUtils.union(customUnsecuredEndpoints, commonUnsecuredEndpoints);
    }
}
