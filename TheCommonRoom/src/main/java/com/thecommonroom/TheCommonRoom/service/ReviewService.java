package com.thecommonroom.TheCommonRoom.service;

import com.thecommonroom.TheCommonRoom.dto.MoviePreviewDTO;
import com.thecommonroom.TheCommonRoom.dto.ReviewRequestDTO;
import com.thecommonroom.TheCommonRoom.dto.ReviewResponseDTO;
import com.thecommonroom.TheCommonRoom.dto.UserPreviewDTO;
import com.thecommonroom.TheCommonRoom.exception.InvalidReviewException;
import com.thecommonroom.TheCommonRoom.exception.MovieNotFoundException;
import com.thecommonroom.TheCommonRoom.exception.ReviewAlreadyExistsException;
import com.thecommonroom.TheCommonRoom.exception.UserNotFoundException;
import com.thecommonroom.TheCommonRoom.mapper.ReviewMapper;
import com.thecommonroom.TheCommonRoom.mapper.UserMapper;
import com.thecommonroom.TheCommonRoom.model.Review;
import com.thecommonroom.TheCommonRoom.model.User;
import com.thecommonroom.TheCommonRoom.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
}
