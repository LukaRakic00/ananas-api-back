package com.ananas.exceltxml.controller;

import com.ananas.exceltxml.dto.ExcelRowDTO;
import com.ananas.exceltxml.service.ExcelService;
import com.ananas.exceltxml.service.XmlExportService;
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
public class ExcelReadController {

    private final ExcelService excelService;
    private final XmlExportService xmlExportService;

    /**
     * GET /api/ananas/excel
     * Vraća sve redove sa paginacijom
     */
    @GetMapping
    public ResponseEntity<Page<ExcelRowDTO>> getAllRows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ExcelRowDTO> rows = excelService.getAllRows(page, size);
        return ResponseEntity.ok(rows);
    }

    /**
     * GET /api/ananas/excel/{id}
     * Vraća red po ID-u
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExcelRowDTO> getById(@PathVariable Integer id) {
        ExcelRowDTO row = excelService.getById(id);
        return ResponseEntity.ok(row);
    }

    /**
     * GET /api/ananas/excel/upload/{uploadId}
     * Vraća sve redove za određeni upload
     */
    @GetMapping("/upload/{uploadId}")
    public ResponseEntity<List<ExcelRowDTO>> getByUploadId(@PathVariable UUID uploadId) {
        List<ExcelRowDTO> rows = excelService.getByUploadId(uploadId);
        return ResponseEntity.ok(rows);
    }

    /**
     * GET /api/ananas/excel/search
     * Pretraga po bilo kom polju
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ExcelRowDTO>> search(
            @RequestParam String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ExcelRowDTO> rows = excelService.search(search, page, size);
        return ResponseEntity.ok(rows);
    }

    /**
     * GET /api/ananas/excel/filter
     * Pretraga po specifičnim filterima
     */
    @GetMapping("/filter")
    public ResponseEntity<Page<ExcelRowDTO>> filter(
            @RequestParam(required = false) String naziv,
            @RequestParam(required = false) String vrednost,
            @RequestParam(required = false) String napomena,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ExcelRowDTO> rows = excelService.searchByFilters(naziv, vrednost, napomena, page, size);
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
        headers.setContentDispositionFormData("attachment", "excel_rows.xml");
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
        headers.setContentDispositionFormData("attachment", "excel_rows_" + uploadId + ".xml");
        return new ResponseEntity<>(xml, headers, HttpStatus.OK);
    }
}

