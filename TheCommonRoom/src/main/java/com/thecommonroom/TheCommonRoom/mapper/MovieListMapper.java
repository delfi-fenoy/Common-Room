package com.thecommonroom.TheCommonRoom.mapper;

import com.thecommonroom.TheCommonRoom.dto.MovieListDTO;
import com.thecommonroom.TheCommonRoom.dto.MovieListPreviewDTO;
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

    public static MovieListPreviewDTO toPreviewDTO(MovieList list)
    {
        return MovieListPreviewDTO.builder()
                .id(list.getId())
                .nameList(list.getNameList())
                .cantidadElementos(list.getMovies() != null ? list.getMovies().size() : 0)
                .build();
    }
}
