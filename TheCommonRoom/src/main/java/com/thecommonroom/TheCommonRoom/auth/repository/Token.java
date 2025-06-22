package com.thecommonroom.TheCommonRoom.auth.repository;

import com.thecommonroom.TheCommonRoom.model.User;
import com.thecommonroom.TheCommonRoom.auth.repository.TokenType;
import jakarta.persistence.*;
import lombok.*;
/**
 * Esta clase representa los datos del token que se guardan en la base de datos.
 */
@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Incremento automático
    private Long id; // Id del token

    @Column(nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER; // Tipo de token

    // Estados actuales
    private boolean revoked; // Si fue revocado (eliminado)
    private boolean expired; // Si expiró

    // Un user puede tener múltiples tokens
    @ManyToOne(fetch = FetchType.LAZY) // Muchos tokens → Un user | LAZY => La entidad User no se carga automáticamente al traer el token
    @JoinColumn(name = "user_id") // Nombre de la columna (foreign key) en la tabla tokens
    private User user; // Usuario al que pertenece
}
