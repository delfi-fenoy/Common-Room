package com.thecommonroom.TheCommonRoom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Schema(description = "DTO que representa la respuesta JSON de la API externa (TMDb) con una " +
        "lista paginada de películas, incluyendo información de paginación y resultados.")
@Getter
@Setter
@NoArgsConstructor
public class RawMovieListDTO {
    private int page;
    private List<RawMovieDTO> results;
    private int total_pages;
    private int total_results;
}
