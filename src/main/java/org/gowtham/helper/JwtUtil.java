package org.gowtham.helper;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.gowtham.model.User;

public class JwtUtil {
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 hours

    // ✅ Use a properly sized 256-bit key (Ensure it's at least 256 bits)
    private static final String SECRET_KEY = "yV34Hf8Pq0LgTkW1YnA5Xz7m9vR2BpJ6yV34Hf8Pq0LgTkW1YnA5Xz7m9vR2BpJ6";
    private static final SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));

    // ✅ Generate JWT Token
    public static String generateToken(User user) {
        long now = System.currentTimeMillis();
        System.out.println("🟢 [DEBUG] Generating Token for userId: " + user.getId() + " and role: " + user.getRole());

        // ✅ Use HashMap for Java 8 compatibility
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", String.valueOf(user.getId())); // User ID as subject
        claims.put("email", user.getEmail()); // Email claim
        claims.put("role", user.getRole()); // Role claim
        claims.put("iat", new Date(now)); // Issued at
        claims.put("exp", new Date(now + EXPIRATION_TIME)); // Expiry time

        return Jwts.builder()
                .claims(claims) // ✅ Set all claims
                .signWith(key, Jwts.SIG.HS256) // ✅ Use HS256 algorithm
                .compact();
    }

    // ✅ Validate JWT Token
    public static Jws<Claims> validateToken(String token) {
        return Jwts.parser()
                .verifyWith(key) // ✅ New method in JJWT 0.12.6
                .build()
                .parseSignedClaims(token);
    }

    // ✅ Extract User Role
    public static String getUserRole(String token) {
        return validateToken(token).getPayload().get("role", String.class);
    }

    // ✅ Extract User ID
    public static int getUserId(String token) {
        return Integer.parseInt(validateToken(token).getPayload().getSubject());
    }
}
