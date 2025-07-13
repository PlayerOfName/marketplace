package org.shvetsov.models.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCharacteristicsRS {
    private BigDecimal weight;
    private BigDecimal height;
    private BigDecimal width;

}
