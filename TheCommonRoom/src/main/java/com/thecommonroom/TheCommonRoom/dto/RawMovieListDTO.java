package com.thecommonroom.TheCommonRoom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

/// Representa la respuesta JSON de la API cuando devuelve una lista de películas populares
@Getter
@Setter
@NoArgsConstructor
public class RawMovieListDTO {
    private int page;
    private List<RawMovieDTO> results;
    private int total_pages;
    private int total_results;
}
