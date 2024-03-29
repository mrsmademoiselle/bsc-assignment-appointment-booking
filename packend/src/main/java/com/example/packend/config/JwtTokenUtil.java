package com.example.packend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utilklasse für Bereitstellung von geläufigen JWT-Funktionen.
 */
@Component
public class JwtTokenUtil implements Serializable {

    // Token TTl
    public static final long JWT_TOKEN_VALIDITY = 1 * 60 * 60;
    private static final long serialVersionUID = -2550185165626007488L;
    @Value("${jwt.secret}")
    private String secret;

    // Benutzername aus Token reversen
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Ende der TLL aus Token reversen
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Secret rauslesen damit wir rückwirkend infos aus den Tokens ziehen können
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token.replace("{", "").replace("}", "")).getBody();
    }

    // Prüfen ob Token bereits abgelaufen
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Token generieren
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }


    // Erstellen des Tokens mit HS512
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8)).compact();
    }

    // Validieren über Username und TTL
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}