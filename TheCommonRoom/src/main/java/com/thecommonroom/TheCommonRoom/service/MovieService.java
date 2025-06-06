package com.thecommonroom.TheCommonRoom.service;

import com.thecommonroom.TheCommonRoom.client.TMDbClient;
import com.thecommonroom.TheCommonRoom.dto.MovieDetailsDTO;
import com.thecommonroom.TheCommonRoom.dto.RawMovieDTO;
import com.thecommonroom.TheCommonRoom.mapper.MovieMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final TMDbClient api;

    public MovieDetailsDTO findMovieById(Long id){
        RawMovieDTO rawMovieDTO = api.getMovieById(id); // Conseguir la pelicula por id
        return MovieMapper.rawToDTO(rawMovieDTO); // Mapear a dto
    }
}
