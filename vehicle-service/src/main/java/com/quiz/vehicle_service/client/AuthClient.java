package com.quiz.vehicle_service.client;

import com.quiz.vehicle_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service")
public interface AuthClient {

    @GetMapping("/api/users/me")
    UserResponse getCurrentUser(@RequestHeader("Authorization") String token);

    @GetMapping("/api/users/{username}")
    UserResponse getUserByUsername(@RequestHeader("Authorization") String token, @PathVariable("username") String username);
}
