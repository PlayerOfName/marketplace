package org.shvetsov.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.shvetsov.models.ProductCharacteristics;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAndCharacteristicsRQ {
    private String name;
    private String description;
    private String categories;
    private Double price;
    private Double overallRating;
    private Long creatorId;
    private ProductCharacteristicsRQ characteristics;
}
