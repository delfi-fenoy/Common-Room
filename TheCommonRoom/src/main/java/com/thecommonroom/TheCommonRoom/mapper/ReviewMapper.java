package com.thecommonroom.TheCommonRoom.mapper;

import com.thecommonroom.TheCommonRoom.dto.MoviePreviewDTO;
import com.thecommonroom.TheCommonRoom.dto.ReviewRequestDTO;
import com.thecommonroom.TheCommonRoom.dto.ReviewResponseDTO;
import com.thecommonroom.TheCommonRoom.dto.UserPreviewDTO;
import com.thecommonroom.TheCommonRoom.model.Review;
import com.thecommonroom.TheCommonRoom.model.User;

import java.util.List;
import java.util.Map;

public class ReviewMapper {

    public static Review toEntity(ReviewRequestDTO requestDTO, User user){
        return Review.builder()
                .rating(requestDTO.getRating())
                .comment(requestDTO.getComment())
                .movieId(requestDTO.getMovieId())
                .user(user)
                .build();
    }

    public static ReviewResponseDTO entityToResponseDTO(Review review, MoviePreviewDTO moviePreviewDTO, UserPreviewDTO userPreviewDTO){
        return ReviewResponseDTO.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .moviePreview(moviePreviewDTO)
                .userPreview(userPreviewDTO)
                .build();
    }
}
