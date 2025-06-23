package com.thecommonroom.TheCommonRoom.service;

import com.thecommonroom.TheCommonRoom.client.TMDbClient;
import com.thecommonroom.TheCommonRoom.dto.MovieDetailsDTO;
import com.thecommonroom.TheCommonRoom.dto.MoviePreviewDTO;
import com.thecommonroom.TheCommonRoom.dto.RawMovieDTO;
import com.thecommonroom.TheCommonRoom.dto.RawMovieListDTO;
import com.thecommonroom.TheCommonRoom.exception.PageOutOfBoundsException;
import com.thecommonroom.TheCommonRoom.mapper.MovieMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final TMDbClient api;

    public MovieDetailsDTO findMovieDetailsById(Long id){
        RawMovieDTO rawMovieDTO = api.getMovieById(id); // Conseguir la pelicula por id
        return MovieMapper.rawToDetailsDTO(rawMovieDTO); // Mapear a dto de detalles de película
        // En caso que no exista, se lanza automáticamente HttpClientErrorException
    }

    public MoviePreviewDTO findMoviePreviewById(Long id){
        RawMovieDTO rawMovieDTO = api.getMovieById(id); // Conseguir la pelicula por id
        return MovieMapper.rawToPreviewDTO(rawMovieDTO); // Mapear a dto de pre-visualización
    }

    public boolean existsMovieById(Long id){
        try {
            api.getMovieById(id);
            return true; // Si existe (no lanza error), retorna verdadero
        } catch (HttpClientErrorException.NotFound ex){
            return false; // Si no existe (lanza error not found), retorna falso
        }
    }

    ///  PAGINACION DE PELICULAS | Devuelve una lista de películas populares, paginadas
    public List<MoviePreviewDTO> getPopularMovies(int page) {
        if (page > 3) throw new PageOutOfBoundsException("This page does not exist");
        RawMovieListDTO rawList = api.getPopularMovies(page);
        return MovieMapper.rawToPreviewDTOList(rawList.getResults());
    }

    public List<MoviePreviewDTO> getRecentMovies(int page) {
        if (page > 3) throw new PageOutOfBoundsException("This page does not exist");
        RawMovieListDTO rawList = api.getRecentMovies(page);
        return MovieMapper.rawToPreviewDTOList(rawList.getResults());
    }

    public List<MoviePreviewDTO> getAllMovies(int page){
        RawMovieListDTO rawList = api.getAllMovies(page);
        // En caso que page > 500, lanza error por parte del cliente
        return MovieMapper.rawToPreviewDTOList(rawList.getResults());
    }

    public List<MoviePreviewDTO> getUncomingMovies(int page){
        RawMovieListDTO rawList = api.getUncomingMovies(page);
        if(page > rawList.getTotal_pages()) throw new PageOutOfBoundsException("This page does not exist. Max page: " + rawList.getTotal_pages());
        return MovieMapper.rawToPreviewDTOList(rawList.getResults());
    }

    ///  BARRA DE BUSQUEDA | Devuelve una lista de películas, paginadas y con la query
    public RawMovieListDTO searchMovies(String query, int page, int pageSize) {
        RawMovieListDTO rawList = api.searchMovies(query, page, pageSize);
        if (page > rawList.getTotal_pages())
            throw new PageOutOfBoundsException("This page does not exist. Max page: " + rawList.getTotal_pages());
        return rawList;
    }

    ///Filtrado de peliculas
    public List<MoviePreviewDTO> filterMovies(String title, Integer year, String genere, Double puntuacionMinima, int page)
    {
        RawMovieListDTO rawList = api.getAllMovies(page); //Trae una página de películas
        List<RawMovieDTO> results = rawList.getResults();

        return results.stream()
                .filter(movie -> title == null || movie.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(movie -> year == null || (movie.getRelease_date() != null && movie.getRelease_date().startsWith(year.toString())))
                .filter(movie -> puntuacionMinima == null || movie.getVote_average() >= puntuacionMinima)
                .filter(movie -> genere == null || movie.getGenres().stream()
                        .map(RawMovieDTO.GenreDTO::getName) // sacar el nombre del género
                        .anyMatch(name -> name.equalsIgnoreCase(genere)))
                .map(MovieMapper::rawToPreviewDTO)
                .toList();
    }
}
