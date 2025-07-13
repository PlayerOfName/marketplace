package org.shvetsov.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCharacteristicsRQ {
    private BigDecimal weight;
    private BigDecimal height;
    private BigDecimal width;

    private Map<String, Object> specification;
}
