package com.thecommonroom.TheCommonRoom.controller.rest;

import com.thecommonroom.TheCommonRoom.dto.ReviewRequestDTO;
import com.thecommonroom.TheCommonRoom.dto.ReviewResponseDTO;
import com.thecommonroom.TheCommonRoom.dto.ReviewUpdateDTO;
import com.thecommonroom.TheCommonRoom.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(
        name = "Reseñas",
        description = "Endpoints para crear, modificar, eliminar y consultar reseñas de usuarios " +
                "sobre películas."
)
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "Crear una reseña",
            description = "Crea una nueva reseña para una película. El usuario debe estar autenticado."
    )
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
    @Operation(
            summary = "Eliminar una reseña",
            description = "Elimina una reseña existente. Solo puede hacerlo el autor de la " +
                    "reseña o un administrador."
    )
    @PreAuthorize("@userSecurity.canDeleteReview(#reviewId, authentication)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/reviews/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReview(reviewId);
    }

    @Operation(
            summary = "Modificar una reseña",
            description = "Permite modificar el contenido de una reseña existente identificada por su ID."
    )
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> modifyReview(@PathVariable Long reviewId, @Valid @RequestBody ReviewUpdateDTO reviewUpdateDTO){
        ReviewResponseDTO reviewResponseDTO = reviewService.modifyReview(reviewId, reviewUpdateDTO);
        return ResponseEntity.ok(reviewResponseDTO);
    }

    // Obtener reseñas por usuario
    @Operation(
            summary = "Obtener reseñas de un usuario",
            description = "Devuelve una lista con todas las reseñas creadas por un usuario específico, " +
                    "identificado por su nombre de usuario."
    )
    @GetMapping("/users/{username}/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getUserReviews(@PathVariable String username){
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByUsername(username);
        return ResponseEntity.ok(reviews);
    }

    // Obtener reseñas por película
    @Operation(
            summary = "Obtener reseñas de una película",
            description = "Devuelve una lista con todas las reseñas asociadas a una película, " +
                    "identificada por su ID."
    )
    @GetMapping("/movies/{id}/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getMovieReviews(@PathVariable Long id){
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByMovieId(id);
        return ResponseEntity.ok(reviews);
    }
}
