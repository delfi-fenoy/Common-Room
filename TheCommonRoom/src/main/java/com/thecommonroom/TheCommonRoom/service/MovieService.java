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

    // ========== OBTENER TODAS LAS PELÍCULAS ==========

    public List<MoviePreviewDTO> getAllMovies(int page){
        RawMovieListDTO rawList = api.getAllMovies(page);
        // En caso que page > 500, lanza error por parte del cliente
        return MovieMapper.rawToPreviewDTOList(rawList.getResults());
    }

    // ========== OBTENER DETALLES O PREVISUALIZACIÓN DE PELÍCULA ==========

    public MovieDetailsDTO findMovieDetailsById(Long id){
        RawMovieDTO rawMovieDTO = api.getMovieById(id); // Conseguir la pelicula por id
        return MovieMapper.rawToDetailsDTO(rawMovieDTO); // Mapear a dto de detalles de película
        // En caso que no exista, se lanza automáticamente HttpClientErrorException
    }

    public MoviePreviewDTO findMoviePreviewById(Long id){
        RawMovieDTO rawMovieDTO = api.getMovieById(id); // Conseguir la pelicula por id
        return MovieMapper.rawToPreviewDTO(rawMovieDTO); // Mapear a dto de pre-visualización
    }

    // ========== COMPROBACIÓN ==========

    public boolean existsMovieById(Long id){
        try {
            api.getMovieById(id);
            return true; // Si existe (no lanza error), retorna verdadero
        } catch (HttpClientErrorException.NotFound ex){
            return false; // Si no existe (lanza error not found), retorna falso
        }
    }

    // ========== FILTROS ==========

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

    public List<MoviePreviewDTO> getUpcomingMovies(int page){
        RawMovieListDTO rawList = api.getUpcomingMovies(page);
        if(page > rawList.getTotal_pages()) throw new PageOutOfBoundsException("This page does not exist. Max page: " + rawList.getTotal_pages());
        return MovieMapper.rawToPreviewDTOList(rawList.getResults());
    }

    // ========== BÚSQUEDA ==========

    ///  BARRA DE BUSQUEDA | Devuelve una lista de películas, paginadas y con la query
    public RawMovieListDTO searchMovies(String query, int page, int pageSize) {
        RawMovieListDTO rawList = api.searchMovies(query, page, pageSize);
        if (page > rawList.getTotal_pages())
            throw new PageOutOfBoundsException("This page does not exist. Max page: " + rawList.getTotal_pages());
        return rawList;
    }
}
