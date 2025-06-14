package com.thecommonroom.TheCommonRoom.controller;

import com.thecommonroom.TheCommonRoom.client.TMDbClient;
import com.thecommonroom.TheCommonRoom.dto.RawMovieListDTO;
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

    private final TMDbClient tmdbClient;

    @GetMapping("/home")
    public String index(@RequestParam(defaultValue = "1") int page, Model model) {
        RawMovieListDTO movieList = tmdbClient.getPopularMovies(page);
        model.addAttribute("movies", movieList.getResults());
        model.addAttribute("currentPage", page);
        return "index";
    }

    @GetMapping("/signin")
    public String signin() {
        return "signin";
    }
}
