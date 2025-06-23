package com.thecommonroom.TheCommonRoom.service;

import com.thecommonroom.TheCommonRoom.auth.service.AuthService;
import com.thecommonroom.TheCommonRoom.dto.UserPreviewDTO;
import com.thecommonroom.TheCommonRoom.dto.UserRequestDTO;
import com.thecommonroom.TheCommonRoom.dto.UserResponseDTO;
import com.thecommonroom.TheCommonRoom.exception.EmailAlreadyExistsException;
import com.thecommonroom.TheCommonRoom.exception.NoUsersFoundException;
import com.thecommonroom.TheCommonRoom.exception.UserNotFoundException;
import com.thecommonroom.TheCommonRoom.exception.UsernameAlreadyExistsException;
import com.thecommonroom.TheCommonRoom.mapper.UserMapper;
import com.thecommonroom.TheCommonRoom.model.Role;
import com.thecommonroom.TheCommonRoom.model.User;
import com.thecommonroom.TheCommonRoom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(UserRequestDTO dto){
        // Validar si el username y el mail ya estan siendo usados
        if(userRepository.existsByUsername(dto.getUsername())){
            throw new UsernameAlreadyExistsException("El nombre de usuario " + dto.getUsername() + " ya está en uso.");
        }
        if(userRepository.existsByEmail(dto.getEmail())){
            throw new EmailAlreadyExistsException("El email " + dto.getEmail() + " ya está en uso.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword()); // Encriptar la contraseña
        User user = UserMapper.toEntity(dto, encodedPassword); // Mapear DTO a entidad User
        user.setRole(Role.USER); // Settear rol de usuario por deafult

        return userRepository.save(user); // Guardar en la base de datos
    }

    ///Obtiene todos los usuarios guardados en la base de datos y si no hay ninguno lanza la exception
    ///Si hay usuarios los convierte en una lista de DTOs
    public List<UserPreviewDTO> getAllUsers(){
        List<User> users = userRepository.findAll();

        if(users.isEmpty()){
            throw new NoUsersFoundException("No registered users found");
        }
        return UserMapper.toPreviewDTOList(users);
    }    

    public UserResponseDTO getUser(String username){
        User user = findUserByUsername(username);
        return UserMapper.toDTO(user);
    }

    public User findUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    //Busco el usuario, verifico si es el mismo, verifico si el username o el emial ya esta usados y luego lo setteo
    public void updateUser(Long id, UserRequestDTO dto) {
        User user=userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Usuario no encontrado"));

        //Con esto me fijo que solo se modifique el mismo
        String loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!user.getUsername().equals(loggedUsername))
        {
            throw new RuntimeException("No tenes permiso para modificar este perfil");
        }

        if(!user.getUsername().equalsIgnoreCase(dto.getUsername())&&
                userRepository.existsByUsername(dto.getUsername()))
        {
            throw new UsernameAlreadyExistsException("El nombre de usuario " + dto.getUsername() + " ya está en uso.");
        }

        if(!user.getEmail().equalsIgnoreCase(dto.getEmail())&&
                userRepository.existsByEmail(dto.getEmail()))
        {
            throw new EmailAlreadyExistsException("El email " + dto.getEmail() + " ya está en uso.");
        }

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setProfilePictureUrl(dto.getProfilePictureUrl());

        userRepository.save(user);
    }

    //Aca borro mi propio usuario yo mismo
    public void deleteUser(Long id) {
        User user= userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Usuario no encontrado"));

        //Verifico que no sea un usuario externo que desee borrar este usuario
        String loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!user.getUsername().equals(loggedUsername))
        {
            throw new RuntimeException("No tenes permiso para eliminar este perfil");
        }

        userRepository.delete(user);
    }

    /*public boolean existsById(Long id){
        return userRepository.existsById(id);
    }*/

    // ========== OBTENER USUARIO ACTUAL ==========
    public User getCurrentUser(){
        Authentication auth = AuthService.getAuthetication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("You must be authenticated"));
    }
}
