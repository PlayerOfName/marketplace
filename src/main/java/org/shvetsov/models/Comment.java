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

    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @NotNull(message = "Text cannot be null")
    @Column(name = "text", nullable = false)
    private String text;

    @NotNull(message = "Product cannot be null")
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "AuthorId cannot be null")
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
