package org.shvetsov.service;

import lombok.RequiredArgsConstructor;
import org.shvetsov.mapper.CharacteristicsMapper;
import org.shvetsov.mapper.ProductMapper;
import org.shvetsov.models.Product;
import org.shvetsov.models.ProductCharacteristics;
import org.shvetsov.models.ProductQuerySpecifications;
import org.shvetsov.product.ForbiddenException;
import org.shvetsov.product.NotFoundProductException;
import org.shvetsov.repositories.ProductCharacteristicsRepository;
import org.shvetsov.repositories.ProductRepository;
import org.shvetsov.requestApi.*;
import org.shvetsov.responseApi.CharacteristicsRS;
import org.shvetsov.responseApi.CommentRS;
import org.shvetsov.responseApi.ProductRS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CharacteristicsMapper characteristicsMapper;

    public Product createProductAndCharacteristics(ProductAndCharacteristicsRQ productRQ) {
        Product product = productMapper.toProduct(productRQ);
        ProductCharacteristics characteristics = switch (product.getCategories()) {
            case ELECTRONICS -> characteristicsMapper.toElectronicsCharacteristics(productRQ.getCharacteristics());
            case CLOTHES -> characteristicsMapper.toClotheCharacteristics(productRQ.getCharacteristics());
            case HOUSEHOLD -> characteristicsMapper.toHouseholdCharacteristics(productRQ.getCharacteristics());
            case CHANCELLERY -> characteristicsMapper.toChancelleryCharacteristics(productRQ.getCharacteristics());
        };
        characteristics.setProduct(product);
        product.setCharacteristics(characteristics);
        productRepository.save(product);
        return product;
    }

    public Product updateProduct(UUID id, ProductRQ productRQ, UUID userId) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundProductException("Product not found"));
        if (!product.getCreatorId().equals(userId)) {
            throw new ForbiddenException("You don't have permission to update this product");
        }
        productMapper.updateProduct(productRQ, product);
        return productRepository.save(product);
    }

    public UUID deleteProduct(UUID productId, UUID userId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundProductException("Product not found"));
        if (!product.getCreatorId().equals(userId)) {
            throw new ForbiddenException("You don't have permission to delete this product");
        }
        productRepository.deleteById(productId);
        return productId;
    }

    public ProductRS getProductWithDetails(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundProductException("Product not found"));
        ProductRS productRS = productMapper.toProductRS(product);

        if (product.getComments() != null) {
            productRS.setComments(product.getComments().stream().map(comment -> CommentRS.builder()
                    .text(comment.getText())
                    .rating(comment.getRating())
                    .build()).toList());
        }
        if (product.getCharacteristics() != null) {
            CharacteristicsRS characteristics = characteristicsMapper.toCharacteristicsRS(product.getCharacteristics());
            productRS.setCharacteristics(characteristics);
        }

        return productRS;
    }

    @Transactional(readOnly = true)
    public Page<ProductRS> getFilteredProducts(FilterRQ filter, Pageable pageable) {
        Specification<Product> spec = (root, query, cb) -> null;

        // Базовые фильтры
        if (filter.getName() != null) {
            spec = spec.and(ProductQuerySpecifications.nameContains(filter.getName()));
        }
        if (filter.getMinPrice() != null || filter.getMaxPrice() != null) {
            spec = spec.and(ProductQuerySpecifications.priceBetween(filter.getMinPrice(), filter.getMaxPrice()));
        }
        if (filter.getCategory() != null) {
            spec = spec.and(ProductQuerySpecifications.categoryEquals(filter.getCategory()));
        }

        // Фильтры характеристик
        if (filter.getWeight() != null) {
            spec = spec.and(ProductQuerySpecifications.weightEquals(filter.getWeight()));
        }

        if (filter.getWidth() != null) {
            spec = spec.and(ProductQuerySpecifications.widthEquals(filter.getWidth()));
        }

        if (filter.getHeight() != null) {
            spec = spec.and(ProductQuerySpecifications.heightEquals(filter.getHeight()));
        }

        if (filter.getRoomType() != null) {
            spec = spec.and(ProductQuerySpecifications.hasRoomType(filter.getRoomType()));
        }
        if (filter.getMinPower() != null && filter.getMaxPower() != null) {
            spec = spec.and(ProductQuerySpecifications.powerBetween(filter.getMinPower(), filter.getMaxPower()));
        }
        if (filter.getMinWarrantyMonths() > 0 && filter.getMaxWarrantyMonths() > 0) {
            spec = spec.and(ProductQuerySpecifications.warrantyMonthsBetween(filter.getMinWarrantyMonths(), filter.getMaxWarrantyMonths()));
        }
        if (filter.getRemoteControl() != null) {
            spec = spec.and(ProductQuerySpecifications.remoteControlEquals(filter.getRemoteControl()));
        }
        if (filter.getType() != null) {
            spec = spec.and(ProductQuerySpecifications.hasType(filter.getType()));
        }
        if (filter.getSize() != null) {
            spec = spec.and(ProductQuerySpecifications.hasSize(filter.getSize()));
        }
        if (filter.getMaterial() != null) {
            spec = spec.and(ProductQuerySpecifications.materialEquals(filter.getMaterial()));
        }
        if (filter.getGender() != null) {
            spec = spec.and(ProductQuerySpecifications.genderEquals(filter.getGender()));
        }

        return productRepository.findAll(spec, pageable)
                .map(product -> productMapper.toProductRS(product));
    }
}
