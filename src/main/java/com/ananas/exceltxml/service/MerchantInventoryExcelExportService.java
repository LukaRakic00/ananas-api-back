package com.ananas.exceltxml.service;

import com.ananas.exceltxml.dto.MerchantInventoryDTO;
import com.ananas.exceltxml.entity.MerchantInventory;
import com.ananas.exceltxml.repository.MerchantInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantInventoryExcelExportService {

    private final MerchantInventoryRepository repository;

    public byte[] exportToExcel(List<MerchantInventoryDTO> rows) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Merchant Inventory");

        // Stilovi
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        // Header red (A1)
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "MerchantInventoryId", "Product name", "Status", "L1 Category", 
            "Product type", "EAN", "a code", "SKU", "Tags", "Warehouse", 
            "Current stock", "Base price with VAT", "New base price with VAT", 
            "VAT", "New VAT"
        };
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Prazan red (A2)
        sheet.createRow(1);

        // Podaci počinju od reda 3 (A3)
        int rowNum = 2;
        for (MerchantInventoryDTO row : rows) {
            Row dataRow = sheet.createRow(rowNum);
            
            createCell(dataRow, 0, row.getMerchantInventoryId(), dataStyle);
            createCell(dataRow, 1, row.getProductName(), dataStyle);
            createCell(dataRow, 2, row.getStatus(), dataStyle);
            createCell(dataRow, 3, row.getL1Category(), dataStyle);
            createCell(dataRow, 4, row.getProductType(), dataStyle);
            createCell(dataRow, 5, row.getEan(), dataStyle);
            createCell(dataRow, 6, row.getACode(), dataStyle);
            createCell(dataRow, 7, row.getSku(), dataStyle);
            createCell(dataRow, 8, row.getTags(), dataStyle);
            createCell(dataRow, 9, row.getWarehouse(), dataStyle);
            createNumericCell(dataRow, 10, row.getCurrentStock(), dataStyle);
            createNumericCell(dataRow, 11, row.getBasePriceWithVat(), dataStyle);
            createNumericCell(dataRow, 12, row.getNewBasePriceWithVat(), dataStyle);
            createNumericCell(dataRow, 13, row.getVat(), dataStyle);
            createNumericCell(dataRow, 14, row.getNewVat(), dataStyle);
            
            rowNum++;
        }

        // Auto-size kolone
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    private void createCell(Row row, int columnIndex, String value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        if (value != null) {
            cell.setCellValue(value);
        }
        cell.setCellStyle(style);
    }

    private void createNumericCell(Row row, int columnIndex, Number value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        if (value != null) {
            cell.setCellValue(value.doubleValue());
        }
        cell.setCellStyle(style);
    }

    public byte[] exportAllToExcel(int page, int size) throws IOException {
        Pageable pageable = PageRequest.of(page, size);
        Page<MerchantInventory> pageResult = repository.findAll(pageable);
        List<MerchantInventoryDTO> rows = pageResult.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return exportToExcel(rows);
    }

    public byte[] exportByUploadIdToExcel(UUID uploadId) throws IOException {
        List<MerchantInventory> rows = repository.findByUploadId(uploadId);
        List<MerchantInventoryDTO> rowDTOs = rows.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return exportToExcel(rowDTOs);
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

