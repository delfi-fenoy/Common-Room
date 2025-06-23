package com.thecommonroom.TheCommonRoom.model;

import com.thecommonroom.TheCommonRoom.auth.repository.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Incremento automático
    private Long id;

    @NotBlank(message = "El username no debe estar vacío")
    @Column(unique = true, nullable = false)
    @Size(min = 5, max = 20, message = "El username debe tener entre 5 y 20 caracteres")
    private String username;

    @NotBlank(message = "La contraseña no debe estar vacía")
    @Column(nullable = false)
    @Size(min = 8, message = "La contraseña debe tener como mínimo 8 caracteres")
    private String password;

    @Email(message = "El email debe tener un formato válido")
    @NotBlank(message = "El email no debe estár vacío")
    @Size(max = 50, message = "El email debe tener como máximo 50 caracteres")
    @Column(unique = true, nullable = false)
    private String email;

    @Size(max = 255, message = "La descripción debe tener como máximo 255 caracteres")
    @Column(length = 255)
    private String description;

    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp // Se llena el campo automaticamente con la fecha y hora actual al guardar en la base de datos por primera vez
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @URL(message = "La URL de la foto de perfil debe ser valida")
    @Column(nullable = true) // Puede estár vacía
    private String profilePictureUrl;

    // Tokens del usuario (un user puede tener muchos tokens)
    /*  • "user" => La relación está mapeada por el atributo user (en entidad Token)
        • FetchType.LAZY => Los tokens no se cargan automáticamente al traer al user, solo al consultarlos
        • CascadeType.REMOVE => Al eliminar un usuario, se propaga la operación a los tokens que le pertenecen
        • orphanRemoval (true) => Si se borra un token de la lista, ese token también se elimina de la bdd */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Token> tokens;

    // Un user puede tener muchas reseñas
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviews;
}
