package com.thecommonroom.TheCommonRoom.auth.service;

import com.thecommonroom.TheCommonRoom.auth.repository.Token;
import com.thecommonroom.TheCommonRoom.auth.repository.TokenRepository;
import com.thecommonroom.TheCommonRoom.auth.repository.TokenType;
import com.thecommonroom.TheCommonRoom.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

/**
 * Esta clase se encarga de generar, validar y extraer información de los tokens JWT.
 * Básicamente, crea tokens seguros con datos del usuario, verifica que esos tokens sean válidos y no hayan expirado,
 * y extrae información como el usuario o el rol que están guardados dentro del token (Payload).
 * Así, facilita la autenticación y autorización en la aplicación usando JWT.
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    // Atributos
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private final TokenRepository tokenRepository;

    // ========== GENERAR TOKENS ==========

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

    // ========== GUARDAR Y BORRAR TOKENS EN LA BDD ==========

    // Guardar token asociado al usuario en la base de datos
    public void saveUserToken(User user, String jwtToken){
        var token = Token.builder()
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .user(user) // Asociar usuario al token
                .build(); // Generar instancia de Token
        tokenRepository.save(token); // Guardar token en la base de datos
    }

    // Revocar tokens de un usuario
    public void revokeAllUserTokens(User user){
        // Buscar tokens validos del user
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUserId(user.getId());
        if (!validUserTokens.isEmpty()){ // Si se encontraron tokens validos
            for(Token token : validUserTokens){
                // Marcar verdadero a revocado y expirado
                token.setRevoked(true);
                token.setExpired(true);
            }
            tokenRepository.saveAll(validUserTokens); // Guardar los cambios
        }
    }

    // ========== EXTRAER DE TOKENS ==========

    // Extraer username de un token
    public String extractUsername(String token){
        // Se parsea el token usando la clave secreta para validar la firma y obtener los claims (datos del user)
        Claims jwtToken = Jwts.parserBuilder()
                .setSigningKey(getSignInKey()) // Le pasamos la clave secreta para verificar el token
                .build()
                .parseClaimsJws(token) // Decodifica y valida el token JWT
                .getBody(); // Extrae el cuerpo del token (datos que contiene del user)

        return jwtToken.getSubject(); // Devuelve el subject (username)
    }

    // Extraer expiracion de un token
    public Date extractExpiration(String token){
        Claims jwtToken = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody(); // info que contiene el token
        return jwtToken.getExpiration(); // Obtener la expiracion del token
    }

    // ========== VALIDAR TOKENS ==========

    // Chequear si el token es valido
    public boolean isTokenValid(String token, User user){
        String username = extractUsername(token); // Extraer el username del token
        return (username.equals(user.getUsername())) && !isTokenExpired(token); // Comparar username de token con username del usuario (deben ser iguales) + que no este expirado el token
    }

    // Chequear si el token está expirado
    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date()); // Si la expiracion es anterior a ahora, retorna true
    }
}
