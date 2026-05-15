package com.example.hivesampling.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    public Auth auth = new Auth();
    public RateLimit rateLimit = new RateLimit();

    public static class Auth {
        public boolean enabled = true;
        public String username;
        public String password;
        public Jwt jwt = new Jwt();
    }

    public static class Jwt {
        public String secret;
        public long expiration = 3600000;
    }

    public static class RateLimit {
        public boolean enabled = true;
        public int requestsPerMinute = 60;
    }
}
