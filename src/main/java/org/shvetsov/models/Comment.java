package org.shvetsov.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "author_id", unique = true)
    private UUID authorId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PreUpdate
    @PrePersist
    public void updateAverageRating() {
        product.setOverallRating(BigDecimal.valueOf(product.getComments().stream().mapToInt(Comment::getRating).average().orElse(0)));
    }

}
