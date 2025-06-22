package com.thecommonroom.TheCommonRoom.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewUpdateDTO
{
    ///Se usa al actualizar la reseña
    @NotBlank(message = "El contenido no puede estar vacío")
    @Size(max = 700, message = "El contenido no puede superar los 700 caracteres")
    private String comment;

    @Min(value = 1, message= "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    private Integer rating;
}

