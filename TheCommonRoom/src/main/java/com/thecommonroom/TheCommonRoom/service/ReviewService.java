package com.thecommonroom.TheCommonRoom.service;

import com.thecommonroom.TheCommonRoom.dto.*;
import com.thecommonroom.TheCommonRoom.exception.*;
import com.thecommonroom.TheCommonRoom.mapper.ReviewMapper;
import com.thecommonroom.TheCommonRoom.mapper.UserMapper;
import com.thecommonroom.TheCommonRoom.model.Review;
import com.thecommonroom.TheCommonRoom.model.Role;
import com.thecommonroom.TheCommonRoom.model.User;
import com.thecommonroom.TheCommonRoom.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Obtener usuario autenticado
        String username = authentication.getName(); // Obtener username de usuario
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

    public List<ReviewResponseDTO> getReviewsByUsername(String username){
        User foundUser = userService.findUserByUsername(username); // Obtener usuario buscado
        List<Review> entityReviews = reviewRepository.findByUser(foundUser); // Obtener reseñas completas (entidad) de usuario

        List<ReviewResponseDTO> responseReviews = new ArrayList<>(); // Lista de reseñas a devolver

        // Iterar cada reseña de usuario
        for (Review review : entityReviews){
            // Obtener pre-visualización de película reseñada
            MoviePreviewDTO moviePreviewDTO = movieService.findMoviePreviewById(review.getMovieId());
            // Mapearla a dto (reseña + moviePreview)
            responseReviews.add(ReviewMapper.entityToResponseDTO(review, moviePreviewDTO));
        }
        return responseReviews;
    }

    // review en peliculas
    public List<ReviewResponseDTO> getReviewsByMovieId(Long movieId) {
        List<Review> entityReviews = reviewRepository.findByMovieId(movieId);

        List<ReviewResponseDTO> responseReviews = new ArrayList<>();
        for (Review review : entityReviews) {
            UserPreviewDTO userPreview = UserMapper.toPreviewDTO(review.getUser());
            responseReviews.add(ReviewMapper.entityToResponseDTO(review, null, userPreview));
        }
        return responseReviews;
    }

    ///Tengo mis propias reseñas
    public List<ReviewResponseDTO> getMyReviews(String username)
    {
        List<Review> reviews= reviewRepository.findByUsername(username);

        if(reviews.isEmpty())
        {
            throw new ReviewNotFoundException("No tenes reseñas creadas");
        }
        return reviews.stream()
                .map(ReviewMapper::toResponseDTO)
                .toList();
    }

    ///Modificar reseña
    public ReviewResponseDTO updateReview(Long reviewId, ReviewUpdateDTO dto, String username)
    {
        Review review=reviewRepository.findById(reviewId)
                .orElseThrow(()-> new ReviewNotFoundException("La reseña no existe"));

        if(!review.getUser().getUsername().equals(username))
        {
            throw new UnauthorizedReviewAccessException("No tenés permiso para modificar esta reseña");
        }

        review.setComment(dto.getComment());

        reviewRepository.save(review);

        return ReviewMapper.toResponseDTO(review);
    }

    ///Eliminar reseña
    public void deleteReview(Long reviewId, String username)
    {
        Review review= reviewRepository.findById(reviewId)
                .orElseThrow(()->new ReviewNotFoundException("La reseña no existe"));

        boolean isOwner=review.getUser().getUsername().equals(username);
        boolean isAdmin=review.getUser().getRole().equals(Role.DMIN);

        if(!isOwner && !isAdmin)
        {
            throw new UnauthorizedReviewAccessException("No tenés permisos para eliminar esta reseña");
        }

        reviewRepository.delete(review);
    }
}
