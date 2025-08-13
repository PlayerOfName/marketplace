package org.shvetsov.models;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
public class ProductQuerySpecifications {

    // Базовые спецификации
    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return buildRangeSpecification("price", minPrice, maxPrice);
    }

    public static Specification<Product> categoryEquals(String category) {
        return buildEqualSpecification("categories", category);
    }

    // Спецификации для характеристик
    public static Specification<Product> weightEquals(BigDecimal weight) {
        return buildCharacteristicSpecification("weight", weight, null);
    }

    public static Specification<Product> heightEquals(BigDecimal height) {
        return buildCharacteristicSpecification("height", height, null);
    }

    public static Specification<Product> widthEquals(BigDecimal width) {
        return buildCharacteristicSpecification("width", width, null);
    }

    public static Specification<Product> hasRoomType(String roomType) {
        return buildCharacteristicSpecification("room_type", roomType, HouseholdCharacteristics.class);
    }

    public static Specification<Product> hasType(String type) {
        return buildCharacteristicSpecification("type", type, ChancelleryCharacteristics.class);
    }

    public static Specification<Product> powerBetween(Double minPower, Double maxPower) {
        return buildCharacteristicRangeSpecification("power", minPower, maxPower, ElectronicsCharacteristics.class);
    }

    public static Specification<Product> hasSize(String size) {
        return buildCharacteristicSpecification("size", size, ClothesCharacteristics.class);
    }

    public static Specification<Product> genderEquals(String gender) {
        return buildCharacteristicSpecification("gender", gender, ClothesCharacteristics.class);
    }

    public static Specification<Product> materialEquals(String material) {
        return buildCharacteristicSpecification("material", material, ClothesCharacteristics.class);
    }

    public static Specification<Product> warrantyMonthsBetween(int minWarrantyMonths, int maxWarrantyMonths) {
        return buildCharacteristicRangeSpecification("warrantyMonths",
                minWarrantyMonths, maxWarrantyMonths, ElectronicsCharacteristics.class);
    }

    public static Specification<Product> remoteControlEquals(Boolean remoteControl) {
        return buildCharacteristicSpecification("remote_control", remoteControl, ElectronicsCharacteristics.class);
    }

    // Общие методы построения спецификаций
    private static <T> Specification<Product> buildEqualSpecification(String field, T value) {
        return (root, query, cb) -> value == null ? null : cb.equal(root.get(field), value);
    }

    private static <T extends Comparable<? super T>> Specification<Product> buildRangeSpecification(
            String field, T min, T max) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (min != null) predicates.add(cb.greaterThanOrEqualTo(root.get(field), min));
            if (max != null) predicates.add(cb.lessThanOrEqualTo(root.get(field), max));
            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static <T> Specification<Product> buildCharacteristicSpecification(
            String field, T value, Class<? extends ProductCharacteristics> characteristicType) {
        return (root, query, cb) -> {
            if (value == null) return null;
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);
            Predicate fieldPredicate = cb.equal(specs.get(field), value);
            return characteristicType == null
                    ? fieldPredicate
                    : cb.and(fieldPredicate, cb.equal(specs.type(), characteristicType));
        };
    }

    private static <T extends Comparable<? super T>> Specification<Product> buildCharacteristicRangeSpecification(
            String field, T min, T max, Class<? extends ProductCharacteristics> characteristicType) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Product, ProductCharacteristics> specs = root.join("characteristics", JoinType.LEFT);

            if (characteristicType != null) {
                predicates.add(cb.equal(specs.type(), characteristicType));
            }
            if (min != null) predicates.add(cb.greaterThanOrEqualTo(specs.get(field), min));
            if (max != null) predicates.add(cb.lessThanOrEqualTo(specs.get(field), max));

            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
