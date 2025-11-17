package com.ananas.exceltxml.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelRowDTO {
    private Integer id;
    private UUID uploadId;
    private String naziv;
    private String vrednost;
    private String napomena;
    private Integer rowNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

