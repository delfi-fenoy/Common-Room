package com.thecommonroom.TheCommonRoom.controller;

import com.thecommonroom.TheCommonRoom.dto.ReviewRequestDTO;
import com.thecommonroom.TheCommonRoom.dto.ReviewResponseDTO;
import com.thecommonroom.TheCommonRoom.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
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
}
