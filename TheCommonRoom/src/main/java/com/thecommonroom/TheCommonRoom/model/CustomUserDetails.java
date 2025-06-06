package com.thecommonroom.TheCommonRoom.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

/**
 * Implementación personalizada de UserDetails para Spring Security,
 * que adapta la clase User del sistema a los requerimientos del framework.
 * <p>
 * Proporciona la información de usuario necesaria para la autenticación y autorización,
 * incluyendo roles, credenciales y estado de la cuenta.
 * Además, ofrece métodos útiles para mostrar datos en las vistas (Thymeleaf|Front).
 */

@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(user.getRole()::name);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // login por username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Métodos útiles para Thymeleaf
    public String getProfilePictureUrl() {
        return user.getProfilePictureUrl();
    }

    public String getNombre() {
        return user.getUsername(); // O cambiar por otro si tenés campo "nombre"
    }
}
