package com.thecommonroom.TheCommonRoom.controller.rest;

import com.thecommonroom.TheCommonRoom.dto.MovieListDTO;
import com.thecommonroom.TheCommonRoom.model.MovieList;
import com.thecommonroom.TheCommonRoom.service.MovieListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Alias;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lists")
public class MovieListController
{
    private final MovieListService movieListService;

    ///Creo lista
    @PostMapping
    public ResponseEntity<MovieListDTO> createList(@RequestBody @Valid MovieListDTO dto, Principal principal)
    {
        MovieListDTO created= movieListService.createList(dto, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    ///Modifica lista
    @PutMapping("{id}")
    public ResponseEntity<MovieListDTO> updateList(@PathVariable Long id, @RequestBody @Valid MovieListDTO dto, Principal principal)
    {
        MovieListDTO updated= movieListService.updateList(id, dto, principal.getName());

        return ResponseEntity.ok(updated);
    }

    ///Borro lista
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteList(@PathVariable Long id, Principal principal)
    {
        movieListService.deleteList(id, principal.getName());
    }

    ///Veo mis listas
    @GetMapping("/mine")
    public ResponseEntity<List<MovieListDTO>> getMyLists(Principal principal)
    {
        List<MovieListDTO> lists= movieListService.getUserLists(principal.getName());

        return ResponseEntity.ok(lists);
    }

    ///Agrego pelicula a lista de favoritos
    @PostMapping("/lists/favorites/{contentId}")
    public ResponseEntity<Void> addToFavorites(@PathVariable Long contentId, Principal principal)
    {
        movieListService.addToFavorites(principal.getName(), contentId);

        return ResponseEntity.ok().build();
    }

    ///Eliminar pelicula de lista de favoritos
    @DeleteMapping("/lists/favorites/{contentId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Long contentId, Principal principal)
    {
        movieListService.removeFromFavorites(principal.getName(), contentId);

        return ResponseEntity.noContent().build();
    }

    ///Agrego pelicula a lista personalizada
    @PostMapping("/lists/{listId}/content/{contentId}")
    public ResponseEntity<Void> addToCustomList(@PathVariable Long listId, @PathVariable Long contentId, Principal principal)
    {
        movieListService.addToCustomList(listId,contentId, principal.getName());

        return ResponseEntity.ok().build();
    }

    ///Elimino pelicula de lista personalizada
    @DeleteMapping("/lists/favorites/{contentId}")
    public ResponseEntity<Void> removeFromCustomList(@PathVariable Long listId, @PathVariable Long contentId, Principal principal)
    {
        movieListService.removeFromCustomList(listId,contentId,principal.getName());

        return ResponseEntity.noContent().build();
    }
}
