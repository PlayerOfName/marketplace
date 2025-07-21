package org.shvetsov.mapper;

import org.mapstruct.*;
import org.shvetsov.models.*;
import org.shvetsov.requestApi.ProductAndCharacteristicsRQ;
import org.shvetsov.requestApi.ProductCharacteristicsRQ;
import org.shvetsov.requestApi.ProductRQ;
import org.shvetsov.requestApi.ProductRS;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    @Mapping(target = "characteristics", ignore = true)
    Product toProduct(ProductAndCharacteristicsRQ productRQ);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductCharacteristics toProductCharacteristics(ProductCharacteristicsRQ productCharacteristicsRQ);

    @Mapping(target = "id", ignore = true)
    void updateProduct(ProductRQ productRQ, @MappingTarget Product product);

    @Mapping(target = "characteristics", ignore = true)
    ProductRS toProductRS(Product product);
}
