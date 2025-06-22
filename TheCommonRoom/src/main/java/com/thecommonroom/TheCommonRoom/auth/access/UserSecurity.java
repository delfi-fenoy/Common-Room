package com.thecommonroom.TheCommonRoom.auth.access;

import com.thecommonroom.TheCommonRoom.model.Review;
import com.thecommonroom.TheCommonRoom.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Esta clase se encarga de validar permisos de acceso a ciertos recursos.
 *
 * Los métodos se llaman desde las anotaciones @PreAuthorize en los controladores, para comprobar
 * si el usuario autenticado tiene autorización para realizar determinadas acciones.
 */
@RequiredArgsConstructor
@Component("userSecurity")
public class UserSecurity {

    private final ReviewService reviewService;

    // Un usuario puede eliminar una reseña si es el dueño de ella, o es un administrador
    public boolean canDeleteReview(Long reviewId, Authentication authentication){
        Review review = reviewService.getReviewById(reviewId); // Obtener reseña por su id
        String username = authentication.getName(); // Obtener user autenticado

        // Devolver true si puede eliminar reseña, o false en caso contrario
        return review.getUser().getUsername().equals(username) // La reseña debe pertenecer al usuario autenticado
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")); // O el usuairo debe ser admin
    }
}
