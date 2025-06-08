package com.thecommonroom.TheCommonRoom.config;

import com.thecommonroom.TheCommonRoom.model.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

///  Nos permite inyectar el usuario autenticado en todas las vistas Thymeleaf, sin tener que repetir el código en cada controller.
@Component
@ControllerAdvice
public class GlobalModelAttribute {

    @ModelAttribute
    public void agregarUsuarioLogueado(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            model.addAttribute("usuarioLogueado", userDetails);
        }
    }
}
