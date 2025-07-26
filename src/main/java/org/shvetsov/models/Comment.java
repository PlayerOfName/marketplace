package org.shvetsov.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "comment",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"product_id", "author_id"},
                        name = "uq_comment_product_author"
                )
        })
public class Comment {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @NotNull(message = "Text cannot be null")
    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PreUpdate
    @PrePersist
    public void updateAverageRating() {
        product.setOverallRating(BigDecimal.valueOf(product.getComments().stream().mapToInt(Comment::getRating).average().orElse(0)));
    }
}
