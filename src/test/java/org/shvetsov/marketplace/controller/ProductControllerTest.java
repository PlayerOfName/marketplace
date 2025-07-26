package org.shvetsov.marketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.shvetsov.controllers.ProductController;
import org.shvetsov.models.Product;
import org.shvetsov.requestApi.FilterRQ;
import org.shvetsov.requestApi.ProductAndCharacteristicsRQ;
import org.shvetsov.requestApi.ProductCharacteristicsRQ;
import org.shvetsov.requestApi.ProductRQ;
import org.shvetsov.responseApi.ProductRS;
import org.shvetsov.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void testCreateProductAndCharacteristics() throws Exception {
        UUID creatorId = UUID.randomUUID();

        ProductAndCharacteristicsRQ request = new ProductAndCharacteristicsRQ();
        request.setName("Product 1");
        request.setDescription("Test Description");
        request.setCategories("ELECTRONICS");
        request.setPrice(new BigDecimal("99.99"));
        request.setCreatorId(creatorId);

        ProductCharacteristicsRQ characteristics = new ProductCharacteristicsRQ();
        characteristics.setWeight(new BigDecimal("1.5"));
        characteristics.setHeight(new BigDecimal("10.0"));
        characteristics.setWidth(new BigDecimal("5.0"));
        characteristics.setSpecification(Map.of("power", 12, "warrantyMonths", 2, "remoteControl", true));

        request.setCharacteristics(characteristics);

        Product mockProduct = new Product();
        mockProduct.setId(UUID.randomUUID());

        Mockito.when(productService.createProductAndCharacteristics(any())).thenReturn(mockProduct);

        mockMvc.perform(post("/product/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockProduct.getId().toString()));
    }


    @Test
    void testDeleteProduct_Success() throws Exception {
        UUID productId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Mockito.when(productService.deleteProduct(eq(productId), eq(userId))).thenReturn(productId);

        mockMvc.perform(delete("/product/delete/{productId}", productId)
                        .header("X-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + productId.toString() + "\""));

    }

    @Test
    void testDeleteProduct_Forbidden() throws Exception {
        UUID productId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Mockito.when(productService.deleteProduct(eq(productId), eq(userId))).thenReturn(null);

        mockMvc.perform(delete("/product/delete/{productId}", productId)
                        .header("X-User-Id", userId))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ProductRQ request = new ProductRQ(); // заполнить при необходимости
        Product mockProduct = new Product();
        mockProduct.setId(productId);

        Mockito.when(productService.updateProduct(eq(productId), any(), eq(userId))).thenReturn(mockProduct);

        mockMvc.perform(patch("/product/update/{id}", productId)
                        .header("X-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()));
    }

    @Test
    void testGetProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductRS productRS = ProductRS.builder().name("Test Product").build();

        Mockito.when(productService.getProductWithDetails(productId)).thenReturn(productRS);

        mockMvc.perform(get("/product/get/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void testGetProductByFilter() throws Exception {
        ProductRS productRS = ProductRS.builder().name("Filtered Product").build();
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(productService.getFilteredProducts(any(FilterRQ.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(productRS)));

        mockMvc.perform(get("/product/getbyfilter")
                        .param("name", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Filtered Product"));
    }
}

