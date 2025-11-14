package Birger.SMS.security;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {

    private static final String SECRET = "MaCleSecreteSuperLonguePourJWT1234567890"; // change selon toi
    private static final long EXPIRATION = 1000 * 60 * 60; // 1h
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Génère un token avec userId et username
    public static String generateToken(Long userId, String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)       // ID utilisateur
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrait le username du token
    public static String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Extrait l'ID utilisateur du token
    public static Long extractUserId(String token) {
        return ((Number) getClaims(token).get("userId")).longValue();
    }

    // Vérifie si le token est valide pour un username donné
    public static boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    // Vérifie si le token est expiré
    private static boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // Récupère les claims du token
    private static Claims getClaims(String token) {
        // retire "Bearer " si présent
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
