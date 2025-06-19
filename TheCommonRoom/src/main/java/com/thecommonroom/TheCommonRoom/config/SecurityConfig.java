package com.thecommonroom.TheCommonRoom.config;

import com.thecommonroom.TheCommonRoom.model.User;
import com.thecommonroom.TheCommonRoom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Habilita la configuración de seguridad web personalizada en la aplicación Spring
@EnableMethodSecurity // Permite usar anotaciones de seguridad en métodos para controlar acceso
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Para desarrollo; en producción deberías activarlo

                .authorizeHttpRequests()
                    .requestMatchers("/", "/auth/**", "/index", "/signin", "/register", "/static/**", "/css/**", "/js/**", "/img/**", "/fragments/**")
                    .permitAll()
                    .requestMatchers("/profile/**", "/favorites/**", "/like/**", "/comment/**")
                    .authenticated()
                    .anyRequest()
                    .permitAll()

                /*.and()
                    .formLogin()
                    .loginPage("/signin")
                    .permitAll()
                    .loginProcessingUrl("/auth/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/home", true)   // Redirige siempre a /home tras login exitoso
                    .failureUrl("/signin?error=true")   // Redirige a /signin con parámetro de error*/

                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/signin?logout=true")
                    .permitAll();

        return http.build();
    }

    // Verificar que el username exista
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            // Busca al usuario por su nombre de usuario (username) en la base de datos
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found")); // Lanzar excepción en caso de no encontralo (Spring lo maneja automáticamente)

            // Construir un objeto de tipo UserDetails con los datos del usuario
            // Spring usa este objeto internamente para verificar credenciales
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build();
        };
    }

    // Verificar username y password al hacer login
    @Bean
    public AuthenticationProvider authenticationProvider(){
        // Crear un proveedor de autenticación que Spring va a usar para verificar usuarios y contraseñas
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        // Configura el servicio que carga los datos del usuario (UserDetailsService)
        authenticationProvider.setUserDetailsService(userDetailsService());

        // Configura el codificador de contraseñas para validar la contraseña ingresada
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        // Devuelve el proveedor configurado para su uso en el proceso de autenticación
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encriptación de contraseña
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
