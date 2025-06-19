package com.thecommonroom.TheCommonRoom.controller;

import com.thecommonroom.TheCommonRoom.dto.UserPreviewDTO;
import com.thecommonroom.TheCommonRoom.dto.UserResponseDTO;
import com.thecommonroom.TheCommonRoom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    // Atributos
    private final UserService userService;

    // Metodos
    /*@PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody UserRequestDTO userRequestDTO){
        userService.createUser(userRequestDTO);
    }*/

    @GetMapping("/users")
    public ResponseEntity<List<UserPreviewDTO>> listUsers(){
        List<UserPreviewDTO> users= userService.getAllUsers();
        return ResponseEntity.ok(users);
    }  
      
    @GetMapping("/users/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO getUserByUsername(@PathVariable String username){
        return userService.findUserByUsername(username);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody @Valid UserRequestDTO dto)
    {
        userService.updateUser(id, dto);
        return ResponseEntity.ok("Usuario actualizado correctamente");
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id)
    {
        userService.deleteUser(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
}
