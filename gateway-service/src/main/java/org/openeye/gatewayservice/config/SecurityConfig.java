package org.openeye.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.cors(Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers("/actuator/health").permitAll()
                        .pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .pathMatchers("/api/users/**", "/api/roles/**", "/api/user-roles/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/students/**")
                        .hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        .pathMatchers("/api/students/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/teachers/**")
                        .hasAnyRole("ADMIN", "TEACHER")
                        .pathMatchers("/api/teachers/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/departements/**", "/api/courses/**", "/api/sections/**")
                        .hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        .pathMatchers(HttpMethod.GET, "/api/inscriptions/**")
                        .hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        .pathMatchers(HttpMethod.PATCH, "/api/inscriptions/**")
                        .hasAnyRole("ADMIN", "TEACHER")
                        .pathMatchers("/api/departements/**", "/api/courses/**", "/api/sections/**", "/api/inscriptions/**")
                        .hasRole("ADMIN")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Location"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        JwtAuthenticationConverter delegate = new JwtAuthenticationConverter();
        delegate.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(delegate);
    }
}
