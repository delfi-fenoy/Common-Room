package com.thecommonroom.TheCommonRoom.dto;

import com.thecommonroom.TheCommonRoom.model.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreviewDTO
{
    ///Envio la informacion necesaria para el front
    private Long id;
    private String username;
    private String profilePictureUrl;
    private Role role;
}
