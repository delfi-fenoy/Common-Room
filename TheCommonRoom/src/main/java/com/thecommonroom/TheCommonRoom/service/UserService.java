package com.thecommonroom.TheCommonRoom.service;

import com.thecommonroom.TheCommonRoom.auth.dto.TokenResponse;
import com.thecommonroom.TheCommonRoom.auth.service.AuthService;
import com.thecommonroom.TheCommonRoom.auth.service.JwtService;
import com.thecommonroom.TheCommonRoom.dto.*;
import com.thecommonroom.TheCommonRoom.exception.*;
import com.thecommonroom.TheCommonRoom.mapper.UserMapper;
import com.thecommonroom.TheCommonRoom.model.Role;
import com.thecommonroom.TheCommonRoom.model.User;
import com.thecommonroom.TheCommonRoom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // ========== ABM USERS ==========

    @Transactional
    public User createUser(UserRequestDTO dto){
        // Validar si el username y el mail ya estan siendo usados
        validateUsername(dto.getUsername());
        validateEmail(dto.getEmail());

        String encodedPassword = passwordEncoder.encode(dto.getPassword()); // Encriptar la contraseña
        User user = UserMapper.toEntity(dto, encodedPassword); // Mapear DTO a entidad User
        user.setRole(Role.USER); // Settear rol de usuario por deafult

        return userRepository.save(user); // Guardar en la base de datos
    }

    @Transactional
    // Eliminar usuarios (dueño o admin)
    public void deleteUser(String username){
        User user = findUserByUsername(username);
        userRepository.delete(user);
    }

    @Transactional
    public TokenResponse modifyUser(String username, UserUpdateDTO dto){
        User foundUser = findUserByUsername(username); // Obtener user completo

        // Chequear campos a modificar
        boolean usernameChanged = dto.getUsername() != null && !dto.getUsername().isBlank() && !dto.getUsername().equals(username);
        boolean emailChanged = dto.getEmail() != null && !dto.getEmail().equals(foundUser.getEmail());
        boolean descriptionChanged = dto.getDescription() != null && !dto.getDescription().equals(foundUser.getDescription());
        boolean profilePictureChanged = dto.getProfilePictureUrl() != null && !dto.getProfilePictureUrl().equals(foundUser.getProfilePictureUrl());

        // Actualizar campos
        if(usernameChanged ){
            validateUsername(dto.getUsername()); // Chequear que no exista
            foundUser.setUsername(dto.getUsername());
        }
        if(emailChanged){
            validateEmail(dto.getEmail()); // Chequear que no exista
            foundUser.setEmail(dto.getEmail());
        }
        if(descriptionChanged) foundUser.setDescription(dto.getDescription());
        if(profilePictureChanged) foundUser.setProfilePictureUrl(dto.getProfilePictureUrl());

        userRepository.save(foundUser); // Guardar cambios del user

        // Si se cambió el username, se debe generar un nuevo token
        if(usernameChanged){
            jwtService.revokeAllUserTokens(foundUser); // Eliminar tokens antiguos
            String newToken = jwtService.generateToken(foundUser); // Generar nuevos tokens
            String newRefreshToken = jwtService.generateRefreshToken(foundUser);
            jwtService.saveUserToken(foundUser, newToken); // Guardar token nuevo
            return new TokenResponse(newToken, newRefreshToken, foundUser.getUsername(), foundUser.getRole().name());
        }
        return null; // Devolver null en caso que no se haya modificado username
    }

    @Transactional
    public void modifyPassword(String username, PasswordUpdateDTO dto){
        // Obtener user completo
        User currentUser = findUserByUsername(username);

        // Validaciones
        if(!passwordEncoder.matches(dto.getOldPassword(), currentUser.getPassword())) // Validar password antigua
            throw new IncorrectPasswordException("The current password is incorrect.");

        if(!dto.getNewPassword().equals(dto.getConfirmPassword())) // Verificar coincidencia entre passwords nuevas
            throw new InvalidPasswordException("The new password and confirmation password do not match.");

        if(passwordEncoder.matches(dto.getNewPassword(), currentUser.getPassword())) // Password actual debe ser diferente a la nueva
            throw new InvalidPasswordException("The new password must be different from the current password.");

        String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
        currentUser.setPassword(encodedNewPassword);
        userRepository.save(currentUser);
    }

    // ========== OBTENER USUARIOS ==========

    @Transactional(readOnly = true)
    // Obtiene todos los usuarios guardados en la base de datos y si no hay ninguno lanza la exception
    // Si hay usuarios los convierte en una lista de DTOs
    public List<UserPreviewDTO> getAllUsers(){
        List<User> users = userRepository.findAll();
        if(users.isEmpty()){
            throw new NoUsersFoundException("No registered users found");
        }
        return UserMapper.toPreviewDTOList(users);
    }    

    public UserResponseDTO getUserResponse(String username){
        User user = findUserByUsername(username);
        return UserMapper.toResponseDTO(user);
    }

    @Transactional(readOnly = true)
    public User findUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    // ========== OBTENER USUARIO ACTUAL (authorization) ==========

    public User getCurrentUser(){
        Authentication auth = AuthService.getAuthetication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("You must be authenticated"));
    }


    // ========== VALIDACIONES ==========

    public void validateUsername(String username){
        if(userRepository.existsByUsername(username)){
            throw new UsernameAlreadyExistsException
                    ("El nombre de usuario " + username + " ya está en uso.");
        }
    }

    public void validateEmail(String email){
        if(userRepository.existsByEmail(email)){
                throw new EmailAlreadyExistsException
                        ("El email " + email + " ya está en uso.");
        }
    }
}
