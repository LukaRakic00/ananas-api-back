package com.ananas.exceltxml.controller;

import com.ananas.exceltxml.dto.ExcelRowDTO;
import com.ananas.exceltxml.dto.ExcelUploadResponse;
import com.ananas.exceltxml.dto.SearchRequest;
import com.ananas.exceltxml.service.ExcelService;
import com.ananas.exceltxml.service.XmlExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
public class ExcelController {

    private final ExcelService excelService;
    private final XmlExportService xmlExportService;

    /**
     * POST /api/excel/upload
     * Upload Excel fajla (.xlsx ili .xls)
     */
    @PostMapping("/upload")
    public ResponseEntity<ExcelUploadResponse> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            ExcelUploadResponse response = excelService.uploadExcel(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ExcelUploadResponse(null, 0, 0, "Greška: " + e.getMessage(), null));
        }
    }

    /**
     * GET /api/excel
     * Vraća sve redove sa paginacijom
     */
    @GetMapping
    public ResponseEntity<?> getAllRows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            Page<ExcelRowDTO> rows = excelService.getAllRows(page, size);
            return ResponseEntity.ok(rows);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", true);
            error.put("message", "Greška pri učitavanju podataka: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET /api/excel/{id}
     * Vraća red po ID-u
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExcelRowDTO> getById(@PathVariable Integer id) {
        ExcelRowDTO row = excelService.getById(id);
        return ResponseEntity.ok(row);
    }

    /**
     * GET /api/excel/upload/{uploadId}
     * Vraća sve redove za određeni upload
     */
    @GetMapping("/upload/{uploadId}")
    public ResponseEntity<List<ExcelRowDTO>> getByUploadId(@PathVariable UUID uploadId) {
        List<ExcelRowDTO> rows = excelService.getByUploadId(uploadId);
        return ResponseEntity.ok(rows);
    }

    /**
     * POST /api/excel/search
     * Pretraga po bilo kom polju
     */
    @PostMapping("/search")
    public ResponseEntity<Page<ExcelRowDTO>> search(@RequestBody SearchRequest request) {
        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            Page<ExcelRowDTO> rows = excelService.search(request.getSearch(), request.getPage(), request.getSize());
            return ResponseEntity.ok(rows);
        } else {
            Page<ExcelRowDTO> rows = excelService.searchByFilters(
                    request.getNaziv(),
                    request.getVrednost(),
                    request.getNapomena(),
                    request.getPage(),
                    request.getSize()
            );
            return ResponseEntity.ok(rows);
        }
    }

    /**
     * GET /api/excel/search
     * Pretraga po bilo kom polju (GET varijanta)
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ExcelRowDTO>> searchGet(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String naziv,
            @RequestParam(required = false) String vrednost,
            @RequestParam(required = false) String napomena,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        if (search != null && !search.isEmpty()) {
            Page<ExcelRowDTO> rows = excelService.search(search, page, size);
            return ResponseEntity.ok(rows);
        } else {
            Page<ExcelRowDTO> rows = excelService.searchByFilters(naziv, vrednost, napomena, page, size);
            return ResponseEntity.ok(rows);
        }
    }

    /**
     * POST /api/excel
     * Kreira novi red
     */
    @PostMapping
    public ResponseEntity<ExcelRowDTO> create(@RequestBody ExcelRowDTO dto) {
        ExcelRowDTO created = excelService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/excel/{id}
     * Ažurira postojeći red
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExcelRowDTO> update(@PathVariable Integer id, @RequestBody ExcelRowDTO dto) {
        ExcelRowDTO updated = excelService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/excel/{id}
     * Briše red
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        excelService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/excel
     * Briše sve redove
     */
    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteAll() {
        excelService.deleteAll();
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
        headers.setContentDispositionFormData("attachment", "excel_rows.xml");
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
        headers.setContentDispositionFormData("attachment", "excel_rows_" + uploadId + ".xml");
        return new ResponseEntity<>(xml, headers, HttpStatus.OK);
    }
}

