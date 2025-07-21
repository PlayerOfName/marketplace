package org.shvetsov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.shvetsov.models.*;
import org.shvetsov.requestApi.CharacteristicsRS;
import org.shvetsov.requestApi.ProductCharacteristicsRQ;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CharacteristicsMapper {
    ProductCharacteristics toProductCharacteristics(ProductCharacteristicsRQ productCharacteristicsRQ);

    default CharacteristicsRS toCharacteristicsRS(ProductCharacteristics characteristics) {
        if (characteristics == null) {
            return null;
        }

        CharacteristicsRS.CharacteristicsRSBuilder builder = CharacteristicsRS.builder()
                .weight(characteristics.getWeight())
                .height(characteristics.getHeight())
                .width(characteristics.getWidth());

        if (characteristics instanceof ElectronicsCharacteristics ec) {
            builder
                    .power(ec.getPower())
                    .warrantyMonths(ec.getWarrantyMonths())
                    .remoteControl(ec.getRemoteControl());
        } else if (characteristics instanceof ClothesCharacteristics cc) {
            builder
                    .size(cc.getSize())
                    .material(cc.getMaterial())
                    .gender(cc.getGender().toString());
        } else if (characteristics instanceof HouseholdCharacteristics hc) {
            builder.roomType(hc.getRoomType());
        } else if (characteristics instanceof ChancelleryCharacteristics ch) {
            builder.type(ch.getType());
        }

        return builder.build();
    }
}
