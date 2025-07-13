package org.shvetsov.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAndCharacteristicsRQ {
    private String name;
    private String description;
    private String categories;
    private BigDecimal price;
    private Double overallRating;
    private UUID creatorId;
    private ProductCharacteristicsRQ characteristics;
}
