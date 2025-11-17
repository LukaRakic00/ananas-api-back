package com.ananas.exceltxml.controller;

import com.ananas.exceltxml.dto.MerchantInventoryDTO;
import com.ananas.exceltxml.dto.MerchantInventorySearchRequest;
import com.ananas.exceltxml.dto.MerchantInventoryUploadResponse;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controller za CRUD operacije - za frontend
 * Ovaj endpoint ima sve CRUD privilegije
 */
@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class MerchantInventoryController {

    private final MerchantInventoryService service;
    private final MerchantInventoryXmlExportService xmlExportService;
    private final MerchantInventoryExcelExportService excelExportService;

    /**
     * POST /api/excel/upload
     * Upload Excel fajla (.xlsx ili .xls)
     */
    @PostMapping("/upload")
    public ResponseEntity<MerchantInventoryUploadResponse> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            MerchantInventoryUploadResponse response = service.uploadExcel(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MerchantInventoryUploadResponse(null, 0, 0, "Greška: " + e.getMessage(), null));
        }
    }

    /**
     * GET /api/excel/{id}
     * Vraća red po ID-u
     */
    @GetMapping("/{id}")
    public ResponseEntity<MerchantInventoryDTO> getById(@PathVariable Integer id) {
        MerchantInventoryDTO row = service.getById(id);
        return ResponseEntity.ok(row);
    }

    /**
     * GET /api/excel/upload/{uploadId}
     * Vraća sve redove za određeni upload
     */
    @GetMapping("/upload/{uploadId}")
    public ResponseEntity<List<MerchantInventoryDTO>> getByUploadId(@PathVariable UUID uploadId) {
        List<MerchantInventoryDTO> rows = service.getByUploadId(uploadId);
        return ResponseEntity.ok(rows);
    }

    /**
     * POST /api/excel/search
     * Pretraga po bilo kom polju ili filterima
     */
    @PostMapping("/search")
    public ResponseEntity<Page<MerchantInventoryDTO>> search(@RequestBody MerchantInventorySearchRequest request) {
        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            Page<MerchantInventoryDTO> rows = service.search(request.getSearch(), request.getPage(), request.getSize());
            return ResponseEntity.ok(rows);
        } else {
            Page<MerchantInventoryDTO> rows = service.searchByFilters(request);
            return ResponseEntity.ok(rows);
        }
    }

    /**
     * GET /api/excel/search
     * Pretraga po bilo kom polju (GET varijanta)
     */
    @GetMapping("/search")
    public ResponseEntity<Page<MerchantInventoryDTO>> searchGet(
            @RequestParam(required = false) String search,
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
        if (search != null && !search.isEmpty()) {
            Page<MerchantInventoryDTO> rows = service.search(search, page, size);
            return ResponseEntity.ok(rows);
        } else {
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
    }

    /**
     * POST /api/excel
     * Kreira novi red
     */
    @PostMapping
    public ResponseEntity<MerchantInventoryDTO> create(@RequestBody MerchantInventoryDTO dto) {
        MerchantInventoryDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/excel/{id}
     * Ažurira postojeći red
     */
    @PutMapping("/{id}")
    public ResponseEntity<MerchantInventoryDTO> update(@PathVariable Integer id, @RequestBody MerchantInventoryDTO dto) {
        MerchantInventoryDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/excel/{id}
     * Briše red
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/excel
     * Briše sve redove
     */
    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteAll() {
        service.deleteAll();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Svi redovi su uspešno obrisani");
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/excel/export/xml
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
     * GET /api/excel/export/xml/{uploadId}
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
     * GET /api/excel/export/excel
     * Export svih redova kao Excel fajl
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
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/excel/export/excel/{uploadId}
     * Export redova za određeni upload kao Excel fajl
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
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/excel
     * Vraća sve redove sa paginacijom
     * Podržava JSON (default) i XML format (Accept: application/xml)
     */
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getAllRows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader(value = "Accept", required = false, defaultValue = "application/json") String accept) {
        try {
            Page<MerchantInventoryDTO> rows = service.getAllRows(page, size);
            
            // Ako se traži XML format, vraćamo XML
            if (accept != null && accept.contains("application/xml")) {
                String xml = xmlExportService.exportToXml(rows.getContent());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_XML);
                return new ResponseEntity<>(xml, headers, HttpStatus.OK);
            }
            
            // Inače vraćamo JSON (default)
            return ResponseEntity.ok(rows);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", true);
            error.put("message", "Greška pri učitavanju podataka: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

