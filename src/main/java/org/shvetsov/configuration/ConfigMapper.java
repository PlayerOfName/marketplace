package org.shvetsov.configuration;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.shvetsov.DTO.CommentRQ;
import org.shvetsov.DTO.CommentRS;
import org.shvetsov.DTO.ProductCharacteristicsRQ;
import org.shvetsov.DTO.ProductRQ;
import org.shvetsov.main_category.ChancelleryCharacteristics;
import org.shvetsov.main_category.ClothesCharacteristics;
import org.shvetsov.main_category.ElectronicsCharacteristics;
import org.shvetsov.main_category.HouseholdCharacteristics;
import org.shvetsov.models.Comment;
import org.shvetsov.models.Product;
import org.shvetsov.models.ProductCharacteristics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigMapper {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        mapper.createTypeMap(ProductRQ.class, Product.class);
        mapper.createTypeMap(Product.class, ProductRQ.class);
        mapper.createTypeMap(CommentRQ.class, Comment.class);



        return mapper;
    }
}
