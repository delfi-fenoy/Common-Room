package com.thecommonroom.TheCommonRoom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
public class HomeController {

    @GetMapping("/home")
    public String index() {
        return "index"; // templates/index.html
    }

    @GetMapping("/moviesheet")
    public String moviesheet() {
        return "moviesheet"; // templates/moviesheet.html
    }

    @GetMapping("/signin")
    public String signin() {
        return "signin"; // templates/signin.html
    }

}
