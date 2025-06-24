package com.thecommonroom.TheCommonRoom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "DTO que representa la respuesta de una reseña, incluyendo puntuación, " +
        "comentario, fecha, y datos básicos del usuario y la película relacionados.")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Al serializar json, se ignoran los campos null (no los muestra)
public class ReviewResponseDTO {

    private Long id;
    private Double rating;
    private String comment;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private MoviePreviewDTO moviePreview; // Pre-visualización de la película
    private UserPreviewDTO userPreview; // No mostrar información sensible

    // Los atributos moviePreview y userPreview pueden estar presente o no (null), dependiendo del endpoint
}
