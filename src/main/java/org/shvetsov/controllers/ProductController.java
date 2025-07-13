package org.shvetsov.controllers;

import org.shvetsov.models.DTO.FilterRQ;
import org.shvetsov.models.DTO.ProductAndCharacteristicsRQ;
import org.shvetsov.models.DTO.ProductRQ;
import org.shvetsov.models.DTO.ProductRS;
import org.shvetsov.models.Product;
import org.shvetsov.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRQ productRQ) {
        return ResponseEntity.ok(productService.createProduct(productRQ));
    }

    @PostMapping("/create-characteristics")
    public ResponseEntity<Product> createProductAndCharacteristics(@RequestBody ProductAndCharacteristicsRQ productRQ) {
        return ResponseEntity.ok(productService.createProductAndCharacteristics(productRQ));
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<UUID> deleteProduct(@PathVariable(value = "productId") UUID productId, @RequestHeader("X-User-Id") UUID userId) {
        if (productService.deleteProduct(productId, userId) == null) {
            return ResponseEntity.status(403).build();
        } else {
            return ResponseEntity.ok(productId);
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody ProductRQ productRQ, @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(productService.updateProduct(id, productRQ, userId));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductRS> getProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductWithDetails(id));
    }

    @GetMapping("/getbyfilter")
    public ResponseEntity<List<Product>> getProductByFilter(@RequestParam FilterRQ filter) {
        return ResponseEntity.ok(productService.getProductByFilter(filter));
    }
}
