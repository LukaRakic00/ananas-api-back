package com.ananas.exceltxml.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "excel_rows")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "upload_id", nullable = false)
    private UUID uploadId = UUID.randomUUID();

    @Column(name = "naziv", nullable = false, length = 255)
    private String naziv;

    @Column(name = "vrednost", length = 255)
    private String vrednost;

    @Column(name = "napomena", columnDefinition = "TEXT")
    private String napomena;

    @Column(name = "row_number")
    private Integer rowNumber;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

