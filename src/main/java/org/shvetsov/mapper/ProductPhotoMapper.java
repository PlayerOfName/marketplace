package org.shvetsov.mapper;

import org.mapstruct.Mapper;
import org.shvetsov.models.ProductPhoto;
import org.shvetsov.requestApi.ProductPhotoRQ;

@Mapper(componentModel = "spring")
public interface ProductPhotoMapper {

    ProductPhotoRQ toDto(ProductPhoto photo);

    ProductPhoto toEntity(ProductPhotoRQ dto);
}

