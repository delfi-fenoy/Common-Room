package com.thecommonroom.TheCommonRoom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "lists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieList
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la lista no puede estar vacío")
    private String nameList;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creationDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Boolean isPublic;

    @ElementCollection
    private List<Long> movies= new ArrayList<>();
}
