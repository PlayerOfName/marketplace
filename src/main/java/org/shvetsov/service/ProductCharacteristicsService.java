package org.shvetsov.service;

import org.shvetsov.models.*;
import org.shvetsov.requestApi.ProductCharacteristicsRQ;
import org.springframework.stereotype.Service;

@Service
public class ProductCharacteristicsService {

    public ProductCharacteristics createElectronicProduct(ProductCharacteristicsRQ productCharacteristicsRQ) {
        ElectronicsCharacteristics specification = ElectronicsCharacteristics.builder()
                .width(productCharacteristicsRQ.getWidth())
                .height(productCharacteristicsRQ.getHeight())
                .weight(productCharacteristicsRQ.getWeight())
                .power(Double.valueOf(productCharacteristicsRQ.getSpecification().get("power").toString()))
                .warrantyMonths(Integer.parseInt(productCharacteristicsRQ.getSpecification().get("warrantyMonths").toString()))
                .remoteControl(Boolean.valueOf(productCharacteristicsRQ.getSpecification().get("remoteControl").toString()))
                .build();
/*        specification.setPower(Double.valueOf(productCharacteristicsRQ.getSpecification().get("power").toString()));
        specification.setWarrantyMonths(Integer.parseInt(productCharacteristicsRQ.getSpecification().get("warrantyMonths").toString()));
        specification.setRemoteControl(Boolean.valueOf(productCharacteristicsRQ.getSpecification().get("remoteControl").toString()));*/
        return specification;
    }

    public ProductCharacteristics createClotheProduct(ProductCharacteristicsRQ productCharacteristicsRQ) {
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

    public ProductCharacteristics createChancelleryProduct(ProductCharacteristicsRQ productCharacteristicsRQ) {
        ChancelleryCharacteristics specification = ChancelleryCharacteristics.builder()
                .width(productCharacteristicsRQ.getWidth())
                .height(productCharacteristicsRQ.getHeight())
                .weight(productCharacteristicsRQ.getWeight())
                .type(productCharacteristicsRQ.getSpecification().get("type").toString())
                .build();
        return specification;
    }

    public ProductCharacteristics createHouseholdProduct(ProductCharacteristicsRQ productCharacteristicsRQ) {
        HouseholdCharacteristics specification = HouseholdCharacteristics.builder()
                .width(productCharacteristicsRQ.getWidth())
                .height(productCharacteristicsRQ.getHeight())
                .weight(productCharacteristicsRQ.getWeight())
                .roomType(productCharacteristicsRQ.getSpecification().get("roomType").toString())
                .build();
        return specification;
    }
}
