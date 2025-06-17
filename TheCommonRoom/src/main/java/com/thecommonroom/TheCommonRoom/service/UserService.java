package com.thecommonroom.TheCommonRoom.service;

import com.thecommonroom.TheCommonRoom.dto.UserPreviewDTO;
import com.thecommonroom.TheCommonRoom.dto.UserRequestDTO;
import com.thecommonroom.TheCommonRoom.dto.UserResponseDTO;
import com.thecommonroom.TheCommonRoom.exception.EmailAlreadyExistsException;
import com.thecommonroom.TheCommonRoom.exception.NoUsersFoundException;
import com.thecommonroom.TheCommonRoom.exception.UsernameAlreadyExistsException;
import com.thecommonroom.TheCommonRoom.mapper.UserMapper;
import com.thecommonroom.TheCommonRoom.model.CustomUserDetails;
import com.thecommonroom.TheCommonRoom.model.Role;
import com.thecommonroom.TheCommonRoom.model.User;
import com.thecommonroom.TheCommonRoom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserRequestDTO dto){
        // Validar si el username y el mail ya estan siendo usados
        if(userRepository.existsByUsername(dto.getUsername())){
            throw new UsernameAlreadyExistsException("El nombre de usuario " + dto.getUsername() + " ya está en uso.");
        }
        if(userRepository.existsByEmail(dto.getEmail())){
            throw new EmailAlreadyExistsException("El email " + dto.getEmail() + " ya está en uso.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword()); // Encriptar la contraseña
        User user = UserMapper.toEntity(dto, encodedPassword); // Mapear DTO a entidad User
        user.setRole(Role.ROLE_USER); // Settear rol de usuario por deafult

        userRepository.save(user); // Guardar en la base de datos
    }

    // Autentica al usuario al hacer login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
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
        
    public UserResponseDTO findUserByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return UserMapper.toDTO(user);
    }

    public Page<UserPreviewDTO> getUsersPaginated(int page)
    {
        Pageable pageable = PageRequest.of(page,20, Sort.by("id").ascending());

        Page<User> usersPage= userRepository.findAll(pageable);

        return usersPage.map(UserMapper::toPreviewDTO);
    }
}
