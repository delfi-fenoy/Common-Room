package com.thecommonroom.TheCommonRoom.repository;

import com.thecommonroom.TheCommonRoom.model.LikeReview;
import com.thecommonroom.TheCommonRoom.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import com.thecommonroom.TheCommonRoom.model.User;

import java.util.Optional;

public interface LikeReviewRepository extends JpaRepository<LikeReview, Long>
{
    Optional<LikeReview> findByUserAndReview(User user, Review review);
    void deleteByUserAndReview(User user, Review review);
    boolean existByUserAndReview(User user, Review review);
    int countByReview(Review review);
}
