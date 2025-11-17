package com.ananas.exceltxml.controller;

import com.ananas.exceltxml.dto.MerchantInventoryDTO;
import com.ananas.exceltxml.dto.MerchantInventorySearchRequest;
import com.ananas.exceltxml.service.MerchantInventoryService;
import com.ananas.exceltxml.service.MerchantInventoryXmlExportService;
import com.ananas.exceltxml.service.MerchantInventoryExcelExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller za READ operacije - samo za Ananas
 * Ovaj endpoint ima samo READ privilegije
 */
@RestController
@RequestMapping("/api/ananas/excel")
@RequiredArgsConstructor
public class MerchantInventoryReadController {

    private final MerchantInventoryService service;
    private final MerchantInventoryXmlExportService xmlExportService;
    private final MerchantInventoryExcelExportService excelExportService;

    /**
     * GET /api/ananas/excel
     * Vraća sve redove sa paginacijom
     */
    @GetMapping
    public ResponseEntity<Page<MerchantInventoryDTO>> getAllRows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<MerchantInventoryDTO> rows = service.getAllRows(page, size);
        return ResponseEntity.ok(rows);
    }

    /**
     * GET /api/ananas/excel/{id}
     * Vraća red po ID-u
     */
    @GetMapping("/{id}")
    public ResponseEntity<MerchantInventoryDTO> getById(@PathVariable Integer id) {
        MerchantInventoryDTO row = service.getById(id);
        return ResponseEntity.ok(row);
    }

    /**
     * GET /api/ananas/excel/upload/{uploadId}
     * Vraća sve redove za određeni upload
     */
    @GetMapping("/upload/{uploadId}")
    public ResponseEntity<List<MerchantInventoryDTO>> getByUploadId(@PathVariable UUID uploadId) {
        List<MerchantInventoryDTO> rows = service.getByUploadId(uploadId);
        return ResponseEntity.ok(rows);
    }

    /**
     * GET /api/ananas/excel/search
     * Pretraga po bilo kom polju
     */
    @GetMapping("/search")
    public ResponseEntity<Page<MerchantInventoryDTO>> search(
            @RequestParam String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<MerchantInventoryDTO> rows = service.search(search, page, size);
        return ResponseEntity.ok(rows);
    }

    /**
     * GET /api/ananas/excel/filter
     * Pretraga po specifičnim filterima
     */
    @GetMapping("/filter")
    public ResponseEntity<Page<MerchantInventoryDTO>> filter(
            @RequestParam(required = false) String merchantInventoryId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String l1Category,
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) String ean,
            @RequestParam(required = false) String aCode,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String warehouse,
            @RequestParam(required = false) Integer currentStockMin,
            @RequestParam(required = false) Integer currentStockMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        MerchantInventorySearchRequest request = new MerchantInventorySearchRequest();
        request.setMerchantInventoryId(merchantInventoryId);
        request.setProductName(productName);
        request.setStatus(status);
        request.setL1Category(l1Category);
        request.setProductType(productType);
        request.setEan(ean);
        request.setACode(aCode);
        request.setSku(sku);
        request.setTags(tags);
        request.setWarehouse(warehouse);
        request.setCurrentStockMin(currentStockMin);
        request.setCurrentStockMax(currentStockMax);
        request.setPage(page);
        request.setSize(size);
        Page<MerchantInventoryDTO> rows = service.searchByFilters(request);
        return ResponseEntity.ok(rows);
    }

    /**
     * GET /api/ananas/excel/export/xml
     * Export svih redova kao XML
     */
    @GetMapping(value = "/export/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> exportAllToXml(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size) {
        String xml = xmlExportService.exportAllToXml(page, size);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentDispositionFormData("attachment", "merchant_inventory.xml");
        return new ResponseEntity<>(xml, headers, HttpStatus.OK);
    }

    /**
     * GET /api/ananas/excel/export/xml/{uploadId}
     * Export redova za određeni upload kao XML
     */
    @GetMapping(value = "/export/xml/{uploadId}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> exportByUploadIdToXml(@PathVariable UUID uploadId) {
        String xml = xmlExportService.exportByUploadIdToXml(uploadId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentDispositionFormData("attachment", "merchant_inventory_" + uploadId + ".xml");
        return new ResponseEntity<>(xml, headers, HttpStatus.OK);
    }

    /**
     * GET /api/ananas/excel/export/excel
     * Export svih redova kao Excel fajl (READ ONLY)
     */
    @GetMapping(value = "/export/excel", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportAllToExcel(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size) {
        try {
            byte[] excelData = excelExportService.exportAllToExcel(page, size);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "merchant_inventory.xlsx");
            headers.setContentLength(excelData.length);
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (java.io.IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/ananas/excel/export/excel/{uploadId}
     * Export redova za određeni upload kao Excel fajl (READ ONLY)
     */
    @GetMapping(value = "/export/excel/{uploadId}", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportByUploadIdToExcel(@PathVariable UUID uploadId) {
        try {
            byte[] excelData = excelExportService.exportByUploadIdToExcel(uploadId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "merchant_inventory_" + uploadId + ".xlsx");
            headers.setContentLength(excelData.length);
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (java.io.IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

