package org.shvetsov.models.DTO;

import lombok.*;
import org.shvetsov.models.ProductCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRS {
    private UUID id;
    private String name;
    private String description;
    private ProductCategory categories;
    private BigDecimal price;
    private BigDecimal overallRating;
    private UUID creatorId;

    private List<CommentRS> comments;
    private ProductCharacteristicsRS characteristics;
}
