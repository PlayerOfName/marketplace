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

/*    @AfterMapping
    default void mapCharacteristics(ProductAndCharacteristicsRQ productRQ, @MappingTarget Product product, SpecificationMapper mapper) {
        if (productRQ.getCharacteristics() != null) {
            switch (product.getCategories()) {
                case ELECTRONICS -> mapper.toProductElectronicsCharacteristics(productRQ.getCharacteristics()).setProduct(product);
                case CLOTHES -> mapper.toProductClothesCharacteristics(productRQ.getCharacteristics()).setProduct(product);
                case HOUSEHOLD -> mapper.toProductHouseholdCharacteristics(productRQ.getCharacteristics()).setProduct(product);
                case CHANCELLERY -> mapper.toProductChancelleryCharacteristics(productRQ.getCharacteristics()).setProduct(product);
            }
        }
    }*/

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductCharacteristics toProductCharacteristics(ProductCharacteristicsRQ productCharacteristicsRQ);

    @Mapping(target = "id", ignore = true)
    void updateProduct(ProductRQ productRQ, @MappingTarget Product product);

    ProductRS toProductRS(Product product);

}
