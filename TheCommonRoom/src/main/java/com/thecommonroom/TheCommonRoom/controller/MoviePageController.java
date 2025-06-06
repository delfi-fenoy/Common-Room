package com.thecommonroom.TheCommonRoom.controller;

import com.thecommonroom.TheCommonRoom.dto.MovieDetailsDTO;
import com.thecommonroom.TheCommonRoom.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controlador que maneja las solicitudes relacionadas con la ficha
 * de una película, obteniendo los datos mediante el servicio MovieService
 * y mostrando la vista correspondiente.
 */

@Controller
@RequiredArgsConstructor
public class MoviePageController {

    private final MovieService movieService;

    @GetMapping("/moviesheet/{id}") // Ruta con parámetro dinámico id
    public String getMovieSheet(@PathVariable Long id, Model model) {
        MovieDetailsDTO movie = movieService.findMovieById(id);
        model.addAttribute("movie", movie);  // Pasa la película a la vista
        return "moviesheet"; // apunta a templates/moviesheet.html
    }
}
