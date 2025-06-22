package com.thecommonroom.TheCommonRoom.controller.view;

import com.thecommonroom.TheCommonRoom.dto.MovieDetailsDTO;
import com.thecommonroom.TheCommonRoom.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador para renderizar la vista de peliculas
 */
@Controller
@RequiredArgsConstructor
public class MovieViewController {

    // =========== Atributos =========== \\
    private final MovieService movieService;

    // =========== Vista de detalle de una película =========== \\
    @GetMapping("/moviesheet/{id}")
    public String getMovieSheet(@PathVariable Long id, Model model) {
        MovieDetailsDTO movie = movieService.findMovieDetailsById(id);
        model.addAttribute("movie", movie);
        return "movie-sheet";
    }

    // =========== Búsqueda de películas por texto =========== \\
    @GetMapping("/search")
    public String search( @RequestParam String query, @RequestParam(defaultValue = "1") int page, Model model) {

        int pageSize = 20;
        var searchResultPage = movieService.searchMovies(query, page, pageSize);

        model.addAttribute("movies", searchResultPage.getResults());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", searchResultPage.getTotal_pages());
        model.addAttribute("query", query);

        return "search-results";
    }
}
