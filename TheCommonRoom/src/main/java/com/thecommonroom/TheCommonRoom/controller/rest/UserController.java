package com.thecommonroom.TheCommonRoom.controller.rest;

import com.thecommonroom.TheCommonRoom.auth.dto.TokenResponse;
import com.thecommonroom.TheCommonRoom.auth.service.JwtService;
import com.thecommonroom.TheCommonRoom.dto.PasswordUpdateDTO;
import com.thecommonroom.TheCommonRoom.dto.UserPreviewDTO;
import com.thecommonroom.TheCommonRoom.dto.UserResponseDTO;
import com.thecommonroom.TheCommonRoom.dto.UserUpdateDTO;
import com.thecommonroom.TheCommonRoom.model.User;
import com.thecommonroom.TheCommonRoom.repository.UserRepository;
import com.thecommonroom.TheCommonRoom.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(
        name = "Usuarios",
        description = "Endpoints relacionados con la gestión de cuentas en la aplicación. " +
                "Permiten listar usuarios (en formato detallado o resumido), modificar datos personales y contraseña, " +
                "eliminar cuentas y acceder al perfil propio a través del token JWT."
)
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    // =========== Atributos =========== \\
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    // =========== Lista todos los usuarios en formato reducido =========== \\
    @Operation(
            summary = "Listar todos los usuarios",
            description = "Devuelve una lista de todos los usuarios registrados en formato reducido."
    )
    @GetMapping("/all")
    public ResponseEntity<List<UserPreviewDTO>> listUsers() {
        List<UserPreviewDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // =========== Devuelve un usuario por su nombre de usuario =========== \\
    @Operation(
            summary = "Obtener usuario por username",
            description = "Devuelve la información pública de un usuario a partir de su username."
    )
    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO getUserByUsername(@PathVariable String username) {
        return userService.getUserResponse(username);
    }

    // =========== Elimina un usuario por su username =========== \\
    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina la cuenta de un usuario por su username. Solo puede hacerlo " +
                    "el propio usuario o un administrador."
    )
    @PreAuthorize("#username == authentication.name or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username){
        userService.deleteUser(username);
    }

    @Operation(
            summary = "Modificar datos del usuario",
            description = "Permite modificar los datos básicos de una cuenta. Solo el dueño puede " +
                    "realizar esta acción. Si se cambia el nombre de usuario, se genera un nuevo " +
                    "token JWT en la respuesta."
    )
    @PreAuthorize("#username == authentication.name")
    @PutMapping("/{username}")
    public ResponseEntity<?> modifyUser(@PathVariable String username,
                                        @Valid @RequestBody UserUpdateDTO userUpdateDTO){
        TokenResponse tokenResponse = userService.modifyUser(username, userUpdateDTO);

        if(tokenResponse != null){ // Si se modifica el username, se genera nuevo token
            return ResponseEntity.ok(tokenResponse);
        } else {
            return ResponseEntity.noContent().build(); // Caso contrario, no devuelve nada (no content)
        }
    }

    @Operation(
            summary = "Modificar contraseña",
            description = "Permite al usuario autenticado actualizar su contraseña. Solo el dueño " +
                    "de la cuenta puede hacerlo."
    )
    @PreAuthorize("#username == authentication.name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{username}/password")
    public void modifyPassword(@PathVariable String username,
                               @Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO){
        userService.modifyPassword(username, passwordUpdateDTO);
    }

    // =========== Devuelve el perfil del usuario autenticado (por token JWT) =========== \\
    @Operation(
            summary = "Obtener perfil propio",
            description = "Devuelve la información del usuario autenticado, extraída del token JWT " +
                    "enviado en la cabecera (header) Authorization."
    )
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
