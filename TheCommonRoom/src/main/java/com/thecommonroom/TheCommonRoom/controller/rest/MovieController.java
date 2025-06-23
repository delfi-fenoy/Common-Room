package com.thecommonroom.TheCommonRoom.controller.rest;

import com.thecommonroom.TheCommonRoom.dto.MovieDetailsDTO;
import com.thecommonroom.TheCommonRoom.dto.MoviePreviewDTO;
import com.thecommonroom.TheCommonRoom.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    // =========== Atributos =========== \\
    private final MovieService movieService;

    // =========== Devuelve una película por ID =========== \\
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MovieDetailsDTO getMovieById(@PathVariable Long id) {
        return movieService.findMovieDetailsById(id);
    }

    // =========== Lista paginada de películas populares =========== \\
    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<MoviePreviewDTO> getPopularMovies(@RequestParam(defaultValue = "1") int page) {
        return movieService.getPopularMovies(page);
    }

    // =========== Lista paginada de películas recientes =========== \\
    @GetMapping("/recent")
    @ResponseStatus(HttpStatus.OK)
    public List<MoviePreviewDTO> getRecentMovies(@RequestParam(defaultValue = "1") int page) {
        return movieService.getRecentMovies(page);
    }

    // =========== Lista paginada de todas las películas =========== \\
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<MoviePreviewDTO> getAllMovies(@RequestParam(defaultValue = "1") int page) {
        return movieService.getAllMovies(page);
    }

    // =========== Lista paginada de próximos estrenos =========== \\
    @GetMapping("/uncoming")
    @ResponseStatus(HttpStatus.OK)
    public List<MoviePreviewDTO> getUncomingMovies(@RequestParam(defaultValue = "1") int page) {
        return movieService.getUncomingMovies(page);
    }

    ///Filtrar peliculas
    @GetMapping("/movies/filter")
    public ResponseEntity<List<MoviePreviewDTO>> filterMovies(@RequestParam(required = false) String title, @RequestParam(required = false) Integer year, @RequestParam(required = false) String genere, @RequestParam(required = false) Double puntuacionMinima, @RequestParam(defaultValue = "1") int page)
    {
        List<MoviePreviewDTO> filtered= movieService.filterMovies(title, year, genere, puntuacionMinima, page);

        return ResponseEntity.ok(filtered);
    }
}
