package com.thecommonroom.TheCommonRoom.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

// record => Tipo de clase que genera automáticamente: constructor, getters, equals(), hashcode() y toString().

/**
 * Esta clase representa los datos que se devolverán al iniciar sesión.
 *
 * @param accessToken Token de acceso, para poder identificar al usuario en cada petición.
 * @param refreshToken Para generar un nuevo access token sin que el usuario vuelva a iniciar sesión.
 * @param username Nombre de usuario.
 * @param role Rol de usuario (USER, ADMIN).
 */
public record TokenResponse(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        String refreshToken,
        String username,
        String role
) {
}
