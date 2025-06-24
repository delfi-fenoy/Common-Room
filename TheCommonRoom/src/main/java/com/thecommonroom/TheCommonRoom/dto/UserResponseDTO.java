package com.thecommonroom.TheCommonRoom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "DTO que representa la información pública de un usuario, incluyendo " +
        "datos básicos y metadata de creación.")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String profilePictureUrl;
    private String description;
}
