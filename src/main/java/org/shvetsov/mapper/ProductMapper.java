package org.shvetsov.mapper;

import org.mapstruct.*;
import org.shvetsov.models.DTO.ProductRQ;
import org.shvetsov.models.DTO.ProductRS;
import org.shvetsov.models.Product;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    Product toProduct(ProductRQ productRQ);
    ProductRS toProductRS(Product product);

    @Mapping(target = "id", ignore = true)
    void updateProduct(ProductRQ productRQ,@MappingTarget Product product);
}
