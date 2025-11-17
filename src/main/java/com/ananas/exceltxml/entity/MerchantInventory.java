package com.ananas.exceltxml.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "merchant_inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "upload_id", nullable = false)
    private UUID uploadId = UUID.randomUUID();

    @Column(name = "merchant_inventory_id", length = 255)
    private String merchantInventoryId;

    @Column(name = "product_name", nullable = false, length = 500)
    private String productName;

    @Column(name = "status", length = 255)
    private String status;

    @Column(name = "l1_category", length = 255)
    private String l1Category;

    @Column(name = "product_type", length = 255)
    private String productType;

    @Column(name = "ean", length = 255)
    private String ean;

    @Column(name = "a_code", length = 255)
    private String aCode;

    @Column(name = "sku", length = 255)
    private String sku;

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags;

    @Column(name = "warehouse", length = 255)
    private String warehouse;

    @Column(name = "current_stock")
    private Integer currentStock;

    @Column(name = "base_price_with_vat", precision = 10, scale = 2)
    private BigDecimal basePriceWithVat;

    @Column(name = "new_base_price_with_vat", precision = 10, scale = 2)
    private BigDecimal newBasePriceWithVat;

    @Column(name = "vat", precision = 5, scale = 2)
    private BigDecimal vat;

    @Column(name = "new_vat", precision = 5, scale = 2)
    private BigDecimal newVat;

    @Column(name = "row_number")
    private Integer rowNumber;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

