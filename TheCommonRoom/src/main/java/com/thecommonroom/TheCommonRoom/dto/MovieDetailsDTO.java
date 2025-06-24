package com.thecommonroom.TheCommonRoom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * Este DTO contiene la información que se mostrará en la ficha de una película.
 */
@Schema(description = "DTO que contiene la información detallada que se muestra en la " +
        "ficha de una película.")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MovieDetailsDTO {

    private Long id;
    private String title;
    private String synopsis;
    private Integer duration;
    private String releaseDate;
    private List<String> genres;
    private Double voteAverage;
    private Long budget;
    private Long revenue; // Recaudación
    private String posterUrl;
    private String backdropUrl; // URL de imagen de fondo
}
