package com.thecommonroom.TheCommonRoom.config;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@Component // Marca esta clase como un componente de Spring para que se registre automáticamente
public class CustomErrorViewResolver implements ErrorViewResolver {

    // Metodo que define cómo se resuelven las vistas de error personalizadas
    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        // Si el error es 404 (página no encontrada), redirige a una vista personalizada
        /*if (status == HttpStatus.NOT_FOUND) {
            return new ModelAndView("redirect:/error/404");
        }*/
        return null;
    }
}