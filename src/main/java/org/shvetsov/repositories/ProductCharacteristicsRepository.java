package org.shvetsov.repositories;

import org.shvetsov.models.Product;
import org.shvetsov.models.ProductCharacteristics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductCharacteristicsRepository extends JpaRepository<ProductCharacteristics, UUID> {
    ProductCharacteristics findByProduct(Product product);
}
