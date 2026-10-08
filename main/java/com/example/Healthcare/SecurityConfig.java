
package com.example.Healthcare;

import com.example.Healthcare.Service.JwtFilter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
            List.of("http://localhost:5173")
        );

        configuration.setAllowedMethods(
            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(
            List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .cors(cors -> {})

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .exceptionHandling(exception ->
                exception
                    .authenticationEntryPoint(
                        (request, response, authException) ->
                            response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                "Unauthorized"
                            )
                    )
                    .accessDeniedHandler(
                        (request, response, accessDeniedException) ->
                            response.sendError(
                                HttpServletResponse.SC_FORBIDDEN,
                                "Forbidden"
                            )
                    )
            )

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                    "/",
                    "/api/users/register",
                    "/api/users/login",
                    "/api/doctors",
                    "/error"
                )
                .permitAll()

                .requestMatchers(
                    "/api/patients/**"
                )
                .hasRole("PATIENT")

                .requestMatchers(
                    "/api/doctors/**"
                )
                .hasRole("DOCTOR")

                .requestMatchers(
                    "/api/appointments/doctor/**"
                )
                .hasRole("DOCTOR")

                .requestMatchers(
                    "/api/appointments/*/confirm",
                    "/api/appointments/*/complete"
                )
                .hasRole("DOCTOR")

                .requestMatchers(
                    "/api/appointments/*/cancel"
                )
                .authenticated()

                .requestMatchers(
                    "/api/appointments/**",
                    "/api/availability/**"
                )
                .authenticated()

                .requestMatchers(
                    "/api/consultations/**"
                )
                .authenticated()

                .anyRequest()
                .authenticated()
            )

            .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}

