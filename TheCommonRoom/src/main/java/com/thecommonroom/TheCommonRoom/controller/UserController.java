package com.thecommonroom.TheCommonRoom.controller;

import com.thecommonroom.TheCommonRoom.dto.UserPreviewDTO;
import com.thecommonroom.TheCommonRoom.dto.UserRequestDTO;
import com.thecommonroom.TheCommonRoom.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    // Atributos
    private final UserService userService;

    // Metodos
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody UserRequestDTO userRequestDTO){
        userService.createUser(userRequestDTO);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserPreviewDTO>> listUsers()
    {
        List<UserPreviewDTO> users= userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
