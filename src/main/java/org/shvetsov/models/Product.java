package org.shvetsov.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.shvetsov.product.NotValidOverallRatingException;
import org.shvetsov.product.NotValidPriceException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ProductCategory categories;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "overall_rating")
    private BigDecimal overallRating = BigDecimal.ZERO;

    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductCharacteristics characteristics;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductPhoto> photos = new ArrayList<>();

    public void validate() {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NotValidPriceException("Price must be greater than 0");
        }
        if (overallRating.compareTo(BigDecimal.ZERO) < 0 ||
                overallRating.compareTo(new BigDecimal("5.00")) > 0) {
            throw new NotValidOverallRatingException("Overall rating must be between 0 and 5");
        }
    }
}
