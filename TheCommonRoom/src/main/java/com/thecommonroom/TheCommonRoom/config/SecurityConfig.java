package com.thecommonroom.TheCommonRoom.config;

import com.thecommonroom.TheCommonRoom.auth.repository.Token;
import com.thecommonroom.TheCommonRoom.auth.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import com.thecommonroom.TheCommonRoom.auth.config.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Habilita la configuración de seguridad web personalizada en la aplicación Spring
@EnableMethodSecurity // Permite usar anotaciones de seguridad en métodos para controlar acceso
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;
    private final TokenRepository tokenRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Para desarrollo; en producción deberías activarlo
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,
                                "/users/me"
                                ).authenticated()

                        .requestMatchers(HttpMethod.POST,
                                "/reviews"
                                ).authenticated()

                        .requestMatchers(HttpMethod.DELETE,
                                "/users/*",
                                "/reviews/*"
                                ).authenticated()

                        .requestMatchers(HttpMethod.PUT,
                                "/users/**"
                                ).authenticated()

                        .requestMatchers(
                                "/users/*",
                                "/users/*/reviews",
                                "/movies/*/reviews",
                                "/movies/*",
                                "/auth/*",
                                "/",
                                "/index",
                                "/home",
                                "/signin",
                                "/register",
                                "/static/**",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/fragments/**",
                                "/profile/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger-ui/index.html",
                                "/api-docs/**"
                        ).permitAll()

                        .anyRequest().permitAll()
                )
                // No usa sesiones HTTP para almacenar información de autenticación,
                // sino que cada request trae el token para autenticarse
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Indicar el objeto encargado de autenticar el usuario (por username y password)
                .authenticationProvider(authenticationProvider)
                // Agrega el filtro JWT (para validar token) antes de comprobar las credenciales con username y password
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler((request, response, authentication) -> {
                            var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                            logout(authHeader);
                        })
                        .logoutSuccessUrl("/signin?logout=true")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            SecurityContextHolder.clearContext();
                        })
                        .permitAll()
                );
        return http.build();
    }

    private void logout(String token){
        // Si el token esta vacio o no comienza con 'Bearer', error
        if(token == null || !token.startsWith("Bearer ")){
            throw new IllegalArgumentException("Invalid token");
        }

        String jwtToken = token.substring(7); // Obtener token sin 'Bearer '
        Token foundToken = tokenRepository.findByToken(jwtToken) // Obtener token de la bdd
                .orElseThrow(() -> new IllegalArgumentException("Invalid token")); // Error en caso de no enconrarla

        // Settear expiración y revocación
        foundToken.setExpired(true);
        foundToken.setRevoked(true);
        // Guardar cambios en la bdd
        tokenRepository.save(foundToken);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
