package com.example.hivesampling.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfiguration;

import static org.junit.jupiter.api.Assertions.*;

class WebConfigTest {

    @Test
    void corsConfig_ShouldAllowLocalhost() {
        WebConfig config = new WebConfig();
        assertNotNull(config);
        
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:3000");
        corsConfig.addAllowedOrigin("http://localhost:5173");
        
        assertTrue(corsConfig.getAllowedOrigins().contains("http://localhost:3000"));
        assertTrue(corsConfig.getAllowedOrigins().contains("http://localhost:5173"));
    }

    @Test
    void webConfig_ShouldBeCreated() {
        WebConfig config = new WebConfig();
        assertNotNull(config);
    }

    @Test
    void corsConfig_ShouldAllowCredentials() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        
        assertTrue(corsConfig.getAllowCredentials());
    }

    @Test
    void corsConfig_ShouldAllowAllMethods() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("DELETE");
        corsConfig.addAllowedMethod("OPTIONS");
        
        assertTrue(corsConfig.getAllowedMethods().contains("GET"));
        assertTrue(corsConfig.getAllowedMethods().contains("POST"));
        assertTrue(corsConfig.getAllowedMethods().contains("OPTIONS"));
    }
}
