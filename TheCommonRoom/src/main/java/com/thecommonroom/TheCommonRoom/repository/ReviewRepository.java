package com.thecommonroom.TheCommonRoom.repository;

import com.thecommonroom.TheCommonRoom.model.Review;
import com.thecommonroom.TheCommonRoom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserIdAndMovieId(Long userId, Long movieId);
    List<Review> findByUser(User user);
    List<Review> findByMovieId(Long movieId);
    List<Review> findByUsername(String username);
}
