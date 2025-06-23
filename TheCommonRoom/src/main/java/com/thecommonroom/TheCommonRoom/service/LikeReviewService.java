package com.thecommonroom.TheCommonRoom.service;

import com.thecommonroom.TheCommonRoom.exception.ReviewNotFoundException;
import com.thecommonroom.TheCommonRoom.exception.UserNotFoundException;
import com.thecommonroom.TheCommonRoom.model.LikeReview;
import com.thecommonroom.TheCommonRoom.model.Review;
import com.thecommonroom.TheCommonRoom.model.User;
import com.thecommonroom.TheCommonRoom.repository.LikeReviewRepository;
import com.thecommonroom.TheCommonRoom.repository.ReviewRepository;
import com.thecommonroom.TheCommonRoom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeReviewService
{
    private final LikeReviewRepository likeReviewRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    ///Dar like
    public void likeReview(Long reviewId, String username)
    {
        User user= userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException("Usuario no encontrado"));
        Review review= reviewRepository.findById(reviewId).orElseThrow(()->new ReviewNotFoundException("La reseña no existe"));

        if(!likeReviewRepository.existByUserAndReview(user,review))
        {
            LikeReview like= LikeReview.builder()
                    .user(user)
                    .review(review)
                    .build();
            likeReviewRepository.save(like);
        }
    }

    ///Quitar like
    public void unlikeReview(Long reviewId, String username)
    {
        User user= userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException("Usuario no encontrado"));
        Review review= reviewRepository.findById(reviewId).orElseThrow(()->new ReviewNotFoundException("La reseña no existe"));

        likeReviewRepository.findByUserAndReview(user, review).ifPresent(likeReviewRepository::delete);
    }
}
