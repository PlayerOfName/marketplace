package org.shvetsov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.shvetsov.requestApi.ProductCharacteristicsRQ;
import org.shvetsov.models.ProductCharacteristics;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CharacteristicsMapper {
    ProductCharacteristics toProductCharacteristics(ProductCharacteristicsRQ productCharacteristicsRQ);
}
