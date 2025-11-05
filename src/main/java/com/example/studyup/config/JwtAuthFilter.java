package com.example.studyup.config; // Config package


import jakarta.servlet.FilterChain; // For filter chaining
import jakarta.servlet.ServletException; // Servlet exception
import jakarta.servlet.http.HttpServletRequest; // Request
import jakarta.servlet.http.HttpServletResponse; // Response
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Auth token
import org.springframework.security.core.context.SecurityContextHolder; // Security context
import org.springframework.security.core.userdetails.User; // Simple user principal
import org.springframework.stereotype.Component; // Spring bean
import org.springframework.web.filter.OncePerRequestFilter; // Run once per request
import java.io.IOException; // IO exceptions
import java.util.List; // Collections


@Component // Register filter as a bean
public class JwtAuthFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String auth = request.getHeader("Authorization"); // Read header
        if (auth != null && auth.startsWith("Bearer ")) { // Check Bearer prefix
            String token = auth.substring(7); // Extract token
// TODO: Validate real JWT (signature, expiration, etc.)
            if (!token.isBlank()) { // For beginner demo: accept any non-empty token
                var principal = new User("user", "", List.of()); // Create simple user
                var authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication); // Set auth in context
            }
        }
        chain.doFilter(request, response); // Continue pipeline
    }
}