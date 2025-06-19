package com.thecommonroom.TheCommonRoom.controller;

import com.thecommonroom.TheCommonRoom.client.TMDbClient;
import com.thecommonroom.TheCommonRoom.dto.RawMovieListDTO;
import com.thecommonroom.TheCommonRoom.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador que maneja las rutas principales estáticas de la aplicación,
 * como la página de inicio, la ficha general de películas y la página de inicio de sesión. (por ahora)
 * <p>
 * - @Controller se usa para devolver vistas (HTML, plantillas) y requiere que los métodos retornen
 *   nombres de plantillas para renderizar. <p>
 * - @RestController es una combinación de @Controller y @ResponseBody, usada para APIs REST,
 *   devuelve directamente datos (como JSON) en lugar de vistas.
 */

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MovieService movieService;
    private final TMDbClient tmdbClient;

    // Muestra la página de inicio. Los datos de películas se cargan desde JS.
    @GetMapping("/home")
    public String index(@RequestParam(defaultValue = "1") int page, Model model) {
        RawMovieListDTO movieList = tmdbClient.getPopularMovies(page);
        model.addAttribute("movies", movieList.getResults());
        model.addAttribute("currentPage", page);
        return "index";
    }

    // Muestra la página de inicio de sesión
    @GetMapping("/signin")
    public String signin() {
        return "signin";
    }

    // Muestra el catálogo de todas las películas paginadas
    @GetMapping("/movies")
    public String movies(@RequestParam(defaultValue = "1") int page, Model model) {
        RawMovieListDTO movieList = tmdbClient.getAllMovies(page);
        model.addAttribute("movies", movieList.getResults());
        model.addAttribute("currentPage", page);
        return "moviesmenu";
    }

    // Búsqueda de películas según el texto ingresado (query)
    @GetMapping("/search")
    public String search(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            Model model) {

        int pageSize = 20;
        var searchResultPage = movieService.searchMovies(query, page, pageSize);

        model.addAttribute("movies", searchResultPage.getResults());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", searchResultPage.getTotal_pages());
        model.addAttribute("query", query);

        return "search-results";
    }



}