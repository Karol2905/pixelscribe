package com.example.pixelscribe.util;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final String SECRET = "PixelScribeSuperSecretKeyForJWTTokenGeneration123!";
    private static final long JWT_EXPIRATION = 24 * 60 * 60 * 1000; // 24 horas

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Genera un token JWT solo con el email del usuario.
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrae el email (subject) del token.
     */
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Verifica si el token ha expirado.
     */
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * Valida que el token sea correcto y pertenezca al usuario indicado.
     */
    public boolean validateToken(String token, String email) {
        return extractEmail(token).equals(email) && !isTokenExpired(token);
    }

    /**
     * Extrae todos los claims (internos) del token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
