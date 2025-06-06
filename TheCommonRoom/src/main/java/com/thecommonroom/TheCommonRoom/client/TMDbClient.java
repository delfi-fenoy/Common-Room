package com.thecommonroom.TheCommonRoom.client;

import com.thecommonroom.TheCommonRoom.dto.RawMovieDTO;
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
        String url = String.format("%s/movie/%d?api_key=%s", baseUrl, id, key);
        return restTemplate.getForObject(url, RawMovieDTO.class);
    }
}
