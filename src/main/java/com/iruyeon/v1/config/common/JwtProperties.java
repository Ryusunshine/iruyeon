package com.iruyeon.v1.config.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// JwtProperties.java
@Component
@ConfigurationProperties("jwt")
@Getter
@Setter
public class JwtProperties {
    private String issuer;
    private String secretKey;
    private TokenProperties token;


    @Setter
    @Getter
    public static class TokenProperties {
        private CookieNames cookieNames;
        private Durations durations;
        private Authentication authentication;
    }

    @Setter
    @Getter
    public static class CookieNames {
        private String refresh;
        private String access;
    }

    @Setter
    @Getter
    public static class Durations {
        private long access;
        private long refresh;
    }

    @Setter
    @Getter
    public static class Authentication {
        private String header;
        private String prefix;
    }
}