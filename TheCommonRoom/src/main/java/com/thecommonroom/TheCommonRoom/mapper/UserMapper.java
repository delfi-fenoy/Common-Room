package com.thecommonroom.TheCommonRoom.mapper;

import com.thecommonroom.TheCommonRoom.dto.UserRequestDTO;
import com.thecommonroom.TheCommonRoom.model.User;

public class UserMapper {

    public static User toEntity(UserRequestDTO dto, String encodedPassword){
        return User.builder()
                .username(dto.getUsername())
                .password(encodedPassword)
                .email(dto.getEmail())
                .profilePictureUrl(dto.getProfilePictureUrl())
                .build();
    }
}
