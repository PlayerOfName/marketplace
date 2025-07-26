package org.shvetsov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.shvetsov.models.*;
import org.shvetsov.requestApi.ProductCharacteristicsRQ;
import org.shvetsov.responseApi.CharacteristicsRS;

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

    default ElectronicsCharacteristics toElectronicsCharacteristics(ProductCharacteristicsRQ productCharacteristicsRQ){
        ElectronicsCharacteristics specification = ElectronicsCharacteristics.builder()
                .width(productCharacteristicsRQ.getWidth())
                .height(productCharacteristicsRQ.getHeight())
                .weight(productCharacteristicsRQ.getWeight())
                .power(Double.valueOf(productCharacteristicsRQ.getSpecification().get("power").toString()))
                .warrantyMonths(Integer.parseInt(productCharacteristicsRQ.getSpecification().get("warrantyMonths").toString()))
                .remoteControl(Boolean.valueOf(productCharacteristicsRQ.getSpecification().get("remoteControl").toString()))
                .build();
        return specification;
    }

    default ClothesCharacteristics toClotheCharacteristics(ProductCharacteristicsRQ productCharacteristicsRQ){
        ClothesCharacteristics specification = ClothesCharacteristics.builder()
                .width(productCharacteristicsRQ.getWidth())
                .height(productCharacteristicsRQ.getHeight())
                .weight(productCharacteristicsRQ.getWeight())
                .size(productCharacteristicsRQ.getSpecification().get("size").toString())
                .material(productCharacteristicsRQ.getSpecification().get("material").toString())
                .gender(Gender.valueOf(productCharacteristicsRQ.getSpecification().get("gender").toString().toUpperCase()))
                .build();
        return specification;
    }

    default ChancelleryCharacteristics toChancelleryCharacteristics(ProductCharacteristicsRQ productCharacteristicsRQ){
        ChancelleryCharacteristics specification = ChancelleryCharacteristics.builder()
                .width(productCharacteristicsRQ.getWidth())
                .height(productCharacteristicsRQ.getHeight())
                .weight(productCharacteristicsRQ.getWeight())
                .type(productCharacteristicsRQ.getSpecification().get("type").toString())
                .build();
        return specification;
    }

    default HouseholdCharacteristics toHouseholdCharacteristics(ProductCharacteristicsRQ productCharacteristicsRQ){
        HouseholdCharacteristics specification = HouseholdCharacteristics.builder()
                .width(productCharacteristicsRQ.getWidth())
                .height(productCharacteristicsRQ.getHeight())
                .weight(productCharacteristicsRQ.getWeight())
                .roomType(productCharacteristicsRQ.getSpecification().get("roomType").toString())
                .build();
        return specification;
    }
}
