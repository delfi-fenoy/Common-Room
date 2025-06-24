package com.thecommonroom.TheCommonRoom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Este DTO mostrará la información en el listado de películas (título, póster, etc).
 */
@Schema(description = "DTO que muestra la información básica de una película para listados, " +
        "incluyendo título, póster y fecha de estreno.")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MoviePreviewDTO {

    private Long id;
    private String title;
    private String synopsis;
    private String releaseDate;
    private String posterUrl;
}
