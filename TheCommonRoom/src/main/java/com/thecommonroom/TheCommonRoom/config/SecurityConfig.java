package com.thecommonroom.TheCommonRoom.config;

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Para desarrollo; en producción deberías activarlo

                .authorizeHttpRequests()
                    .requestMatchers("/", "/index", "/signin", "/register", "/static/**", "/css/**", "/js/**", "/img/**", "/fragments/**")
                    .permitAll()
                    .requestMatchers("/profile/**", "/favorites/**", "/like/**", "/comment/**")
                    .authenticated()
                    .anyRequest()
                    .permitAll()

                .and()
                    .formLogin()
                    .loginPage("/signin")
                    .permitAll()
                    .loginProcessingUrl("/auth/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/home", true)   // Redirige siempre a /home tras login exitoso
                    .failureUrl("/signin?error=true")   // Redirige a /signin con parámetro de error

                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/signin?logout=true")
                    .permitAll();

        return http.build();
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
