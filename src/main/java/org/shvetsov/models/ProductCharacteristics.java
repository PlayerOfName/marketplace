package org.shvetsov.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "spec_type")
@Table(name = "product_characteristics")
public class ProductCharacteristics {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    @Column(name = "weight", nullable = false)
    private BigDecimal weight;

    @Column(name = "height", nullable = false)
    private BigDecimal height;

    @Column(name = "width", nullable = false)
    private BigDecimal width;

    @OneToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    public void validate() {
        if (weight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Weight must be greater than 0");
        }
        if (height.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Height must be greater than 0");
        }
        if (width.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Width must be greater than 0");
        }
        if (product == null) {
            throw new IllegalStateException("Product must be specified");
        }
    }
}
