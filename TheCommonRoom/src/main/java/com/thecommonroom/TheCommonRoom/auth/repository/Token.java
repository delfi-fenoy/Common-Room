package com.thecommonroom.TheCommonRoom.auth.repository;

import com.thecommonroom.TheCommonRoom.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    public enum TokenType{
        BEARER // Tipo de token para otorgar acceso
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Incremento automático
    public Long id;

    @Column(unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    // Estados actuales
    public boolean revoked;
    public boolean expired;

    // Un user puede tener múltiples tokens
    @ManyToOne(fetch = FetchType.LAZY) // Muchos tokens → Un user | LAZY => La entidad User no se carga automáticamente al traer el token
    @JoinColumn(name = "user_id") // Nombre de la columna (foreign key) en la tabla tokens
    public User user;
}
