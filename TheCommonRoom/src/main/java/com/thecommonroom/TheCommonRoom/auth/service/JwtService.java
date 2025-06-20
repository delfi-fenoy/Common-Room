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

/**
 * Esta clase se encarga de generar, validar y extraer información de los tokens JWT.
 *
 * Básicamente, crea tokens seguros con datos del usuario, verifica que esos tokens sean válidos y no hayan expirado,
 * y extrae información como el usuario o el rol que están guardados dentro del token (Payload).
 * Así, facilita la autenticación y autorización en la aplicación usando JWT.
 */
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
        return buildToken(user, jwtExpiration); // Crear access token, pasandole su tiempo de expiración
    }

    public String generateRefreshToken(User user){
        return buildToken(user, refreshExpiration); // Crear refresh token, pasandole su tiempo de expiración
    }

    // Ambos métodos de generate llaman a este, y le pasan el tiempo de expiracion correspondiente (access o refresh)
    private String buildToken(User user, long expiration){
        // Guardar información adicional (claims) para incluir en el Payload del token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());

        // Construir token con la info y configuraciones necesarias
        return Jwts.builder()
                .setClaims(claims) // Agregar los claims personalizados al token
                .setSubject(user.getUsername()) // Identificador principal, usualemente el username o email del usuario
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha y hora en que se generó el token
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Fecha de expiración del token (ahora + tiempo de expiración)
                .signWith(getSignInKey()) // Firmar el token con la clave secreta para seguridad (signature del token)
                .compact(); // Construir y devolver el token como String
    }

    // Generar la clave secreta que se usará para firmar el token JWT (signature)
    private SecretKey getSignInKey(){
        // Decodificar la cadena 'secretKey' (que está codificada en Base64) para obtener un arreglo de bytes.
        /* Esto es necesario porque la clave para firmar el token debe estar en formato binario (bytes),
           no como texto plano. */
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        /* Crear clave secreta (SecretKey) usando el arreglo de bytes, aplicando el algoritmo HMAC-SHA,
           que es el metodo criptográfico usado para firmar y validar la integridad del token. */
        return Keys.hmacShaKeyFor(keyBytes);
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
