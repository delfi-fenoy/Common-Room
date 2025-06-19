package com.thecommonroom.TheCommonRoom.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Error404DTO {
    private String backgroundImage;
    private String quote;
    private String movie;
}
