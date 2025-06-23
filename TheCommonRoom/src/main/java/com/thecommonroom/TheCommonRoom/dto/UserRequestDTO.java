package com.thecommonroom.TheCommonRoom.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequestDTO {

    @NotBlank(message = "El username no debe estar vacío")
    @Size(min = 5, max = 20, message = "El username debe tener entre 5 y 20 caracteres")
    private String username;

    @NotBlank(message = "La contraseña no debe estar vacía")
    @Size(min = 8, message = "La contraseña debe tener como mínimo 8 caracteres")
    private String password;

    @Email(message = "El email debe tener un formato válido")
    @NotBlank(message = "El email no debe estár vacío")
    @Size(max = 50, message = "El email debe tener como máximo 50 caracteres")
    private String email;

    // @Pattern = Permite que el campo profilePictureUrl sea:
    // + vacío (no obligado a completar) o
    // + una URL válida que empiece con http://, https:// o ftp://.
    @Pattern(regexp = "^$|^(https?|ftp)://.*$", message = "La URL de la foto de perfil debe ser valida")
    private String profilePictureUrl;

    @Size(max = 255, message = "La descripción debe tener como máximo 255 caracteres")
    private String description;
}
