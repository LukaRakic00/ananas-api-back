package com.ananas.exceltxml.repository;

import com.ananas.exceltxml.entity.MerchantInventory;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MerchantInventorySpecifications {

    public static Specification<MerchantInventory> findByFilters(
            String merchantInventoryId,
            String productName,
            String status,
            String l1Category,
            String productType,
            String ean,
            String aCode,
            String sku,
            String tags,
            String warehouse,
            Integer currentStockMin,
            Integer currentStockMax,
            BigDecimal basePriceWithVatMin,
            BigDecimal basePriceWithVatMax,
            BigDecimal newBasePriceWithVatMin,
            BigDecimal newBasePriceWithVatMax,
            BigDecimal vatMin,
            BigDecimal vatMax,
            BigDecimal newVatMin,
            BigDecimal newVatMax) {
        
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (merchantInventoryId != null && !merchantInventoryId.trim().isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.get("merchantInventoryId")),
                    "%" + merchantInventoryId.toLowerCase() + "%"
                ));
            }

            if (productName != null && !productName.trim().isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.get("productName")),
                    "%" + productName.toLowerCase() + "%"
                ));
            }

            if (status != null && !status.trim().isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.get("status")),
                    "%" + status.toLowerCase() + "%"
                ));
            }

            if (l1Category != null && !l1Category.trim().isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.get("l1Category")),
                    "%" + l1Category.toLowerCase() + "%"
                ));
            }

            if (productType != null && !productType.trim().isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.get("productType")),
                    "%" + productType.toLowerCase() + "%"
                ));
            }

            if (ean != null && !ean.trim().isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.get("ean")),
                    "%" + ean.toLowerCase() + "%"
                ));
            }

            if (aCode != null && !aCode.trim().isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.get("aCode")),
                    "%" + aCode.toLowerCase() + "%"
                ));
            }

            if (sku != null && !sku.trim().isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.get("sku")),
                    "%" + sku.toLowerCase() + "%"
                ));
            }

            if (tags != null && !tags.trim().isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.get("tags")),
                    "%" + tags.toLowerCase() + "%"
                ));
            }

            if (warehouse != null && !warehouse.trim().isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.get("warehouse")),
                    "%" + warehouse.toLowerCase() + "%"
                ));
            }

            if (currentStockMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("currentStock"), currentStockMin));
            }

            if (currentStockMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("currentStock"), currentStockMax));
            }

            if (basePriceWithVatMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("basePriceWithVat"), basePriceWithVatMin));
            }

            if (basePriceWithVatMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("basePriceWithVat"), basePriceWithVatMax));
            }

            if (newBasePriceWithVatMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("newBasePriceWithVat"), newBasePriceWithVatMin));
            }

            if (newBasePriceWithVatMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("newBasePriceWithVat"), newBasePriceWithVatMax));
            }

            if (vatMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("vat"), vatMin));
            }

            if (vatMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("vat"), vatMax));
            }

            if (newVatMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("newVat"), newVatMin));
            }

            if (newVatMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("newVat"), newVatMax));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

