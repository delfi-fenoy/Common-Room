package com.thecommonroom.TheCommonRoom.dto;

import lombok.*;

/**
 * Este DTO mostrará la información en el listado de películas (título, póster, etc).
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MoviePreviewDTO {

    private Long id;
    private String title;
    private String synopsis;
    private String releaseDate;
    private String posterUrl;
}
