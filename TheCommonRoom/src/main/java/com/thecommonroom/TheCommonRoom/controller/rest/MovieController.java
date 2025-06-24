package com.thecommonroom.TheCommonRoom.controller.rest;

import com.thecommonroom.TheCommonRoom.dto.MovieDetailsDTO;
import com.thecommonroom.TheCommonRoom.dto.MoviePreviewDTO;
import com.thecommonroom.TheCommonRoom.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Películas",
        description = "Endpoints para obtener listas y detalles de películas, con soporte para " +
                "paginación y diferentes categorías como populares, recientes y próximos estrenos."
)
@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    // =========== Atributos =========== \\
    private final MovieService movieService;

    // =========== Lista paginada de todas las películas =========== \\
    @Operation(
            summary = "Lista paginada de todas las películas",
            description = "Devuelve una lista paginada de todas las películas disponibles. " +
                    "Parámetro 'page' opcional para seleccionar la página."
    )
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<MoviePreviewDTO> getAllMovies(@RequestParam(defaultValue = "1") int page) {
        return movieService.getAllMovies(page);
    }

    // =========== Devuelve una película por ID =========== \\
    @Operation(
            summary = "Obtener detalles de una película",
            description = "Devuelve información detallada de una película según su ID."
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MovieDetailsDTO getMovieById(@PathVariable Long id) {
        return movieService.findMovieDetailsById(id);
    }

    // =========== Lista paginada de películas populares =========== \\
    @Operation(
            summary = "Lista paginada de películas populares",
            description = "Devuelve una lista paginada de películas populares."
    )
    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<MoviePreviewDTO> getPopularMovies(@RequestParam(defaultValue = "1") int page) {
        return movieService.getPopularMovies(page);
    }

    // =========== Lista paginada de películas recientes =========== \\
    @Operation(
            summary = "Lista paginada de películas recientes",
            description = "Devuelve una lista paginada de películas recientes."
    )
    @GetMapping("/recent")
    @ResponseStatus(HttpStatus.OK)
    public List<MoviePreviewDTO> getRecentMovies(@RequestParam(defaultValue = "1") int page) {
        return movieService.getRecentMovies(page);
    }

    // =========== Lista paginada de próximos estrenos =========== \\
    @Operation(
            summary = "Lista paginada de próximos estrenos",
            description = "Devuelve una lista paginada de películas que serán estrenadas próximamente."
    )
    @GetMapping("/upcoming")
    @ResponseStatus(HttpStatus.OK)
    public List<MoviePreviewDTO> getUpcomingMovies(@RequestParam(defaultValue = "1") int page) {
        return movieService.getUpcomingMovies(page);
    }
}
