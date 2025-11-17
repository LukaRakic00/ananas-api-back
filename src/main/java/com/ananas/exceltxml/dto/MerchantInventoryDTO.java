package com.ananas.exceltxml.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantInventoryDTO {
    private Integer id;
    private UUID uploadId;
    private String merchantInventoryId;
    private String productName;
    private String status;
    private String l1Category;
    private String productType;
    private String ean;
    private String aCode;
    private String sku;
    private String tags;
    private String warehouse;
    private Integer currentStock;
    private BigDecimal basePriceWithVat;
    private BigDecimal newBasePriceWithVat;
    private BigDecimal vat;
    private BigDecimal newVat;
    private Integer rowNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

