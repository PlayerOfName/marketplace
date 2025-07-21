package org.shvetsov.service;

import lombok.RequiredArgsConstructor;
import org.shvetsov.mapper.ProductMapper;
import org.shvetsov.models.Product;
import org.shvetsov.models.ProductCharacteristics;
import org.shvetsov.models.ProductQuerySpecifications;
import org.shvetsov.repositories.ProductCharacteristicsRepository;
import org.shvetsov.repositories.ProductRepository;
import org.shvetsov.requestApi.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductCharacteristicsRepository productCharacteristicsRepository;
    private final ProductRepository productRepository;
    private final ProductCharacteristicsService productCharacteristicsService;
    private final ProductMapper productMapper;

    public Product createProductAndCharacteristics(ProductAndCharacteristicsRQ productRQ) {
/*        Product product = Product.builder()
                .name(productRQ.getName())
                .description(productRQ.getDescription())
                .price(productRQ.getPrice())
                .categories(valueOf(productRQ.getCategories().toUpperCase()))
                .creatorId(productRQ.getCreatorId())
                .build();

        ProductCharacteristics characteristics = switch (product.getCategories()) {
            case ELECTRONICS -> productCharacteristicsService.createElectronicProduct(productRQ.getCharacteristics());
            case CLOTHES -> productCharacteristicsService.createClotheProduct(productRQ.getCharacteristics());
            case HOUSEHOLD -> productCharacteristicsService.createHouseholdProduct(productRQ.getCharacteristics());
            case CHANCELLERY -> productCharacteristicsService.createChancelleryProduct(productRQ.getCharacteristics());
        };

        characteristics.setProduct(product);
        product.setCharacteristics(characteristics);

        productRepository.save(product);
        return product;*/
        Product product = productMapper.toProduct(productRQ);
        ProductCharacteristics characteristics = switch (product.getCategories()) {
            case ELECTRONICS -> productCharacteristicsService.createElectronicProduct(productRQ.getCharacteristics());
            case CLOTHES -> productCharacteristicsService.createClotheProduct(productRQ.getCharacteristics());
            case HOUSEHOLD -> productCharacteristicsService.createHouseholdProduct(productRQ.getCharacteristics());
            case CHANCELLERY -> productCharacteristicsService.createChancelleryProduct(productRQ.getCharacteristics());
        };
        characteristics.setProduct(product);
        product.setCharacteristics(characteristics);
        productRepository.save(product);
        return product;

        // отдольно характеристики, отдельно продукт
    }

    public Product updateProduct(UUID id, ProductRQ productRQ, UUID userId) {
        if (productRepository.findById(id).get().getCreatorId() != userId) {
            throw new RuntimeException("You don't have permission to update this product");
        }
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productMapper.updateProduct(productRQ, product);
        return productRepository.save(product);

    }

/*    public void updateOverallRating(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        List<Comment> comments = product.getComments();
        if (!comments.isEmpty()) {
            double averageRating = comments.stream().mapToInt(Comment::getRating).average().orElse(0.0);
            product.setOverallRating(averageRating);
            productRepository.save(product);
        }

    }*/

    public UUID deleteProduct(UUID productId, UUID userId) {
        if (productRepository.findById(productId).get().getCreatorId() != userId) {
            return null;
        }
        productRepository.deleteById(productId);
        return productId;
    }

    public List<Product> getProductByFilter(FilterRQ filterRQ) {
        List<Product> products = productRepository.findAll();

        return products;
    }

    public ProductRS getProductWithDetails(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
/*        ProductRS productRS = ProductRS.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .categories(product.getCategories())
                .price(product.getPrice())
                .overallRating(product.getOverallRating())
                .creatorId(product.getCreatorId())
                .build();*/
        ProductRS productRS = productMapper.toProductRS(product);

        if (product.getComments() != null) {
            productRS.setComments(product.getComments().stream().map(comment -> CommentRS.builder()
                    .text(comment.getText())
                    .rating(comment.getRating())
                    .build()).toList());
        }

        if (product.getCharacteristics() != null) {
            productRS.setCharacteristics(ProductCharacteristicsRS.builder()
                    .weight(product.getCharacteristics().getWeight())
                    .height(product.getCharacteristics().getHeight())
                    .width(product.getCharacteristics().getWidth())
                    .build());
        }

        return productRS;
        // изначально проверить категорию через switch или if
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

        return productRepository.findAll(spec, pageable)
                .map(product -> productMapper.toProductRS(product));
    }

}
