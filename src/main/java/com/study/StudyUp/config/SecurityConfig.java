package com.study.StudyUp.config;

import com.study.StudyUp.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // ✅ Inject your CustomUserDetailsService
    @Autowired
    private CustomUserDetailsService userDetailsService;

    // ✅ Define security rules for your app
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (for API testing; enable in production)
                .csrf(AbstractHttpConfigurer::disable)

                // Define which endpoints are public and which are protected
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",   // public endpoints (login/register)
                                "/h2-console/**",
                                "/",
                                "/register",
                                "/register-success"
                        ).permitAll()
                        .anyRequest().authenticated() // all others need authentication
                )

                // Allow H2 console in iframe
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // Disable form login
                .formLogin(form -> form.disable())

                // Enable Basic Auth for testing
                .httpBasic();

        return http.build();
    }

    // ✅ Connect UserDetailsService with PasswordEncoder
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    // ✅ AuthenticationManager for authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // ✅ BCrypt encoder for secure password hashing
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
