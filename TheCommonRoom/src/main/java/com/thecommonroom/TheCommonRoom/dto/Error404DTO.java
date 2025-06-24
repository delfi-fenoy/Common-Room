package com.thecommonroom.TheCommonRoom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "DTO que representa la información personalizada mostrada en una página de error 404.")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Error404DTO {
    private String backgroundImage;
    private String quote;
    private String movie;
}
