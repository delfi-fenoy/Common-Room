package com.thecommonroom.TheCommonRoom.mapper;

import com.thecommonroom.TheCommonRoom.dto.UserPreviewDTO;
import com.thecommonroom.TheCommonRoom.dto.UserRequestDTO;
import com.thecommonroom.TheCommonRoom.model.Role;
import com.thecommonroom.TheCommonRoom.dto.UserResponseDTO;
import com.thecommonroom.TheCommonRoom.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toEntity(UserRequestDTO dto, String encodedPassword) {
        return User.builder()
                .username(dto.getUsername())
                .password(encodedPassword)
                .email(dto.getEmail())
                .profilePictureUrl(dto.getProfilePictureUrl())
                .build();
    }

    ///Convierto de un objeto a otro
    ///De un User a un UserPreviewDTO
    public static UserPreviewDTO toPreviewDTO(User user) {
        return UserPreviewDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .profilePictureUrl(user.getProfilePictureUrl())
                .role(Role.valueOf(user.getRole().name()))
                .build();
    }

    ///Convierte una lista de User a una lista de UserPreviewDTO
    public static List<UserPreviewDTO> toPreviewDTOList(List<User> users){
        return users.stream()
                .map(UserMapper::toPreviewDTO)
                .collect(Collectors.toList());
    }
  
    public static UserResponseDTO toDTO(User user){
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }
}
