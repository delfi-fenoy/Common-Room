package com.thecommonroom.TheCommonRoom.controller;

import com.thecommonroom.TheCommonRoom.dto.Error404DTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Random;

@Controller
public class ErrorController {

    // Muestra una página de error 404 personalizada con una versión aleatoria
    @RequestMapping("/error/404")
    public String error404(Model model) {

        // Lista de versiones de error 404 con imágenes y frases temáticas
        List<Error404DTO> versions = List.of(
                new Error404DTO("/img/errors/obliviate.jpg", "Obliviate. Este rincón de la web ha sido borrado de tu memoria.", "Harry Potter and the Deathly Hallows: Part 1 (2010)"),
                new Error404DTO("/img/errors/pensadero.png", "Has entrado a un recuerdo que ya no existe.", "Fantastic Beasts: The Secrets of Dumbledore (2022)"),
                new Error404DTO("/img/errors/farquaad.jpg", "Estás más perdido que Lord Farquaad buscando altura.", "Shrek 1 (2001)"),
                new Error404DTO("/img/errors/shrek.jpeg", "Esta página es como la cebolla: tiene capas... pero ninguna te da lo que buscas.", "Shrek 1 (2001)"),
                new Error404DTO("/img/errors/yoda.jpg", "El maestro Yoda dijo: 'Que tu búsqueda termine en otro lugar debe.'", "Star Wars: Episode V - The Empire Strikes Back (1980)"),
                new Error404DTO("/img/errors/obiwan.jpg", "Has perdido la conexión con la Fuerza... y con esta página.", "Star Wars: Episode III - Revenge of the Sith (2005)")
        );

        // Selecciona una versión al azar
        Error404DTO selected = versions.get(new Random().nextInt(versions.size()));
        model.addAttribute("errorData", selected);

        // Devuelve la vista personalizada de error 404
        return "error/404";
    }
}

