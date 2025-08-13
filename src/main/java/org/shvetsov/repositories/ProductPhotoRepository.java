package org.shvetsov.repositories;

import org.shvetsov.models.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, UUID> {
}
