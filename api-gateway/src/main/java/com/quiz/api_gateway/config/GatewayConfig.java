package com.quiz.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/auth/**", "/api/users/**")
                        .uri("lb://auth-service"))
                .route("vehicle-service", r -> r
                        .path("/api/vehicles/**")
                        .uri("lb://vehicle-service"))
                .route("driver-service", r -> r
                        .path("/api/drivers/**")
                        .uri("lb://driver-service"))
                .route("trip-service", r -> r
                        .path("/api/trips/**")
                        .uri("lb://trip-service"))
                .route("tracking-service", r -> r
                        .path("/api/tracking/**")
                        .uri("lb://tracking-service"))
                .route("tracking-websocket", r -> r
                        .path("/ws/tracking/**")
                        .and()
                        .header("Upgrade", "(?i)websocket")
                        .uri("lb:ws://tracking-service"))
                .route("tracking-websocket-http", r -> r
                        .path("/ws/tracking/**")
                        .uri("lb://tracking-service"))
                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .uri("lb://notification-service"))
                .route("map-service", r -> r
                        .path("/api/map/**")
                        .uri("lb://map-service"))
                .route("file-service", r -> r
                        .path("/files/**")
                        .uri("lb://file-service"))
                .route("emergency-service", r -> r
                        .path("/emergencies/**")
                        .uri("lb://emergency-service"))
                .route("digital-twin-service", r -> r
                        .path("/digitaltwins/**")
                        .uri("lb://digital-twin-service"))
                .route("ai-service", r -> r
                        .path("/predict/**", "/health")
                        .uri("http://localhost:8084"))
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
