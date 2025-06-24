package com.thecommonroom.TheCommonRoom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PasswordUpdateDTO {

    @NotBlank(message = "La contraseña no debe estar vacía")
    private String oldPassword;

    @NotBlank(message = "La contraseña no debe estar vacía")
    @Size(min = 8, message = "La nueva contraseña debe tener como mínimo 8 caracteres")
    private String newPassword;

    @NotBlank(message = "La contraseña no debe estar vacía")
    @Size(min = 8, message = "La confirmación debe tener como mínimo 8 caracteres")
    private String confirmPassword;
}
