package com.thecommonroom.TheCommonRoom.controller;

import com.thecommonroom.TheCommonRoom.dto.MovieDetailsDTO;
import com.thecommonroom.TheCommonRoom.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MovieDetailsDTO getMovieById(@PathVariable Long id){
        return movieService.findMovieById(id);
    }

    ///  PAGINACION DE PELICULAS | Devuelve una lista paginada de películas populares
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<MovieDetailsDTO> getPaginatedMovies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return movieService.getPaginatedMovies(page, size);
    }

}
