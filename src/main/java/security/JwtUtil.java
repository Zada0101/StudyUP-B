package security;

import io.jsonwebtoken.Jwts;                    // For building and parsing JWTs
import io.jsonwebtoken.SignatureAlgorithm;      // Algorithm used to sign JWTs
import io.jsonwebtoken.security.Keys;           // Helps generate secure keys
import org.springframework.stereotype.Component; // Marks this as a Spring-managed bean

import java.security.Key;                       // Represents our secret key
import java.util.Date;                          // For timestamps

@Component // Makes this class available for dependency injection
public class JwtUtil {

    // Generate a random secret key for signing tokens (in real projects, store in config)
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Generate a JWT token for a given username
    public String generateToken(String username) {
        // Build and sign the token
        return Jwts.builder()
                .setSubject(username)                                   // Store username inside token
                .setIssuedAt(new Date(System.currentTimeMillis()))      // Set token creation time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Expire in 1 hour
                .signWith(secretKey)                                    // Sign with our secret key
                .compact();                                             // Finish and return as String
    }

    // Extract the username (subject) from the token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // The “subject” field contains the username
    }

    // Validate the token (check username and expiration)
    public boolean validateToken(String token, String username) {
        String extracted = extractUsername(token);
        return (extracted.equals(username) && !isTokenExpired(token));
    }

    // Helper to check if token expired
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
