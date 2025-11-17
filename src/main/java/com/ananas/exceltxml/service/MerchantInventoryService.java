package com.ananas.exceltxml.service;

import com.ananas.exceltxml.dto.MerchantInventoryDTO;
import com.ananas.exceltxml.dto.MerchantInventorySearchRequest;
import com.ananas.exceltxml.dto.MerchantInventoryUploadResponse;
import com.ananas.exceltxml.entity.MerchantInventory;
import com.ananas.exceltxml.repository.MerchantInventoryRepository;
import com.ananas.exceltxml.repository.MerchantInventorySpecifications;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantInventoryService {

    private final MerchantInventoryRepository repository;

    @Transactional
    public MerchantInventoryUploadResponse uploadExcel(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Fajl je prazan");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
            throw new IllegalArgumentException("Fajl mora biti .xlsx ili .xls format");
        }

        Workbook workbook;
        if (fileName.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else {
            workbook = new HSSFWorkbook(file.getInputStream());
        }

        Sheet sheet = workbook.getSheetAt(0);
        UUID uploadId = UUID.randomUUID();
        List<MerchantInventory> rowsToSave = new ArrayList<>();
        int rowNumber = 0;

        for (Row row : sheet) {
            // Preskačemo prva 2 reda (A1 - header, A2 - prazan)
            // Podaci počinju od reda A3 (index 2)
            if (rowNumber < 2) {
                rowNumber++;
                continue;
            }

            MerchantInventory inventory = new MerchantInventory();
            inventory.setUploadId(uploadId);
            inventory.setRowNumber(rowNumber);

            // Čitamo podatke iz kolona prema redosledu u Excel fajlu
            // MerchantInventoryId; Product name; Status; L1 Category; Product type; EAN; a code; SKU; Tags; Warehouse; Current stock; Base price with VAT; New base price with VAT; VAT; New VAT
            inventory.setMerchantInventoryId(getCellValueAsString(row.getCell(0)));
            inventory.setProductName(getCellValueAsString(row.getCell(1)));
            inventory.setStatus(getCellValueAsString(row.getCell(2)));
            inventory.setL1Category(getCellValueAsString(row.getCell(3)));
            inventory.setProductType(getCellValueAsString(row.getCell(4)));
            inventory.setEan(getCellValueAsString(row.getCell(5)));
            inventory.setACode(getCellValueAsString(row.getCell(6)));
            inventory.setSku(getCellValueAsString(row.getCell(7)));
            inventory.setTags(getCellValueAsString(row.getCell(8)));
            inventory.setWarehouse(getCellValueAsString(row.getCell(9)));
            inventory.setCurrentStock(getCellValueAsInteger(row.getCell(10)));
            inventory.setBasePriceWithVat(getCellValueAsBigDecimal(row.getCell(11)));
            inventory.setNewBasePriceWithVat(getCellValueAsBigDecimal(row.getCell(12)));
            inventory.setVat(getCellValueAsBigDecimal(row.getCell(13)));
            inventory.setNewVat(getCellValueAsBigDecimal(row.getCell(14)));

            // Čuvamo samo redove gde je Product name popunjen
            if (inventory.getProductName() != null && !inventory.getProductName().trim().isEmpty()) {
                rowsToSave.add(inventory);
            }

            rowNumber++;
        }

        workbook.close();

        List<MerchantInventory> savedRows = repository.saveAll(rowsToSave);

        List<MerchantInventoryDTO> rowDTOs = savedRows.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        MerchantInventoryUploadResponse response = new MerchantInventoryUploadResponse();
        response.setUploadId(uploadId);
        response.setTotalRows(rowNumber - 1);
        response.setSavedRows(savedRows.size());
        response.setMessage("Uspešno učitano " + savedRows.size() + " redova");
        response.setRows(rowDTOs);

        return response;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == Math.floor(numericValue)) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    private Integer getCellValueAsInteger(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }

    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING:
                try {
                    return new BigDecimal(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }

    public Page<MerchantInventoryDTO> getAllRows(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public Page<MerchantInventoryDTO> search(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.search(searchTerm, pageable)
                .map(this::convertToDTO);
    }

    public Page<MerchantInventoryDTO> searchByFilters(MerchantInventorySearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Specification<MerchantInventory> spec = MerchantInventorySpecifications.findByFilters(
                request.getMerchantInventoryId(),
                request.getProductName(),
                request.getStatus(),
                request.getL1Category(),
                request.getProductType(),
                request.getEan(),
                request.getACode(),
                request.getSku(),
                request.getTags(),
                request.getWarehouse(),
                request.getCurrentStockMin(),
                request.getCurrentStockMax(),
                request.getBasePriceWithVatMin(),
                request.getBasePriceWithVatMax(),
                request.getNewBasePriceWithVatMin(),
                request.getNewBasePriceWithVatMax(),
                request.getVatMin(),
                request.getVatMax(),
                request.getNewVatMin(),
                request.getNewVatMax()
        );
        return repository.findAll(spec, pageable).map(this::convertToDTO);
    }

    public List<MerchantInventoryDTO> getByUploadId(UUID uploadId) {
        return repository.findByUploadId(uploadId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MerchantInventoryDTO getById(Integer id) {
        MerchantInventory inventory = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Red sa ID " + id + " nije pronađen"));
        return convertToDTO(inventory);
    }

    @Transactional
    public MerchantInventoryDTO create(MerchantInventoryDTO dto) {
        MerchantInventory inventory = convertToEntity(dto);
        inventory.setUploadId(UUID.randomUUID());
        MerchantInventory saved = repository.save(inventory);
        return convertToDTO(saved);
    }

    @Transactional
    public MerchantInventoryDTO update(Integer id, MerchantInventoryDTO dto) {
        MerchantInventory existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Red sa ID " + id + " nije pronađen"));

        existing.setMerchantInventoryId(dto.getMerchantInventoryId());
        existing.setProductName(dto.getProductName());
        existing.setStatus(dto.getStatus());
        existing.setL1Category(dto.getL1Category());
        existing.setProductType(dto.getProductType());
        existing.setEan(dto.getEan());
        existing.setACode(dto.getACode());
        existing.setSku(dto.getSku());
        existing.setTags(dto.getTags());
        existing.setWarehouse(dto.getWarehouse());
        existing.setCurrentStock(dto.getCurrentStock());
        existing.setBasePriceWithVat(dto.getBasePriceWithVat());
        existing.setNewBasePriceWithVat(dto.getNewBasePriceWithVat());
        existing.setVat(dto.getVat());
        existing.setNewVat(dto.getNewVat());
        existing.setRowNumber(dto.getRowNumber());

        MerchantInventory updated = repository.save(existing);
        return convertToDTO(updated);
    }

    @Transactional
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Red sa ID " + id + " nije pronađen");
        }
        repository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
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

    private MerchantInventory convertToEntity(MerchantInventoryDTO dto) {
        MerchantInventory inventory = new MerchantInventory();
        inventory.setId(dto.getId());
        inventory.setUploadId(dto.getUploadId());
        inventory.setMerchantInventoryId(dto.getMerchantInventoryId());
        inventory.setProductName(dto.getProductName());
        inventory.setStatus(dto.getStatus());
        inventory.setL1Category(dto.getL1Category());
        inventory.setProductType(dto.getProductType());
        inventory.setEan(dto.getEan());
        inventory.setACode(dto.getACode());
        inventory.setSku(dto.getSku());
        inventory.setTags(dto.getTags());
        inventory.setWarehouse(dto.getWarehouse());
        inventory.setCurrentStock(dto.getCurrentStock());
        inventory.setBasePriceWithVat(dto.getBasePriceWithVat());
        inventory.setNewBasePriceWithVat(dto.getNewBasePriceWithVat());
        inventory.setVat(dto.getVat());
        inventory.setNewVat(dto.getNewVat());
        inventory.setRowNumber(dto.getRowNumber());
        return inventory;
    }
}

