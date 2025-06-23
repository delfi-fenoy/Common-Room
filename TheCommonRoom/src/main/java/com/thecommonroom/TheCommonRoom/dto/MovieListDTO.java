package com.thecommonroom.TheCommonRoom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.text.StyledEditorKit;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieListDTO
{
    private Long id;

    @NotBlank(message = "El nombre de la lista no puede estar vacío")
    private String nameList;

    private LocalDateTime creationDate;

    private Boolean isPublic;

    private List<Long> movies;
}
