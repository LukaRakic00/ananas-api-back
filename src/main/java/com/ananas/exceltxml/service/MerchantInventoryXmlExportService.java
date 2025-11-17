package com.ananas.exceltxml.service;

import com.ananas.exceltxml.dto.MerchantInventoryDTO;
import com.ananas.exceltxml.entity.MerchantInventory;
import com.ananas.exceltxml.repository.MerchantInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantInventoryXmlExportService {

    private final MerchantInventoryRepository repository;

    public String exportToXml(List<MerchantInventoryDTO> rows) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<merchant_inventory>\n");

        for (MerchantInventoryDTO row : rows) {
            xml.append("  <item>\n");
            xml.append("    <id>").append(row.getId()).append("</id>\n");
            xml.append("    <upload_id>").append(row.getUploadId()).append("</upload_id>\n");
            if (row.getMerchantInventoryId() != null) {
                xml.append("    <merchant_inventory_id>").append(escapeXml(row.getMerchantInventoryId())).append("</merchant_inventory_id>\n");
            }
            xml.append("    <product_name>").append(escapeXml(row.getProductName())).append("</product_name>\n");
            if (row.getStatus() != null) {
                xml.append("    <status>").append(escapeXml(row.getStatus())).append("</status>\n");
            }
            if (row.getL1Category() != null) {
                xml.append("    <l1_category>").append(escapeXml(row.getL1Category())).append("</l1_category>\n");
            }
            if (row.getProductType() != null) {
                xml.append("    <product_type>").append(escapeXml(row.getProductType())).append("</product_type>\n");
            }
            if (row.getEan() != null) {
                xml.append("    <ean>").append(escapeXml(row.getEan())).append("</ean>\n");
            }
            if (row.getACode() != null) {
                xml.append("    <a_code>").append(escapeXml(row.getACode())).append("</a_code>\n");
            }
            if (row.getSku() != null) {
                xml.append("    <sku>").append(escapeXml(row.getSku())).append("</sku>\n");
            }
            if (row.getTags() != null) {
                xml.append("    <tags>").append(escapeXml(row.getTags())).append("</tags>\n");
            }
            if (row.getWarehouse() != null) {
                xml.append("    <warehouse>").append(escapeXml(row.getWarehouse())).append("</warehouse>\n");
            }
            if (row.getCurrentStock() != null) {
                xml.append("    <current_stock>").append(row.getCurrentStock()).append("</current_stock>\n");
            }
            if (row.getBasePriceWithVat() != null) {
                xml.append("    <base_price_with_vat>").append(row.getBasePriceWithVat()).append("</base_price_with_vat>\n");
            }
            if (row.getNewBasePriceWithVat() != null) {
                xml.append("    <new_base_price_with_vat>").append(row.getNewBasePriceWithVat()).append("</new_base_price_with_vat>\n");
            }
            if (row.getVat() != null) {
                xml.append("    <vat>").append(row.getVat()).append("</vat>\n");
            }
            if (row.getNewVat() != null) {
                xml.append("    <new_vat>").append(row.getNewVat()).append("</new_vat>\n");
            }
            if (row.getRowNumber() != null) {
                xml.append("    <row_number>").append(row.getRowNumber()).append("</row_number>\n");
            }
            xml.append("    <created_at>").append(row.getCreatedAt()).append("</created_at>\n");
            if (row.getUpdatedAt() != null) {
                xml.append("    <updated_at>").append(row.getUpdatedAt()).append("</updated_at>\n");
            }
            xml.append("  </item>\n");
        }

        xml.append("</merchant_inventory>");
        return xml.toString();
    }

    public String exportAllToXml(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MerchantInventory> pageResult = repository.findAll(pageable);
        List<MerchantInventoryDTO> rows = pageResult.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return exportToXml(rows);
    }

    public String exportByUploadIdToXml(UUID uploadId) {
        List<MerchantInventory> rows = repository.findByUploadId(uploadId);
        List<MerchantInventoryDTO> rowDTOs = rows.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return exportToXml(rowDTOs);
    }

    private String escapeXml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private MerchantInventoryDTO convertToDTO(MerchantInventory inventory) {
        MerchantInventoryDTO dto = new MerchantInventoryDTO();
        dto.setId(inventory.getId());
        dto.setUploadId(inventory.getUploadId());
        dto.setMerchantInventoryId(inventory.getMerchantInventoryId());
        dto.setProductName(inventory.getProductName());
        dto.setStatus(inventory.getStatus());
        dto.setL1Category(inventory.getL1Category());
        dto.setProductType(inventory.getProductType());
        dto.setEan(inventory.getEan());
        dto.setACode(inventory.getACode());
        dto.setSku(inventory.getSku());
        dto.setTags(inventory.getTags());
        dto.setWarehouse(inventory.getWarehouse());
        dto.setCurrentStock(inventory.getCurrentStock());
        dto.setBasePriceWithVat(inventory.getBasePriceWithVat());
        dto.setNewBasePriceWithVat(inventory.getNewBasePriceWithVat());
        dto.setVat(inventory.getVat());
        dto.setNewVat(inventory.getNewVat());
        dto.setRowNumber(inventory.getRowNumber());
        dto.setCreatedAt(inventory.getCreatedAt());
        dto.setUpdatedAt(inventory.getUpdatedAt());
        return dto;
    }
}

