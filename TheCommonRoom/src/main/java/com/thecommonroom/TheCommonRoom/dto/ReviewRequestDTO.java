package com.thecommonroom.TheCommonRoom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "DTO para crear una nueva reseña, que incluye la puntuación, " +
        "comentario opcional y el ID de la película asociada.")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewRequestDTO {

    @NotNull
    @DecimalMin(value = "0.5")
    @DecimalMax(value = "5")
    private Double rating;

    @Size(max = 700)
    private String comment;

    @NotNull
    private Long movieId;
}
