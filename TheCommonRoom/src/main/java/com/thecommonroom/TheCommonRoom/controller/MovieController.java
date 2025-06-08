package com.thecommonroom.TheCommonRoom.controller;

import com.thecommonroom.TheCommonRoom.dto.MovieDetailsDTO;
import com.thecommonroom.TheCommonRoom.dto.MoviePreviewDTO;
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
    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<MoviePreviewDTO> getPopularMovies(@RequestParam(defaultValue = "1") int page){
        return movieService.getPopularMovies(page);
    }

    @GetMapping("/recent")
    @ResponseStatus(HttpStatus.OK)
    public List<MoviePreviewDTO> getRecentMovies(@RequestParam(defaultValue = "1") int page){
        return movieService.getRecentMovies(page);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<MoviePreviewDTO> getAllMovies(@RequestParam(defaultValue = "1") int page){
        return movieService.getAllMovies(page);
    }

    @GetMapping("/uncoming")
    @ResponseStatus(HttpStatus.OK)
    public List<MoviePreviewDTO> getUncomingMovies(@RequestParam(defaultValue = "1") int page){
        return movieService.getUncomingMovies(page);
    }
}
