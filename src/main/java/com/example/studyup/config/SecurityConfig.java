package com.example.studyup.config; // Security package


import org.springframework.context.annotation.Bean; // Define beans
import org.springframework.context.annotation.Configuration; // Config class
import org.springframework.security.config.Customizer; // For httpBasic defaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Security builder
import org.springframework.security.config.http.SessionCreationPolicy; // Stateless sessions
import org.springframework.security.web.SecurityFilterChain; // Main chain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Insert filter


@Configuration // Security configuration class
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter; // Our custom JWT filter (stub)
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) { this.jwtAuthFilter = jwtAuthFilter; }


    @Bean // Define the security filter chain bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF (we use tokens for APIs)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No server sessions
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/hello").permitAll() // Public endpoints
                        .requestMatchers("/api/chat").authenticated() // Chat requires auth
                        .anyRequest().permitAll() // Everything else allowed for now
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Insert JWT filter
                .httpBasic(Customizer.withDefaults()); // Keep basic defaults (not used)


        return http.build(); // Build the chain
    }
}