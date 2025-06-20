package com.thecommonroom.TheCommonRoom.auth.service;

import com.thecommonroom.TheCommonRoom.auth.dto.LoginRequest;
import com.thecommonroom.TheCommonRoom.auth.dto.TokenResponse;
import com.thecommonroom.TheCommonRoom.auth.repository.TokenType;
import com.thecommonroom.TheCommonRoom.auth.repository.Token;
import com.thecommonroom.TheCommonRoom.auth.repository.TokenRepository;
import com.thecommonroom.TheCommonRoom.dto.UserRequestDTO;
import com.thecommonroom.TheCommonRoom.model.User;
import com.thecommonroom.TheCommonRoom.repository.UserRepository;
import com.thecommonroom.TheCommonRoom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager; // Para procesar y validar las credenciales de usuarios

    // ========== REGISTRARSE ==========
    public TokenResponse register(UserRequestDTO userRequestDTO){
        // Guardar user en base de datos y devolver el usuario (entidad)
        var savedUser = userService.createUser(userRequestDTO); // var => El compilador deduce el tipo de la variable a partir del valor que se le asigna

        // Generar tokens (access y refresh)
        var jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(savedUser);

        // Guardar el token asociado al usuario en la base de datos
        saveUserToken(savedUser, jwtToken);

        // Devolver ambos tokens al front
        return new TokenResponse(jwtToken, refreshToken, savedUser.getUsername(), savedUser.getRole().name());
    }

    // Guardar token asociado al usuario en la base de datos
    private void saveUserToken(User user, String jwtToken){
        var token = Token.builder()
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .user(user) // Asociar usuario al token
                .build(); // Generar instancia de Token
        tokenRepository.save(token); // Guardar token en la base de datos
    }

    // ========== INICIAR SESIÓN ==========
    public TokenResponse login(LoginRequest request){
        // Validar el username y password enviados
        // Si no son válidos, lanza una excepción automáticamente
        authenticationManager.authenticate( // Llama automaticamente a autheticantionProvider() (en SecurityConfig)
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        // Busca al usuario en la base de datos por su username
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        // Genera un nuevo token JWT y refresh token para este usuario (se genera uno nuevo por cada login)
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user); // Revocar tokens validos viejos del usuario en la bdd
        saveUserToken(user, jwtToken); // Guardar token nuevo
        // Devuelve al front ambos tokens + info útil (username y rol)
        return new TokenResponse(jwtToken, refreshToken, user.getUsername(), user.getRole().name());
    }

    // Revocar tokens de un usuario
    private void revokeAllUserTokens(User user){
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

    // ========== REFRESH TOKEN ==========
    public TokenResponse refreshToken(String authHeader){
        // Validar que el header no sea nulo y que comience con "Bearer "
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new IllegalArgumentException("Invalid Bearer Token");
        }
        String refreshToken = authHeader.substring(7); // Obtener token sin "Bearer:"
        String username = jwtService.extractUsername(refreshToken); // Extraer el username del token

        if(username == null){ // Si el username está vacío, error
            throw new IllegalArgumentException("Invalid Refresh Token");
        }

        User user = userRepository.findByUsername(username) // Buscar user por username del token en la bdd
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if(!jwtService.isTokenValid(refreshToken, user)){ // Verificar que el token sea valido para ese usuario
            throw new IllegalArgumentException("Invalid Refresh Token");
        }

        String accessToken = jwtService.generateToken(user); // Generar nuevo token de acceso
        revokeAllUserTokens(user); // Revocar tokens anteriores del usuario
        saveUserToken(user, accessToken); // Guardar token nuevo del usuario
        // Devolver ambos token + info adicional del usuario
        return new TokenResponse(accessToken, refreshToken, user.getUsername(), user.getRole().name());
    }
}
