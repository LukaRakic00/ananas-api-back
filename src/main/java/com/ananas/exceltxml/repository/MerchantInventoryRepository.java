package com.ananas.exceltxml.repository;

import com.ananas.exceltxml.entity.MerchantInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MerchantInventoryRepository extends JpaRepository<MerchantInventory, Integer>, JpaSpecificationExecutor<MerchantInventory> {

    List<MerchantInventory> findByUploadId(UUID uploadId);

    @Query("SELECT m FROM MerchantInventory m WHERE " +
           "LOWER(m.productName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(m.merchantInventoryId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(m.ean) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(m.sku) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(m.aCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(m.tags) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<MerchantInventory> search(@Param("search") String search, Pageable pageable);
}

