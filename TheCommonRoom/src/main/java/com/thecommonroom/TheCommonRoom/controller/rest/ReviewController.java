package com.thecommonroom.TheCommonRoom.controller.rest;

import com.thecommonroom.TheCommonRoom.dto.ReviewRequestDTO;
import com.thecommonroom.TheCommonRoom.dto.ReviewResponseDTO;
import com.thecommonroom.TheCommonRoom.repository.LikeReviewRepository;
import com.thecommonroom.TheCommonRoom.service.LikeReviewService;
import com.thecommonroom.TheCommonRoom.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final LikeReviewService likeReviewService;

    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponseDTO> createReview(@Valid @RequestBody ReviewRequestDTO reviewRequestDTO){
        ReviewResponseDTO reviewResponseDTO = reviewService.createReview(reviewRequestDTO); // Crear reseña

        // Construir la URI donde se puede acceder a la reseña recién creada
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() // Toma la URL actual (/reviews)
                .path("/{id}") // Agrega "/{id}" al final para indicar ruta del nuevo recurso
                .buildAndExpand(reviewResponseDTO.getId()) // Reemplaza {id} por el id de la reseña creada
                .toUri(); // Convierte el resultado a un objeto URI

        return ResponseEntity.created(location).body(reviewResponseDTO); // Devolver código de estado + reseña completa
    }

    // Llamar metodo de UserSecurity, para comprobar permisos
    @PreAuthorize("@userSecurity.canDeleteReview(#reviewId, authentication)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("reviews/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReview(reviewId);
    }

    // Obtener reseñas por usuario
    @GetMapping("/users/{username}/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getUserReviews(@PathVariable String username){
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByUsername(username);
        return ResponseEntity.ok(reviews);
    }

    // Obtener reseñas por película
    @GetMapping("/movies/{id}/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getMovieReviews(@PathVariable Long id){
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByMovieId(id);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/reviews/{id}/like")
    public ResponseEntity<String> likeReview(@PathVariable Long id, Principal principal)
    {
        likeReviewService.likeReview(id, principal.getName());
        return ResponseEntity.ok("Like agregado");
    }

    @DeleteMapping("/reviews/{id}/like")
    public ResponseEntity<String> unliikeReview(@PathVariable Long id, Principal principal)
    {
        likeReviewService.unlikeReview(id, principal.getName());
        return ResponseEntity.ok("Like eliminado");
    }
}
