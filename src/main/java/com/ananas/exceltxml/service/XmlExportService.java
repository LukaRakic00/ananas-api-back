package com.ananas.exceltxml.service;

import com.ananas.exceltxml.dto.ExcelRowDTO;
import com.ananas.exceltxml.entity.ExcelRow;
import com.ananas.exceltxml.repository.ExcelRowRepository;
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
public class XmlExportService {

    private final ExcelRowRepository excelRowRepository;

    public String exportToXml(List<ExcelRowDTO> rows) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<excel_rows>\n");

        for (ExcelRowDTO row : rows) {
            xml.append("  <row>\n");
            xml.append("    <id>").append(row.getId()).append("</id>\n");
            xml.append("    <upload_id>").append(row.getUploadId()).append("</upload_id>\n");
            xml.append("    <naziv>").append(escapeXml(row.getNaziv())).append("</naziv>\n");
            if (row.getVrednost() != null) {
                xml.append("    <vrednost>").append(escapeXml(row.getVrednost())).append("</vrednost>\n");
            }
            if (row.getNapomena() != null) {
                xml.append("    <napomena>").append(escapeXml(row.getNapomena())).append("</napomena>\n");
            }
            if (row.getRowNumber() != null) {
                xml.append("    <row_number>").append(row.getRowNumber()).append("</row_number>\n");
            }
            xml.append("    <created_at>").append(row.getCreatedAt()).append("</created_at>\n");
            if (row.getUpdatedAt() != null) {
                xml.append("    <updated_at>").append(row.getUpdatedAt()).append("</updated_at>\n");
            }
            xml.append("  </row>\n");
        }

        xml.append("</excel_rows>");
        return xml.toString();
    }

    public String exportAllToXml(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ExcelRow> pageResult = excelRowRepository.findAll(pageable);
        List<ExcelRowDTO> rows = pageResult.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return exportToXml(rows);
    }

    public String exportByUploadIdToXml(UUID uploadId) {
        List<ExcelRow> rows = excelRowRepository.findByUploadId(uploadId);
        List<ExcelRowDTO> rowDTOs = rows.stream()
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
}

