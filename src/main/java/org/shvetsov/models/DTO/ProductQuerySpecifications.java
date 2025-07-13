package org.shvetsov.models.DTO;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.shvetsov.models.ElectronicsCharacteristics;
import org.shvetsov.models.HouseholdCharacteristics;
import org.shvetsov.models.Product;
import org.shvetsov.models.ProductCharacteristics;
import org.springframework.data.jpa.domain.Specification;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductQuerySpecifications {

    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Product> categoryEquals(String category) {
        return (root, query, cb) ->
                category == null ? null : cb.equal(root.get("category"), category);
    }

    public static Specification<Product> weightBetween(BigDecimal minWeight, BigDecimal maxWeight) {
        return (root, query, cb) -> {
            if (minWeight == null && maxWeight == null) return null;
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);
            List<Predicate> predicates = new ArrayList<>();
            if (minWeight != null) {
                predicates.add(cb.greaterThanOrEqualTo(specs.get("weight"), minWeight));
            }
            if (maxWeight != null) {
                predicates.add(cb.lessThanOrEqualTo(specs.get("weight"), maxWeight));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Product> weightEquals(BigDecimal weight) {
        return (root, query, cb) -> {
            if (weight == null) return null;
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);
            return cb.and(
                    cb.equal(specs.type(), HouseholdCharacteristics.class),
                    cb.equal(specs.get("weight"), weight)
            );
        };
    }

    public static Specification<Product> heightEquals(BigDecimal height) {
        return (root, query, cb) -> {
            if (height == null) return null;
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);
            return cb.and(
                    cb.equal(specs.type(), HouseholdCharacteristics.class),
                    cb.equal(specs.get("height"), height)
            );
        };
    }

    public static Specification<Product> widthEquals(BigDecimal width) {
        return (root, query, cb) -> {
            if (width == null) return null;
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);
            return cb.and(
                    cb.equal(specs.type(), HouseholdCharacteristics.class),
                    cb.equal(specs.get("width"), width)
            );
        };
    }

    public static Specification<Product> hasRoomType(String roomType) {
        return (root, query, cb) -> {
            if (roomType == null) return null;
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);
            return cb.and(
                    cb.equal(specs.type(), HouseholdCharacteristics.class),
                    cb.equal(specs.get("roomType"), roomType)
            );
        };
    }

    public static Specification<Product> hasPowerGreaterThan(Double minPower) {
        return (root, query, cb) -> {
            if (minPower == null) return null;
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);
            return cb.and(
                    cb.equal(specs.type(), ElectronicsCharacteristics.class),
                    cb.greaterThanOrEqualTo(specs.get("power"), minPower)
            );
        };
    }

    // Аналогичные методы для других типов характеристик
}
