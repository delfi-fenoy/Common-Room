package com.thecommonroom.TheCommonRoom.controller;

import com.thecommonroom.TheCommonRoom.dto.UserRequestDTO;
import com.thecommonroom.TheCommonRoom.dto.UserResponseDTO;
import com.thecommonroom.TheCommonRoom.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    // Atributos
    private final UserService userService;

    // Metodos
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody UserRequestDTO userRequestDTO){
        userService.createUser(userRequestDTO);
    }

    @GetMapping("/users/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO getUserByUsername(@PathVariable String username){
        return userService.findUserByUsername(username);
    }
}
