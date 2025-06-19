package com.thecommonroom.TheCommonRoom.auth.service;

import com.thecommonroom.TheCommonRoom.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class JwtService {

    // Atributos
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    // Métodos
    public String generateToken(User user){
        return buildToken(user, jwtExpiration);
    }

    public String generateRefreshToken(User user){
        return buildToken(user, refreshExpiration);
    }

    // Ambos métodos llaman a este, y le pasan el tiempo de expiracion correspondiente (access o refresh)
    private String buildToken(User user, long expiration){
        // Guardar información adicional (claims) para incluir en el token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());

        // Construir token con la info y configuraciones necesarias
        return Jwts.builder()
                .setClaims(claims) // Agregar los claims personalizados al token
                .setSubject(user.getUsername()) // Identificador principal, usualemente el username o email
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha y hora en que se generó el token
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Fecha de expiración (ahora + tiempo que expiración)
                .signWith(getSignInKey()) // Firmar el token con la clave secreta para seguridad
                .compact(); // Construir y devolver el token como String
    }

    // Generar clave privada para token
    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Decodificar la clave secreta (generada por nosotros) en Base64 (para manejar bytes)
        return Keys.hmacShaKeyFor(keyBytes); // Crear la clave HMAC-SHA con esos bytes para firmar el token
    }

    public String extractUsername(String token){
        Claims jwtToken = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody(); // info que contiene el token
        return jwtToken.getSubject();
    }

    public Date extractExpiration(String token){
        Claims jwtToken = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody(); // info que contiene el token
        return jwtToken.getExpiration(); // Obtener la expiracion del token
    }

    public boolean isTokenValid(String token, User user){
        String username = extractUsername(token); // Extraer el username del token
        return (username.equals(user.getUsername())) && !isTokenExpired(token); // Comparar username de token con username del usuario (deben ser iguales) + que no este expirado el token
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date()); // Si la expiracion es anterior a ahora, retorna true
    }
}
