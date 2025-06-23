package com.thecommonroom.TheCommonRoom.service;

import com.thecommonroom.TheCommonRoom.dto.MovieListDTO;
import com.thecommonroom.TheCommonRoom.exception.*;
import com.thecommonroom.TheCommonRoom.mapper.MovieListMapper;
import com.thecommonroom.TheCommonRoom.model.MovieList;
import com.thecommonroom.TheCommonRoom.repository.MovieListRepository;
import com.thecommonroom.TheCommonRoom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.thecommonroom.TheCommonRoom.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.DuplicateFormatFlagsException;
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

    ///Agregar pelicula a la lista de favoritos
    @Transactional
    public void addToFavorites(String username, Long movieId)
    {
        User user= userRepository.findByUsername(username)
                .orElseThrow(()->new UserNotFoundException("Usuario no encontrado"));

        MovieList favorites= user.getMovieLists().stream()
                .filter(list -> list.getNameList().equalsIgnoreCase("Favorites"))
                .findFirst()
                .orElseThrow(()->new MovieListNotFoundException("Lista de favortios no encontrada"));

        if(favorites.getMovies().contains(movieId))
        {
            throw new DuplicateContentException("La pelicula ya está en favoritos");
        }

        favorites.getMovies().add(movieId);
        movieListRepository.save(favorites);
    }

    ///Elimino pelicila de la lista de favoritos
    @Transactional
    public void removeFromFavorites(String username, Long movieId)
    {
        User user= userRepository.findByUsername(username)
                .orElseThrow(()->new UserNotFoundException("Usuario no encontrado"));

        MovieList favorites = user.getMovieLists().stream()
                .filter(list -> list.getNameList().equalsIgnoreCase("Favorites"))
                .findFirst()
                .orElseThrow(() -> new MovieListNotFoundException("Lista de favoritos no encontrada"));

        if (!favorites.getMovies().contains(movieId))
        {
            throw new ContentNotFoundException("La película no está en la lista");
        }

        favorites.getMovies().remove(movieId);
        movieListRepository.save(favorites);
    }

    ///Agrego pelicula a lista personalizada
    @Transactional
    public void addToCustomList(Long listId, Long movieId, String username)
    {
        MovieList list = movieListRepository.findById(listId)
                .orElseThrow(() -> new MovieListNotFoundException("Lista no encontrada"));

        if (!list.getUser().getUsername().equals(username))
        {
            throw new NoPermissionException("No tenés permisos sobre esta lista");
        }

        if (list.getMovies().contains(movieId))
        {
            throw new DuplicateContentException("La película ya está en la lista");
        }

        list.getMovies().add(movieId);
        movieListRepository.save(list);
    }

    ///Elimino pelicula de lista personalizada
    public void removeFromCustomList(Long listId, Long movieId, String username)
    {
        MovieList list = movieListRepository.findById(listId)
                .orElseThrow(() -> new MovieListNotFoundException("Lista no encontrada"));

        if (!list.getUser().getUsername().equals(username))
        {
            throw new NoPermissionException("No tenés permisos sobre esta lista");
        }

        if (!list.getMovies().contains(movieId))
        {
            throw new ContentNotFoundException("La película no está en la lista");
        }

        list.getMovies().remove(movieId);
        movieListRepository.save(list);
    }
}
