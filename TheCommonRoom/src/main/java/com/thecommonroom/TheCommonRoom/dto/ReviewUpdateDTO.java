package com.thecommonroom.TheCommonRoom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "DTO para actualizar una reseña, permitiendo modificar la puntuación y el comentario.")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewUpdateDTO {

    @DecimalMin(value = "0.5")
    @DecimalMax(value = "5")
    private Double rating;

    @Size(max = 700)
    private String comment;
}
