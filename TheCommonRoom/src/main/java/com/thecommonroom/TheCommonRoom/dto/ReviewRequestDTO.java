package com.thecommonroom.TheCommonRoom.dto;

import com.thecommonroom.TheCommonRoom.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewRequestDTO {

    @NotNull
    @DecimalMin(value = "0.5")
    @DecimalMax(value = "5")
    private Double rating;

    @Size(max = 700)
    private String comment;

    @NotNull
    private Long movieId;
}
