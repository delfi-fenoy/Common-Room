package com.thecommonroom.TheCommonRoom.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Esta clase representa los datos necesarios para iniciar sesión (nombre de usuario y contraseña).
 *
 * Separar estos valores ayuda a estructurar mejor el código y facilita agregar nuevos campos en el futuro.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String username;
    private String password;
}
