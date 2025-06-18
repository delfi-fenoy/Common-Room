package com.thecommonroom.TheCommonRoom.client;

import com.thecommonroom.TheCommonRoom.dto.RawMovieDTO;
import com.thecommonroom.TheCommonRoom.dto.RawMovieListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Clase encargada de conectarse a la API The Movie Database y hacer los requests.
 */
@Component
@RequiredArgsConstructor
public class TMDbClient {

    // Inyecta el valor de la propiedad "themoviedb.api.key" definida en el application.properties
    @Value("${themoviedb.api.key}")
    private String key;

    @Value("${themoviedb.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate; // Clase de Spring para hacer peticiones HTTP

    public RawMovieDTO getMovieById(Long id){
        String url = String.format("%s/movie/%d?api_key=%s&language=es-AR", baseUrl, id, key);
        return restTemplate.getForObject(url, RawMovieDTO.class);
    }

    ///  PAGINACION DE PELICULAS | Llama a la API de TMDb y devuelve películas populares (paginadas)
    public RawMovieListDTO getPopularMovies(int page) {
        String url = String.format("%s/movie/popular?api_key=%s&page=%d&language=es-AR", baseUrl, key, page);
        return restTemplate.getForObject(url, RawMovieListDTO.class);
    }

    public RawMovieListDTO getRecentMovies(int page) {
        String url = String.format("%s/movie/now_playing?api_key=%s&page=%d&language=es-AR&region=AR", baseUrl, key, page);
        return restTemplate.getForObject(url, RawMovieListDTO.class);
    }

    public RawMovieListDTO getAllMovies(int page){
        String url = String.format("%s/discover/movie?api_key=%s" +
                                    "&page=%d" +
                                    "&language=es-AR" +
                                    "&region=AR" +
                                    "&sort_by=popularity.desc", baseUrl, key, page);
        return restTemplate.getForObject(url, RawMovieListDTO.class);
    }

    public RawMovieListDTO getUncomingMovies(int page){
        String url = String.format("%s/movie/upcoming?api_key=%s" +
                                    "&page=%d" +
                                    "&language=es-AR" +
                                    "&region=AR", baseUrl, key, page);
        return restTemplate.getForObject(url, RawMovieListDTO.class);
    }

    ///  BARRA DE BUSQUEDA
    public RawMovieListDTO searchMovies(String query, int page, int pageSize) {
        String url = String.format(
                "%s/search/movie?api_key=%s&query=%s&page=%d&language=es-AR",
                baseUrl, key, query, page
        );
        return restTemplate.getForObject(url, RawMovieListDTO.class);
    }

}
