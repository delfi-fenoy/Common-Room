package com.thecommonroom.TheCommonRoom.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "likes_reviews", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "review_id"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeReview
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    ///Usuario que da like
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    ///Reseña que tiene el like
    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;
}
