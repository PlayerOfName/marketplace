package org.shvetsov.repositories;

import org.shvetsov.models.Product;
import org.shvetsov.models.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, UUID> {
    Integer countByProduct(Product product);
    Optional<ProductPhoto> findByProductAndPosition(Product product, Integer position);
    List<ProductPhoto> findByProductAndPositionGreaterThanOrderByPositionAsc(Product product, Integer deletedPosition);
    List<ProductPhoto> findByProductIdOrderByPositionAsc(UUID productId);
    Optional<ProductPhoto> findFirstByProductIdOrderByPositionAsc(UUID productId);
    Optional<ProductPhoto> findByFileName(String fileName);
}
