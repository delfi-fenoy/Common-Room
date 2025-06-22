package com.thecommonroom.TheCommonRoom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "movie_id"})) // El user puede tener una unica reseña para la misma película
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    @DecimalMin(value = "0.5") // Validar valor en el back (Bean Validation)
    @DecimalMax(value = "5") // Validar valor en el back (Bean Validation)
    private Double rating;

    @Size(max = 700)
    @Column(columnDefinition = "TEXT") // Para mucho texto
    private String comment;

    @CreationTimestamp
    @Column(nullable = false, updatable = false) // Es obligatorio y no puede actualizarse
    private LocalDateTime createdAt;

    @Column(name = "movie_id", nullable = false) // Nombre de columna en la bdd, es obligatorio
    @NotNull
    private Long movieId;

    @ManyToOne(fetch = FetchType.LAZY) // LAZY => La entidad User no se carga automáticamente al traer la reseña, solo al consultarla
    @JoinColumn(name = "user_id", nullable = false) // Nombre de columna en la bdd, es obligatorio
    private User user;
}
