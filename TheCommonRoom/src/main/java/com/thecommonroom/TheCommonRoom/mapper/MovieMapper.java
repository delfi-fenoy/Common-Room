package com.thecommonroom.TheCommonRoom.mapper;

import com.thecommonroom.TheCommonRoom.dto.MovieDetailsDTO;
import com.thecommonroom.TheCommonRoom.dto.RawMovieDTO;

import java.util.List;

public class MovieMapper {

    public static MovieDetailsDTO rawToDTO(RawMovieDTO raw){
        String posterBaseUrl = "https://image.tmdb.org/t/p/w500";
        String backdropBaseUrl = "https://image.tmdb.org/t/p/original";

        List<String> genreNames = raw.getGenres() != null
                ? raw.getGenres().stream()
                .map(RawMovieDTO.GenreDTO::getName)
                .toList()
                : List.of();  // lista vacía si es null

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
                .posterUrl(posterBaseUrl + raw.getPoster_path())
                .backdropUrl(backdropBaseUrl + raw.getBackdrop_path())
                .build();
    }

}
