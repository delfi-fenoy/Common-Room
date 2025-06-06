package com.thecommonroom.TheCommonRoom.mapper;

import com.thecommonroom.TheCommonRoom.dto.MovieDetailsDTO;
import com.thecommonroom.TheCommonRoom.dto.RawMovieDTO;
import io.swagger.v3.oas.models.links.Link;

import java.util.List;

public class MovieMapper {

    public static MovieDetailsDTO rawToDTO(RawMovieDTO raw){
        // La API no devuelve la URL completa, unicamente la ruta, por lo que debemos concatener con:
        String posterBaseUrl = "https://image.tmdb.org/t/p/w500";
        String backdropBaseUrl = "https://image.tmdb.org/t/p/original";

        List<String> genreNames = raw.getGenres().stream() // Pasar los nombres de los generos a la lista
                                        .map(genre -> genre.getName()).toList();

        return MovieDetailsDTO.builder()
                .id(raw.getId())
                .title(raw.getTitle())
                .synopsis(raw.getOverview())
                .duration(raw.getRuntime())
                .releaseDate(raw.getRelease_date())
                .genres(genreNames)
                .voteAverage(raw.getVote_average())
                .budget(raw.getBudget())
                .revenue(raw.getRevenue())
                .posterUrl(posterBaseUrl + raw.getPoster_path()) // Concatenar url base + ruta de poster
                .backdropUrl(backdropBaseUrl + raw.getBackdrop_path()) // Concatenar url base + ruta de fondo
                .build();
    }
}
