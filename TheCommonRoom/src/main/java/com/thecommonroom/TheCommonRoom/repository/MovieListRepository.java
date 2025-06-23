package com.thecommonroom.TheCommonRoom.repository;

import com.thecommonroom.TheCommonRoom.model.MovieList;
import com.thecommonroom.TheCommonRoom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieListRepository extends JpaRepository<MovieList,Long>
{
    List<MovieList> findByUserUsername(String username);
    List<MovieList> findAllByIsPublicTrue();
    List<MovieList> findByUserIdAndIsPublicTrue(Long userId);
    List<MovieList> findByUser(User user);
}
