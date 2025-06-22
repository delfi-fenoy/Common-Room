package com.thecommonroom.TheCommonRoom.service;

import com.thecommonroom.TheCommonRoom.dto.MoviePreviewDTO;
import com.thecommonroom.TheCommonRoom.dto.ReviewRequestDTO;
import com.thecommonroom.TheCommonRoom.dto.ReviewResponseDTO;
import com.thecommonroom.TheCommonRoom.dto.UserPreviewDTO;
import com.thecommonroom.TheCommonRoom.exception.*;
import com.thecommonroom.TheCommonRoom.mapper.ReviewMapper;
import com.thecommonroom.TheCommonRoom.mapper.UserMapper;
import com.thecommonroom.TheCommonRoom.model.Review;
import com.thecommonroom.TheCommonRoom.model.User;
import com.thecommonroom.TheCommonRoom.repository.ReviewRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final MovieService movieService;

    public ReviewResponseDTO createReview(ReviewRequestDTO reviewRequestDTO){

        // Obtener el usuario actual
        String username = SecurityContextHolder.getContext().getAuthentication().getName(); // Obtener username de usuario autenticado (actual)
        User currentUser = userService.findUserByUsername(username); // Obtener usuario completo

        // Comprobar existencia de película
        if(!movieService.existsMovieById(reviewRequestDTO.getMovieId()))
            throw new MovieNotFoundException("Movie does not exist");

        // Comprobar que el usuario no haya reseñado esta película anteriormente
        if(reviewRepository.findByUserIdAndMovieId(currentUser.getId(), reviewRequestDTO.getMovieId())
                .isPresent())
            throw new ReviewAlreadyExistsException("User has already reviewed this movie");

        // Comprobar que el rating sea múltiplo válido de 0.5 (0.5, 1, 1.5, etc)
        if(reviewRequestDTO.getRating() % 0.5 != 0)
            throw new InvalidReviewException("Rating must be a multiple of 0.5 between 0.5 and 5");

        // Si se incluye un comentario (opcional), chequear que no sean solo espacios en blanco
        if(reviewRequestDTO.getComment() != null && reviewRequestDTO.getComment().isBlank())
            throw new InvalidReviewException("Comment cannot contain only whitespace");

        // Guardar reseña en la base de datos, mapeandola a su entidad
        Review review = reviewRepository.save(ReviewMapper.toEntity(reviewRequestDTO, currentUser));

        // Devolver response de reseña
        MoviePreviewDTO moviePreviewDTO = movieService.findMoviePreviewById(review.getMovieId()); // Obtener pre-visualización de película
        UserPreviewDTO userPreviewDTO = UserMapper.toPreviewDTO(currentUser); // Obtener pre-visualización de user
        return ReviewMapper.entityToResponseDTO(review, moviePreviewDTO, userPreviewDTO); // Mapear reseña a responseDTO
    }

    public void deleteReview(Long reviewId){
        Review review = getReviewById(reviewId);
        reviewRepository.delete(review);
    }

    public List<ReviewResponseDTO> getReviewsByUsername(String username){
        User foundUser = userService.findUserByUsername(username); // Obtener usuario buscado
        List<Review> entityReviews = reviewRepository.findByUser(foundUser); // Obtener reseñas completas (entidad) de usuario

        List<ReviewResponseDTO> responseReviews = new ArrayList<>(); // Lista de reseñas a devolver

        // Iterar cada reseña de usuario
        for (Review review : entityReviews){
            // Obtener pre-visualización de película reseñada
            MoviePreviewDTO moviePreviewDTO = movieService.findMoviePreviewById(review.getMovieId());
            // Mapearla a dto (reseña + moviePreview) (user null, para menor redundancia)
            responseReviews.add(ReviewMapper.entityToResponseDTO(review, moviePreviewDTO, null));
        }
        return responseReviews;
    }

    public List<ReviewResponseDTO> getReviewsByMovieId(Long movieId){
        List<Review> entityReviews = reviewRepository.findByMovieId(movieId); // Obtener reseñas completas de película

        return entityReviews.stream()
                .map(review ->
                        ReviewMapper.entityToResponseDTO( // Mapear reseñas a ReviewResponseDTO para visualización
                                review, // Pasar la reseña
                                null, // Movie null, para menor redundancia (es siempre la misma)
                                UserMapper.toPreviewDTO(review.getUser()) // Mapear user a su preview, y pasar
                        ))
                .toList();
    }

    @Transactional(readOnly = true) // Operación solo de lectura
    public Review getReviewById(Long reviewId){
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review does not exist"));
    }
}
