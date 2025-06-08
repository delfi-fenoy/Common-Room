package com.thecommonroom.TheCommonRoom.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Habilita la configuración de seguridad web personalizada en la aplicación Spring
@EnableMethodSecurity // Permite usar anotaciones de seguridad en métodos para controlar acceso
@RequiredArgsConstructor
public class SecurityConfig {

    // SecurityChain de API REST | Version Original
    /*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                    .requestMatchers("/login", "/logout", "/auth/**", "/users/**", "/movie/**").permitAll() // Rutas públicas
                    .anyRequest().authenticated() // Para las demas rutas se requiere autenticacion
                .and()
                .formLogin()
                    .loginProcessingUrl("/login") // Ruta que recibe POST con username y password
                    // Nombre de los parametros que van a venir en el POST
                    .usernameParameter("username")
                    .passwordParameter("password")
                    // En caso de login exitoso
                    .successHandler((request, response, authentication) -> {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("Successful login");
                    })
                    // En caso de login fallido
                    .failureHandler((request, response, exception) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Incorrect user or password");
                    })
                .and()
                .logout()
                    .logoutUrl("/logout") // Ruta para cerrar sesión
                    // En caso de logout exitoso
                    .logoutSuccessHandler((request, response, authentication) -> {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("Successful logout");
                    });

        return http.build();
    }
     */

    // SecurityChain con HTML | Version Ian :D
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/", "/index", "/index.html", "/signin", "/register",
                        "/css/**", "/js/**", "/img/**",
                        "/movie/**", "/reviews/**" // TODO lo que se ve sin estar logueado
                ).permitAll()
                .requestMatchers(
                        "/profile/**", "/favorites/**", "/like/**", "/comment/**"
                ).authenticated() // Acciones privadas
                .anyRequest().permitAll() // Por si queda algo suelto
                .and()
                .formLogin()
                .loginPage("/signin")
                .loginProcessingUrl("/auth/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/signin?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/signin?logout=true")
                .permitAll();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encriptar contraseña
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Administrar proceso de login
    }
}
