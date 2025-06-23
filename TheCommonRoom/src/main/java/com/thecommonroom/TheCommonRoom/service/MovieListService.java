package com.thecommonroom.TheCommonRoom.service;

import com.thecommonroom.TheCommonRoom.dto.MovieListDTO;
import com.thecommonroom.TheCommonRoom.exception.MovieListNotFoundException;
import com.thecommonroom.TheCommonRoom.exception.NoPermissionException;
import com.thecommonroom.TheCommonRoom.exception.UserNotFoundException;
import com.thecommonroom.TheCommonRoom.mapper.MovieListMapper;
import com.thecommonroom.TheCommonRoom.model.MovieList;
import com.thecommonroom.TheCommonRoom.repository.MovieListRepository;
import com.thecommonroom.TheCommonRoom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.thecommonroom.TheCommonRoom.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieListService
{
    private final MovieListRepository movieListRepository;
    private final UserRepository userRepository;

    ///Creo lista
    public MovieListDTO createList(MovieListDTO dto, String username)
    {
        User user= userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException("Usuario no encontrado"));
        MovieList list= MovieListMapper.toEntity(dto, user);
        MovieList saved= movieListRepository.save(list);

        return MovieListMapper.toDTO(saved);
    }

    ///Modifico lista
    public MovieListDTO updateList(Long id, MovieListDTO dto, String username)
    {
        MovieList list= movieListRepository.findById(id).orElseThrow(()->new MovieListNotFoundException("Lista no encontrada"));

        if(!list.getUser().getUsername().equals(username))
        {
            throw new NoPermissionException("No tenes permisos para modificar esta lista");
        }

        list.setNameList(dto.getNameList());
        list.setIsPublic(dto.getIsPublic());
        list.setMovies(dto.getMovies());

        return MovieListMapper.toDTO(movieListRepository.save(list));
    }

    ///Borro lista
    public void deleteList(Long id, String username)
    {
        MovieList list= movieListRepository.findById(id)
                .orElseThrow(()->new MovieListNotFoundException("Lista no encontrada"));

        if(!list.getUser().getUsername().equals(username))
        {
            throw new NoPermissionException("No tenes permiso para eliminar esta lista");
        }

        movieListRepository.delete(list);
    }

    ///Obtengo las listas de usuario
    public List<MovieListDTO> getUserLists(String username)
    {
        return movieListRepository.findByUserUsername(username).stream()
                .map(MovieListMapper::toDTO)
                .toList();
    }
}
