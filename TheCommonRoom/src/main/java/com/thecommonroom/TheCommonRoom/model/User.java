package com.thecommonroom.TheCommonRoom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp // Se llena el campo automaticamente con la fecha y hora actual al guardar en la base de datos por primera vez
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true) // Puede estár vacía
    private String profilePictureUrl;
}
