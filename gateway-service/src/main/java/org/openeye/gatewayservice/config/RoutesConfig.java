package org.openeye.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", route -> route
                        .path("/api/users", "/api/users/**", "/api/roles", "/api/roles/**", "/api/user-roles", "/api/user-roles/**")
                        .uri("lb://auth-service"))
                .route("student-service", route -> route
                        .path("/api/students", "/api/students/**")
                        .uri("lb://student-service"))
                .route("teacher-service", route -> route
                        .path("/api/teachers", "/api/teachers/**")
                        .uri("lb://teacher-service"))
                .route("departement-service", route -> route
                        .path("/api/departements", "/api/departements/**")
                        .uri("lb://departement-service"))
                .route("course-service", route -> route
                        .path("/api/courses", "/api/courses/**")
                        .uri("lb://course-service"))
                .route("section-service", route -> route
                        .path("/api/sections", "/api/sections/**")
                        .uri("lb://section-service"))
                .route("inscription-service", route -> route
                        .path("/api/inscriptions", "/api/inscriptions/**")
                        .uri("lb://inscription-service"))
                .build();
    }
}
