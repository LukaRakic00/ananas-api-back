package com.ananas.exceltxml.service;

import com.ananas.exceltxml.dto.ExcelRowDTO;
import com.ananas.exceltxml.dto.ExcelUploadResponse;
import com.ananas.exceltxml.entity.ExcelRow;
import com.ananas.exceltxml.repository.ExcelRowRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final ExcelRowRepository excelRowRepository;

    @Transactional
    public ExcelUploadResponse uploadExcel(MultipartFile file) throws IOException {
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
        List<ExcelRow> rowsToSave = new ArrayList<>();
        int rowNumber = 0;

        for (Row row : sheet) {
            if (rowNumber == 0) {
                // Preskačemo header red
                rowNumber++;
                continue;
            }

            ExcelRow excelRow = new ExcelRow();
            excelRow.setUploadId(uploadId);
            excelRow.setRowNumber(rowNumber);

            // Čitamo podatke iz kolona (prva kolona - naziv, druga - vrednost, treća - napomena)
            Cell nazivCell = row.getCell(0);
            Cell vrednostCell = row.getCell(1);
            Cell napomenaCell = row.getCell(2);

            excelRow.setNaziv(getCellValueAsString(nazivCell));
            excelRow.setVrednost(getCellValueAsString(vrednostCell));
            excelRow.setNapomena(getCellValueAsString(napomenaCell));

            // Čuvamo samo redove gde je naziv popunjen
            if (excelRow.getNaziv() != null && !excelRow.getNaziv().trim().isEmpty()) {
                rowsToSave.add(excelRow);
            }

            rowNumber++;
        }

        workbook.close();

        List<ExcelRow> savedRows = excelRowRepository.saveAll(rowsToSave);

        List<ExcelRowDTO> rowDTOs = savedRows.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        ExcelUploadResponse response = new ExcelUploadResponse();
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
                    // Formatiranje brojeva bez decimala ako su celi brojevi
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

    public Page<ExcelRowDTO> getAllRows(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return excelRowRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public Page<ExcelRowDTO> search(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return excelRowRepository.search(searchTerm, pageable)
                .map(this::convertToDTO);
    }

    public Page<ExcelRowDTO> searchByFilters(String naziv, String vrednost, String napomena, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return excelRowRepository.findByFilters(naziv, vrednost, napomena, pageable)
                .map(this::convertToDTO);
    }

    public List<ExcelRowDTO> getByUploadId(UUID uploadId) {
        return excelRowRepository.findByUploadId(uploadId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ExcelRowDTO getById(Integer id) {
        ExcelRow row = excelRowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Red sa ID " + id + " nije pronađen"));
        return convertToDTO(row);
    }

    @Transactional
    public ExcelRowDTO create(ExcelRowDTO dto) {
        ExcelRow row = convertToEntity(dto);
        row.setUploadId(UUID.randomUUID());
        ExcelRow saved = excelRowRepository.save(row);
        return convertToDTO(saved);
    }

    @Transactional
    public ExcelRowDTO update(Integer id, ExcelRowDTO dto) {
        ExcelRow existing = excelRowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Red sa ID " + id + " nije pronađen"));

        existing.setNaziv(dto.getNaziv());
        existing.setVrednost(dto.getVrednost());
        existing.setNapomena(dto.getNapomena());
        existing.setRowNumber(dto.getRowNumber());

        ExcelRow updated = excelRowRepository.save(existing);
        return convertToDTO(updated);
    }

    @Transactional
    public void delete(Integer id) {
        if (!excelRowRepository.existsById(id)) {
            throw new RuntimeException("Red sa ID " + id + " nije pronađen");
        }
        excelRowRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        excelRowRepository.deleteAll();
    }

    private ExcelRowDTO convertToDTO(ExcelRow row) {
        ExcelRowDTO dto = new ExcelRowDTO();
        dto.setId(row.getId());
        dto.setUploadId(row.getUploadId());
        dto.setNaziv(row.getNaziv());
        dto.setVrednost(row.getVrednost());
        dto.setNapomena(row.getNapomena());
        dto.setRowNumber(row.getRowNumber());
        dto.setCreatedAt(row.getCreatedAt());
        dto.setUpdatedAt(row.getUpdatedAt());
        return dto;
    }

    private ExcelRow convertToEntity(ExcelRowDTO dto) {
        ExcelRow row = new ExcelRow();
        row.setId(dto.getId());
        row.setUploadId(dto.getUploadId());
        row.setNaziv(dto.getNaziv());
        row.setVrednost(dto.getVrednost());
        row.setNapomena(dto.getNapomena());
        row.setRowNumber(dto.getRowNumber());
        return row;
    }
}

