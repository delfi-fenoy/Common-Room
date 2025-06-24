package com.thecommonroom.TheCommonRoom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Schema(description = "DTO para actualizar los datos básicos de un usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserUpdateDTO {

    @Size(min = 5, max = 20, message = "El username debe tener entre 5 y 20 caracteres")
    private String username;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 50, message = "El email debe tener como máximo 50 caracteres")
    private String email;

    @Size(max = 255, message = "La descripción debe tener como máximo 255 caracteres")
    private String description;

    @URL(message = "La URL de la foto de perfil debe ser valida")
    private String profilePictureUrl;
}
