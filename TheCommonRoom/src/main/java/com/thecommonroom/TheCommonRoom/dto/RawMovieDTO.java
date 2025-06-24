package com.thecommonroom.TheCommonRoom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Clase que contiene todos los datos de una película que devuelve la API. Se mapeará a MovieDetailsDTO para
 * guardar solamente la informacion que nos interesa.
 */
@Schema(description = "DTO que representa todos los datos de una película recibidos desde la API " +
        "externa, antes de mapearlos a la entidad interna.")
@Getter
@Setter
@NoArgsConstructor // Para crear la instancia
public class RawMovieDTO {

    private Long id;
    private String title;
    private String overview;
    private Integer runtime;
    private String release_date;
    private Double vote_average;
    private Long budget;
    private Long revenue;
    private String poster_path;
    private String backdrop_path;
    private List<GenreDTO> genres;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class GenreDTO { // Clase interna, ya que la API devuelve un array de generos (id + nombre de genero)
        private Long id;
        private String name;
    }
}
