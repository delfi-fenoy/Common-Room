package com.thecommonroom.TheCommonRoom.auth.config;

import com.thecommonroom.TheCommonRoom.model.User;
import com.thecommonroom.TheCommonRoom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserRepository userRepository;

    // Este metodo le dice a Spring cómo buscar un usuario en la base de datos
    @Bean
    public UserDetailsService userDetailsService(){ // Interfaz con un solo metodo -> loadUserByUsername(String username) (que devuelve un UserDetails)
        return username -> { // Funcion lambda
            // Busca al usuario por su nombre de usuario (username) en la base de datos
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found")); // Lanzar excepción en caso de no encontralo (Spring lo maneja automáticamente)

            // Si se encuentra, se convierte ese usuario en un objeto que Spring Security pueda usar
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword()) // Contraseña ya cifrada
                    .roles(user.getRole().name()) // USER o ADMIN
                    .build(); // Construye el objeto
        };
    }

    // Verificar username y password al hacer login (se llama automaticamente al intentar hacer login)
    @Bean
    public AuthenticationProvider authenticationProvider(){
        // Crea un proveedor de autenticación que Spring va a usar para verificar usuarios y contraseñas
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        // Configura el servicio que carga los datos del usuario (UserDetailsService)
        authenticationProvider.setUserDetailsService(userDetailsService());

        // Configura el codificador de contraseñas para validar la contraseña ingresada
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        // Devuelve el proveedor configurado para su uso en el proceso de autenticación
        return authenticationProvider;
    }

    // Define cómo se encriptan las contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt recomendado por Spring
    }
}
