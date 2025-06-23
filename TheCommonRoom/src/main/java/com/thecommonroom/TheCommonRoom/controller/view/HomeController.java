package com.thecommonroom.TheCommonRoom.controller.view;

import com.thecommonroom.TheCommonRoom.client.TMDbClient;
import com.thecommonroom.TheCommonRoom.dto.RawMovieListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador que maneja las rutas principales estáticas de la aplicación.
 * <p>
 * - @Controller se usa para devolver vistas (HTML, plantillas) y requiere que los métodos retornen
 *   nombres de plantillas para renderizar. <p>
 * - @RestController es una combinación de @Controller y @ResponseBody, usada para APIs REST,
 *   devuelve directamente datos (como JSON) en lugar de vistas.
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    // =========== Atributos =========== \\
    private final TMDbClient tmdbClient;

    // =========== Página principal con películas populares =========== \\
    @GetMapping("/home")
    public String index(@RequestParam(defaultValue = "1") int page, Model model) {
        RawMovieListDTO movieList = tmdbClient.getPopularMovies(page);
        model.addAttribute("movies", movieList.getResults());
        model.addAttribute("currentPage", page);
        return "index";
    }

    // =========== Página de inicio de sesión =========== \\
    @GetMapping("/signin")
    public String signin() {
        return "signin";
    }

    // =========== Catálogo general de películas paginado =========== \\
    @GetMapping("/movies")
    public String movies(@RequestParam(defaultValue = "1") int page, Model model) {
        RawMovieListDTO movieList = tmdbClient.getAllMovies(page);
        model.addAttribute("movies", movieList.getResults());
        model.addAttribute("currentPage", page);
        return "moviesmenu";
    }
}