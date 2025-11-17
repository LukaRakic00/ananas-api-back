package com.ananas.exceltxml.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelUploadResponse {
    private UUID uploadId;
    private int totalRows;
    private int savedRows;
    private String message;
    private List<ExcelRowDTO> rows;
}

