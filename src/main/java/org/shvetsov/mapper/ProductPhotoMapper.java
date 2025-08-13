package org.shvetsov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.shvetsov.models.ProductPhoto;
import org.shvetsov.requestApi.ProductPhotoRQ;
import org.shvetsov.requestApi.UploadFileRQ;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public interface ProductPhotoMapper {

    ProductPhotoRQ toDto(ProductPhoto photo);

    ProductPhoto toEntity(ProductPhotoRQ dto);

    default ProductPhoto test(ProductPhotoRQ dto) {
        return ProductPhoto.builder()
                .fileName(dto.getFile().getOriginalFilename())
                .contentType(dto.getFile().getContentType())
                .size(dto.getFile().getSize())
                .build();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "uploadedAt", ignore = true)
    default ProductPhoto toProductPhoto(MultipartFile file){
        return ProductPhoto.builder()
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .build();
    }
}

