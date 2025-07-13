package org.shvetsov.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.shvetsov.models.Gender;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterRQ {
    private String name;
    private String description;
    private String category;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal overallRating;
    private Long creatorId;

    private BigDecimal weight;
    private BigDecimal height;
    private BigDecimal width;
    private String roomType;
    private String type;
    private String size;
    private String material;
    private Gender gender;
    private Double power;
    private int warrantyMonths;
    private Boolean remoteControl;
}
