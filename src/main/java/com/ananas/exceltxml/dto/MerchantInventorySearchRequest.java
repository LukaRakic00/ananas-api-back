package com.ananas.exceltxml.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantInventorySearchRequest {
    private String search;
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
    private Integer currentStockMin;
    private Integer currentStockMax;
    private BigDecimal basePriceWithVatMin;
    private BigDecimal basePriceWithVatMax;
    private BigDecimal newBasePriceWithVatMin;
    private BigDecimal newBasePriceWithVatMax;
    private BigDecimal vatMin;
    private BigDecimal vatMax;
    private BigDecimal newVatMin;
    private BigDecimal newVatMax;
    private int page = 0;
    private int size = 20;
}

