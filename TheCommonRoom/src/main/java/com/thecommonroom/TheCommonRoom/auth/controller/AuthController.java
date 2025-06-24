package com.thecommonroom.TheCommonRoom.auth.controller;

import com.thecommonroom.TheCommonRoom.auth.service.AuthService;
import com.thecommonroom.TheCommonRoom.auth.dto.LoginRequest;
import com.thecommonroom.TheCommonRoom.auth.dto.TokenResponse;
import com.thecommonroom.TheCommonRoom.dto.UserRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Autenticación",
        description = "Endpoints para registro, inicio de sesión y renovación de tokens JWT."
)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Registra un nuevo usuario en la aplicación y devuelve tokens de autenticación."
    )
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody UserRequestDTO userRequestDTO){
        TokenResponse tokenResponse = authService.register(userRequestDTO);
        return ResponseEntity.ok(tokenResponse); // Devolver codigo de estado 200 (ok) + tokens e info de usuario
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica a un usuario con sus credenciales y devuelve tokens de " +
                    "acceso y refresh."
    )
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody LoginRequest loginRequest){
        TokenResponse tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok(tokenResponse); // Devolver codigo de estado 200 (ok) + tokens e info de usuario
    }

    // Generar nuevo access token (en caso que haya expirado), sin la necesidad de iniciar sesión nuevamente
    @Operation(
            summary = "Renovar token de acceso",
            description = "Genera un nuevo token de acceso utilizando un token de refresh válido, " +
                    "sin necesidad de reingresar credenciales."
    )
    @PostMapping("/refresh")
    public TokenResponse refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        return authService.refreshToken(authHeader);
    }
}
