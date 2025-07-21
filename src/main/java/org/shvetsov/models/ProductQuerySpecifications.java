package org.shvetsov.models;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
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
                category == null ? null : cb.equal(root.get("categories"), category);
    }

    public static Specification<Product> weightEquals(BigDecimal weight) {
        return (root, query, cb) -> {
            if (weight == null) return null;
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);
            return cb.equal(specs.get("weight"), weight);
        };
    }

    public static Specification<Product> heightEquals(BigDecimal height) {
        return (root, query, cb) -> {
            if (height == null) return null;
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);
            return cb.equal(specs.get("height"), height);
        };
    }

    public static Specification<Product> widthEquals(BigDecimal width) {
        return (root, query, cb) -> {
            if (width == null) return null;
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);
            return cb.equal(specs.get("width"), width);
        };
    }

    public static Specification<Product> hasRoomType(String roomType) {
        return (root, query, cb) -> {
            if (roomType == null) return null;
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);
            return cb.and(
                    cb.equal(specs.type(), HouseholdCharacteristics.class),
                    cb.equal(specs.get("room_type"), roomType)
            );
        };
    }

    public static Specification<Product> hasType(String type) {
        return (root, query, cb) -> {
            if (type == null) return null;
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);
            return cb.and(
                    cb.equal(specs.type(), ChancelleryCharacteristics.class),
                    cb.equal(specs.get("type"), type)
            );
        };
    }

    public static Specification<Product> powerBetween(Double minPower, Double maxPower) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("characteristics").get("type"), ElectronicsCharacteristics.class));
            if (minPower != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("power"), minPower));
            }
            if (maxPower != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("power"), maxPower));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Product> hasSize(String size) {
        return (root, query, cb) -> {
            if (size == null) return null;
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);
            return cb.and(
                    cb.equal(specs.type(), ClothesCharacteristics.class),
                    cb.equal(specs.get("size"), size)
            );
        };
    }

    public static Specification<Product> genderEquals(String gender) {
        return (root, query, cb) -> {
            if (gender == null) return null;
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);
            return cb.and(
                    cb.equal(specs.type(), ClothesCharacteristics.class),
                    cb.equal(specs.get("gender"), gender)
            );
        };
    }

    public static Specification<Product> materialEquals(String material) {
        return (root, query, cb) -> {
            if (material == null) return null;
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);
            return cb.and(
                    cb.equal(specs.type(), ClothesCharacteristics.class),
                    cb.equal(specs.get("material"), material)
            );
        };
    }

    public static Specification<Product> warrantyMonthsBetween(int minWarrantyMonths, int maxWarrantyMonths) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("characteristics").get("type"), ElectronicsCharacteristics.class));
            predicates.add(cb.greaterThanOrEqualTo(root.get("warrantyMonths"), minWarrantyMonths));
            predicates.add(cb.lessThanOrEqualTo(root.get("warrantyMonths"), maxWarrantyMonths));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Product> remoteControlEquals(Boolean remoteControl) {
        return (root, query, cb) -> {
            if (remoteControl == null) return null;
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);
            return cb.and(
                    cb.equal(specs.type(), ElectronicsCharacteristics.class),
                    cb.equal(specs.get("remote_control"), remoteControl)
            );
        };
    }


    // Сделать общий предикат для характеристик !

}
