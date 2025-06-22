package com.thecommonroom.TheCommonRoom.controller.view;

import com.thecommonroom.TheCommonRoom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controlador para renderizar la vista del perfil de un usuario
 */
@Controller
@RequiredArgsConstructor
public class UserViewController {

    // =========== Atributos =========== \\
    private final UserService userService;

    // =========== Perfil de usuario =========== \\
    @GetMapping("/profile/{username}")
    public String getUserProfile(@PathVariable String username, Model model) {
        var user = userService.findUserByUsername(username);
        model.addAttribute("user", user);
        return "user-profile";
    }
}
