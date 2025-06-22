package com.thecommonroom.TheCommonRoom.controller.rest;

import com.thecommonroom.TheCommonRoom.dto.ReviewRequestDTO;
import com.thecommonroom.TheCommonRoom.dto.ReviewResponseDTO;
import com.thecommonroom.TheCommonRoom.dto.ReviewUpdateDTO;
import com.thecommonroom.TheCommonRoom.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

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

    @GetMapping("/users/{username}/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getUserReviews(@PathVariable String username){
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByUsername(username);
        return ResponseEntity.ok(reviews);
    }

    // reviews en peliculas
    @GetMapping("/movies/{movieId}/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByMovie(@PathVariable Long movieId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByMovieId(movieId);
        return ResponseEntity.ok(reviews);
    }

    ///PRINCIPAL representa al usuario logueado

    ///Ver reseñas propias
    @GetMapping("/reviews/mine")
    public ResponseEntity<List<ReviewResponseDTO>> getOwnReviews(Principal principal)
    {
        String username= principal.getName(); //Obtengo el username del usuario logueado
        return ResponseEntity.ok(reviewService.getMyReviews(username));
    }

    ///Modificar reseñas
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable Long reviewId, @RequestBody @Valid ReviewUpdateDTO dto, Principal principal)
    {
        ReviewResponseDTO updated= reviewService.updateReview(reviewId, dto, principal.getName());
        return ResponseEntity.ok(updated);
    }

    ///Eliminar reseña
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id, Principal principal)
    {
        String username= principal.getName();
        reviewService.deleteReview(id,username);
        return ResponseEntity.ok("Reseña eliminada correctamente");
    }
}
