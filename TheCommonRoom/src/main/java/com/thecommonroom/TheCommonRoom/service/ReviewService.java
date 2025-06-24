package com.thecommonroom.TheCommonRoom.service;

import com.thecommonroom.TheCommonRoom.dto.*;
import com.thecommonroom.TheCommonRoom.exception.*;
import com.thecommonroom.TheCommonRoom.mapper.ReviewMapper;
import com.thecommonroom.TheCommonRoom.mapper.UserMapper;
import com.thecommonroom.TheCommonRoom.model.Review;
import com.thecommonroom.TheCommonRoom.model.User;
import com.thecommonroom.TheCommonRoom.repository.ReviewRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final MovieService movieService;

    // ========== ABM REVIEWS ==========

    @Transactional
    public ReviewResponseDTO createReview(ReviewRequestDTO reviewRequestDTO){

        // Obtener el usuario actual
        User currentUser = userService.getCurrentUser();

        // Validaciones
        validateMovieExists(reviewRequestDTO.getMovieId());
        validateUserHasNotReviewedMovie(currentUser.getId(), reviewRequestDTO.getMovieId());
        validateReview(reviewRequestDTO.getRating(), reviewRequestDTO.getComment()); // Validar rating y comment

        // Guardar reseña en la base de datos, mapeandola a su entidad
        Review review = reviewRepository.save(ReviewMapper.toEntity(reviewRequestDTO, currentUser));

        // Devolver response de reseña
        MoviePreviewDTO moviePreviewDTO = movieService.findMoviePreviewById(review.getMovieId()); // Obtener pre-visualización de película
        UserPreviewDTO userPreviewDTO = UserMapper.toPreviewDTO(currentUser); // Obtener pre-visualización de user
        return ReviewMapper.entityToResponseDTO(review, moviePreviewDTO, userPreviewDTO); // Mapear reseña a responseDTO
    }

    @Transactional
    public void deleteReview(Long reviewId){
        Review review = getReviewById(reviewId);
        reviewRepository.delete(review);
    }

    @Transactional
    public ReviewResponseDTO modifyReview(Long reviewId, ReviewUpdateDTO reviewUpdateDTO){
        // Obtener review antigua completa y usuario autenticado
        Review review = getReviewById(reviewId);
        User currentUser = userService.getCurrentUser();

        // Comprobar que la reseña a modificar pertenezca al usuario autenticado
        if(!review.getUser().equals(currentUser))
            throw new AccessDeniedException("You are not allowed to edit this review");

        // Validar rating y comment
        validateReview(reviewUpdateDTO.getRating(), reviewUpdateDTO.getComment());

        // Settear valores y guardar en bdd
        if(reviewUpdateDTO.getRating() != null){
            review.setRating(reviewUpdateDTO.getRating());
        }
        if(reviewUpdateDTO.getComment() != null){
            review.setComment(reviewUpdateDTO.getComment());
        }
        reviewRepository.save(review);

        // Devolver response de review
        MoviePreviewDTO moviePreviewDTO = movieService.findMoviePreviewById(review.getMovieId());
        return ReviewMapper.entityToResponseDTO(review,
                                            moviePreviewDTO,
                                            UserMapper.toPreviewDTO(review.getUser()));
    }

    // ========== OBTENER REVIEWS ==========

    @Transactional(readOnly = true) // Para mayor rendimiento
    public List<ReviewResponseDTO> getReviewsByUsername(String username){
        User foundUser = userService.findUserByUsername(username); // Obtener usuario buscado
        List<Review> entityReviews = reviewRepository.findByUser(foundUser); // Obtener reseñas completas (entidad) de usuario

        List<ReviewResponseDTO> responseReviews = new ArrayList<>(); // Lista de reseñas a devolver

        // Iterar cada reseña de usuario
        for (Review review : entityReviews){
            // Obtener pre-visualización de película reseñada
            MoviePreviewDTO moviePreviewDTO = movieService.findMoviePreviewById(review.getMovieId());
            UserPreviewDTO userPreviewDTO = UserMapper.toPreviewDTO(foundUser);
            // Mapearla a dto (reseña + moviePreview + userPreview)
            responseReviews.add(
                    ReviewMapper.entityToResponseDTO(
                            review,
                            moviePreviewDTO,
                            userPreviewDTO)); // (front necesita estos datos)
        }
        return responseReviews;
    }

    @Transactional(readOnly = true)
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

    // ========== VALIDACIONES ==========

    public void validateMovieExists(Long movieId){
        // Comprobar existencia de película
        if(!movieService.existsMovieById(movieId))
            throw new MovieNotFoundException("Movie does not exist");
    }

    @Transactional(readOnly = true)
    public void validateUserHasNotReviewedMovie(Long userId, Long movieId){
        // Comprobar que el usuario no haya reseñado esta película anteriormente
        if(reviewRepository.findByUserIdAndMovieId(userId, movieId)
                .isPresent())
            throw new ReviewAlreadyExistsException("User has already reviewed this movie");
    }

    public void validateReview(Double rating, String comment){
        // Comprobar que el rating sea múltiplo válido de 0.5 (0.5, 1, 1.5, etc)
        if(rating != null && rating % 0.5 != 0)
            throw new InvalidReviewException("Rating must be a multiple of 0.5 between 0.5 and 5");

        // Si se incluye un comentario (opcional), chequear que no sean solo espacios en blanco
        if(comment != null && comment.isBlank())
            throw new InvalidReviewException("Comment cannot contain only whitespace");
    }
}
