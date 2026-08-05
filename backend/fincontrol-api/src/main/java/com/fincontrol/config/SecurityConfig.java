package com.fincontrol.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final FiltroDeSeguranca filtroDeSeguranca;

    public SecurityConfig(FiltroDeSeguranca filtroDeSeguranca) {
        this.filtroDeSeguranca = filtroDeSeguranca;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // 1. Libera chamadas de pre-flight do CORS
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            
            // 2. Libera endpoints públicos de Autenticação
            .requestMatchers("/auth/login", "/auth/register").permitAll()
            
            // 3. Libera as rotas do Swagger/OpenAPI para o Front-end respirar
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            
            // 4. QUALQUER outra requisição (receitas, despesas, dashboard, metas) EXIGE autenticação
            .anyRequest().authenticated()
        )
        .addFilterBefore(filtroDeSeguranca, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
            "http://127.0.0.1:5500",
            "http://localhost:5500"
        ));

        config.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}