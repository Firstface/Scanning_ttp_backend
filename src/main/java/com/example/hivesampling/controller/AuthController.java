package com.example.hivesampling.controller;

import com.example.hivesampling.config.AppProperties;
import com.example.hivesampling.dto.LoginRequest;
import com.example.hivesampling.dto.LoginResponse;
import com.example.hivesampling.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AppProperties appProperties;
    private final JwtUtil jwtUtil;

    public AuthController(AppProperties appProperties, JwtUtil jwtUtil) {
        this.appProperties = appProperties;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (appProperties.auth.username.equals(request.username) &&
            appProperties.auth.password.equals(request.password)) {
            String token = jwtUtil.generateToken(request.username);
            return ResponseEntity.ok(new LoginResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid username or password"));
    }
}
