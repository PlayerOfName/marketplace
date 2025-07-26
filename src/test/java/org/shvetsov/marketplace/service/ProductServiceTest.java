package org.shvetsov.marketplace.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shvetsov.mapper.CharacteristicsMapper;
import org.shvetsov.mapper.ProductMapper;
import org.shvetsov.models.ElectronicsCharacteristics;
import org.shvetsov.models.Product;
import org.shvetsov.models.ProductCategory;
import org.shvetsov.product.ForbiddenException;
import org.shvetsov.product.NotFoundProductException;
import org.shvetsov.repositories.ProductCharacteristicsRepository;
import org.shvetsov.repositories.ProductRepository;
import org.shvetsov.requestApi.FilterRQ;
import org.shvetsov.requestApi.ProductAndCharacteristicsRQ;
import org.shvetsov.requestApi.ProductRQ;
import org.shvetsov.responseApi.ProductRS;
import org.shvetsov.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCharacteristicsRepository productCharacteristicsRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CharacteristicsMapper characteristicsMapper;

    @InjectMocks
    private ProductService productService;

    private final UUID TEST_UUID = UUID.randomUUID();
    private final UUID CREATOR_ID = UUID.randomUUID();

    @Test
    void createProductAndCharacteristics_ShouldSaveProductWithCharacteristics() {
        ProductAndCharacteristicsRQ request = new ProductAndCharacteristicsRQ();
        Product product = new Product();
        product.setCategories(ProductCategory.ELECTRONICS); // Устанавливаем категорию
        ElectronicsCharacteristics characteristics = new ElectronicsCharacteristics();

        when(productMapper.toProduct(any())).thenReturn(product);
        when(characteristicsMapper.toElectronicsCharacteristics(any())).thenReturn(characteristics);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProductAndCharacteristics(request);

        assertThat(result).isEqualTo(product);
        verify(productRepository).save(product);
    }

    @Test
    void updateProduct_WhenProductNotFound_ShouldThrowException() {
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(UUID.randomUUID(), new ProductRQ(), UUID.randomUUID()))
                .isInstanceOf(NotFoundProductException.class)
                .hasMessageContaining("Product not found");
    }

    @Test
    void updateProduct_WhenUserIsNotCreator_ShouldThrowForbidden() {
        Product product = new Product();
        product.setCreatorId(UUID.randomUUID()); // Другой пользователь
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> productService.updateProduct(UUID.randomUUID(), new ProductRQ(), UUID.randomUUID()))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("permission");
    }

    @Test
    void deleteProduct_WhenUserIsCreator_ShouldDeleteProduct() {
        Product product = new Product();
        product.setCreatorId(CREATOR_ID);
        when(productRepository.findById(TEST_UUID)).thenReturn(Optional.of(product));

        UUID result = productService.deleteProduct(TEST_UUID, CREATOR_ID);

        assertThat(result).isEqualTo(TEST_UUID);
        verify(productRepository).deleteById(TEST_UUID);
    }

    @Test
    void deleteProduct_WhenProductNotFound_ShouldThrowException() {
        when(productRepository.findById(TEST_UUID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.deleteProduct(TEST_UUID, CREATOR_ID))
                .isInstanceOf(NotFoundProductException.class);
    }

    @Test
    void deleteProduct_WhenUserIsNotCreator_ShouldThrowForbidden() {
        Product product = new Product();
        product.setCreatorId(UUID.randomUUID()); // Другой пользователь
        when(productRepository.findById(TEST_UUID)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> productService.deleteProduct(TEST_UUID, CREATOR_ID))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void getProductWithDetails_ShouldReturnProductWithCommentsAndCharacteristics() {
        Product product = new Product();
        product.setId(TEST_UUID);
        ProductRS expectedRS = new ProductRS();

        when(productRepository.findById(TEST_UUID)).thenReturn(Optional.of(product));
        when(productMapper.toProductRS(product)).thenReturn(expectedRS);

        ProductRS result = productService.getProductWithDetails(TEST_UUID);

        assertThat(result).isEqualTo(expectedRS);
        verify(productMapper).toProductRS(product);
    }

    @Test
    void getProductWithDetails_WhenProductNotFound_ShouldThrowException() {
        when(productRepository.findById(TEST_UUID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductWithDetails(TEST_UUID))
                .isInstanceOf(NotFoundProductException.class);
    }

    @Test
    @Transactional
    void getFilteredProducts_ShouldApplyFiltersAndReturnPagedResults() {
        FilterRQ filter = new FilterRQ();
        filter.setName("test");
        filter.setMinPrice(BigDecimal.valueOf(100));
        filter.setCategory("ELECTRONICS");

        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product();
        Page<Product> productPage = new PageImpl<>(List.of(product));
        ProductRS productRS = new ProductRS();

        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(productPage);
        when(productMapper.toProductRS(product)).thenReturn(productRS);

        Page<ProductRS> result = productService.getFilteredProducts(filter, pageable);

        assertThat(result.getContent()).containsExactly(productRS);
        verify(productRepository).findAll(any(Specification.class), eq(pageable));
    }
}