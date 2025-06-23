package com.thecommonroom.TheCommonRoom.mapper;

import com.thecommonroom.TheCommonRoom.dto.MovieListDTO;
import com.thecommonroom.TheCommonRoom.model.MovieList;
import com.thecommonroom.TheCommonRoom.model.User;

public class MovieListMapper
{
    public static MovieListDTO toDTO(MovieList entity)
    {
        return MovieListDTO.builder()
                .id(entity.getId())
                .nameList(entity.getNameList())
                .creationDate(entity.getCreationDate())
                .isPublic(entity.getIsPublic())
                .movies(entity.getMovies())
                .build();
    }

    public static MovieList toEntity(MovieListDTO dto, User user)
    {
        return MovieList.builder()
                .nameList(dto.getNameList())
                .user(user)
                .isPublic(dto.getIsPublic())
                .movies(dto.getMovies())
                .build();
    }
}
