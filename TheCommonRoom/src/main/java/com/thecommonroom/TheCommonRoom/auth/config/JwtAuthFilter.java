package com.thecommonroom.TheCommonRoom.auth.config;

import com.thecommonroom.TheCommonRoom.auth.repository.Token;
import com.thecommonroom.TheCommonRoom.auth.repository.TokenRepository;
import com.thecommonroom.TheCommonRoom.auth.service.JwtService;
import com.thecommonroom.TheCommonRoom.model.User;
import com.thecommonroom.TheCommonRoom.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    // OncePerRequestFilter -> Cada vez que se hace una petición, se ejecuta 'doFilterInternal()'

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
        )throws ServletException, IOException {

        // VALIDAR RUTA
        if(request.getServletPath().contains("/auth")){ // Si la ruta contiene '/auth' (como /auth/login, por ejemplo)
            filterChain.doFilter(request, response); // Se pasa la solicitud al siguiente filtro (fuera del metodo)
            return; // Salimos del metodo
        }

        // CHEQUEAR QUE HAYA TOKEN Y QUE SEA VALIDO
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION); // Obtener header (encabezado) de la solicitud
        // Si el header no existe o no comienza con 'Bearer', entonces puede que no haya token o no está en el formato correcto
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            /* Como no hay token válido, se pasa al siguiente filtro (capaz es un recurso público o se
               manejará el acceso en otro filtro o capa) */
            filterChain.doFilter(request, response);
            return; // Detener ejecución del resto del metodo
        }

        // CHEQUEAR USERNAME DEL TOKEN
        String jwtToken = authHeader.substring(7); // Obtener token sin 'Bearer '
        String username = jwtService.extractUsername(jwtToken); // Obtener username dentro del token, para identificar a quien pertenece
        // Si no se pudo obtener username, o ya existe una autenticación cargada en el contexto
        // de seguridad (para no volver a autenticar al usuario si ya está autenticado)
        if(username == null || SecurityContextHolder.getContext().getAuthentication() != null){
            filterChain.doFilter(request, response);
            return;
        }

        // VERIFICAR ESTADO DEL TOKEN
        Token token = tokenRepository.findByToken(jwtToken) // Obtener token de la base de datos (que coincida con el que vino en el header)
                .orElse(null); // Si no lo encuentra, guarda null
        // Si el token es null (no está en la bdd), está expirado o revocado
        if(token == null || token.isExpired() || token.isRevoked()){
            filterChain.doFilter(request, response); /* Sigue a los siguientes filtros (no lanza error porque
                        en este filtro no se invalidan directamente los tokens expirados o revocados) */
            return;
        }

        // CHEQUEAR USER EN BASE DE DATOS
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username); // Conseguir detalles
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        if(user.isEmpty()){
            filterChain.doFilter(request, response);
            return;
        }

        // VALIDAR TOKEN
        boolean isTokenValid = jwtService.isTokenValid(jwtToken, user.get());
        if(!isTokenValid){
            return; // Si el token NO es valido, no sigue filtrando ni autentica nada
        }

        // AUTENTICAR
        // Si el token es válido, se crea un objeto de autenticación con los datos del usuario
        var authToken = new UsernamePasswordAuthenticationToken(
                userDetails, // Usuario
                null, // No pasamos contraseña porque ya se validó el token
                userDetails.getAuthorities() // Roles o permisos del usuario
        );
        // Agregar información extra al objeto de autenticación, que viene del request (como ip, datos de sesion, etc)
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Se guarda esta autenticación en el contexto de seguridad para que Spring Security la
        // reconozca como usuario autenticado
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response); // Pasa al siguiente filtro de la cadena para que siga procesando la petición
    }


}
