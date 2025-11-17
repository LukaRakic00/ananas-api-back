package com.ananas.exceltxml.repository;

import com.ananas.exceltxml.entity.ExcelRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExcelRowRepository extends JpaRepository<ExcelRow, Integer> {

    List<ExcelRow> findByUploadId(UUID uploadId);

    @Query("SELECT e FROM ExcelRow e WHERE " +
           "LOWER(e.naziv) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.vrednost) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.napomena) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<ExcelRow> search(@Param("search") String search, Pageable pageable);

    @Query("SELECT e FROM ExcelRow e WHERE " +
           "(:naziv IS NULL OR LOWER(e.naziv) LIKE LOWER(CONCAT('%', :naziv, '%'))) AND " +
           "(:vrednost IS NULL OR LOWER(e.vrednost) LIKE LOWER(CONCAT('%', :vrednost, '%'))) AND " +
           "(:napomena IS NULL OR LOWER(e.napomena) LIKE LOWER(CONCAT('%', :napomena, '%')))")
    Page<ExcelRow> findByFilters(@Param("naziv") String naziv,
                                  @Param("vrednost") String vrednost,
                                  @Param("napomena") String napomena,
                                  Pageable pageable);
}

