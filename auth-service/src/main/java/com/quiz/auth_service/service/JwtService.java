package com.quiz.auth_service.service;

import com.quiz.auth_service.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtUtil jwtUtil;

    public JwtService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String generateToken(Authentication authentication) {
        return jwtUtil.generateToken(authentication);
    }

    public String generateToken(String username) {
        return jwtUtil.generateToken(username);
    }

    public String getUsernameFromToken(String token) {
        return jwtUtil.getUsernameFromToken(token);
    }

    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    public long getExpirationMs() {
        return jwtUtil.getExpirationMs();
    }
}
