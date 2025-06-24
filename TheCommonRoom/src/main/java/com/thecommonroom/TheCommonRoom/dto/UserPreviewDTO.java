package com.thecommonroom.TheCommonRoom.dto;

import com.thecommonroom.TheCommonRoom.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "DTO con información básica y necesaria de un usuario para vistas " +
        "resumidas o listados.")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreviewDTO {
    ///Envio la informacion necesaria para el front
    private Long id;
    private String username;
    private String profilePictureUrl;
    private Role role;
}
