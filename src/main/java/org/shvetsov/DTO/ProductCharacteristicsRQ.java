package org.shvetsov.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCharacteristicsRQ {
    private Double weight;
    private Double height;
    private Double width;

    private Map<String, Object> specification;
}
