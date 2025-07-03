package org.shvetsov.repositories;

import org.shvetsov.models.ProductCharacteristics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCharacteristicsRepository extends JpaRepository<ProductCharacteristics, Long> {
}
