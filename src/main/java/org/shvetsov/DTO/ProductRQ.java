package org.shvetsov.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRQ {
    private String name;
    private String description;
    private String categories;
    private Double price;
    private Long creatorId;
}
