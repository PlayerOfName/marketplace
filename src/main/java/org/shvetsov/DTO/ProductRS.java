package org.shvetsov.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.shvetsov.main_category.ProductCategory;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRS {
    private String name;
    private String description;
    private ProductCategory categories;
    private Double price;
    private Double overallRating;
    private Long creatorId;
}
