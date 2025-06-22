package com.thecommonroom.TheCommonRoom.auth.controller;

import com.thecommonroom.TheCommonRoom.auth.service.AuthService;
import com.thecommonroom.TheCommonRoom.auth.dto.LoginRequest;
import com.thecommonroom.TheCommonRoom.auth.dto.TokenResponse;
import com.thecommonroom.TheCommonRoom.dto.UserRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody UserRequestDTO userRequestDTO){
        TokenResponse tokenResponse = authService.register(userRequestDTO);
        return ResponseEntity.ok(tokenResponse); // Devolver codigo de estado 200 (ok) + tokens e info de usuario
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody LoginRequest loginRequest){
        TokenResponse tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok(tokenResponse); // Devolver codigo de estado 200 (ok) + tokens e info de usuario
    }

    // Generar nuevo access token (en caso que haya expirado), sin la necesidad de iniciar sesión nuevamente
    @PostMapping("/refresh")
    public TokenResponse refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        return authService.refreshToken(authHeader);
    }
}
