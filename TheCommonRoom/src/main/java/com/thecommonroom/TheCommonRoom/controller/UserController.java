package com.thecommonroom.TheCommonRoom.controller;

import com.thecommonroom.TheCommonRoom.auth.service.JwtService;
import com.thecommonroom.TheCommonRoom.dto.UserPreviewDTO;
import com.thecommonroom.TheCommonRoom.dto.UserRequestDTO;
import com.thecommonroom.TheCommonRoom.dto.UserResponseDTO;
import com.thecommonroom.TheCommonRoom.model.User;
import com.thecommonroom.TheCommonRoom.repository.UserRepository;
import com.thecommonroom.TheCommonRoom.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    // =========== Atributos =========== \\
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    // =========== Lista todos los usuarios en formato reducido =========== \\
    @GetMapping
    public ResponseEntity<List<UserPreviewDTO>> listUsers() {
        List<UserPreviewDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // =========== Devuelve un usuario por su nombre de usuario =========== \\
    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO getUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }

    // =========== Actualiza un usuario por su ID =========== \\
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody @Valid UserRequestDTO dto) {
        userService.updateUser(id, dto);
        return ResponseEntity.ok("Usuario actualizado correctamente");
    }

    // =========== Elimina un usuario por su ID =========== \\
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }

    // =========== Devuelve el perfil del usuario autenticado (por token JWT) =========== \\
    @GetMapping("/me")
    public UserResponseDTO getCurrentUser(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No hay token JWT válido en la solicitud");
        }

        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }
}
