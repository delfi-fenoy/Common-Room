package com.thecommonroom.TheCommonRoom.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieListPreviewDTO
{
    private Long id;
    private String nameList;
    private int cantidadElementos;
}
