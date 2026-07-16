package com.example.orn.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.orn.model.ExcelUpload;
import com.example.orn.model.OrnEntry;
import org.springframework.data.jpa.repository.Query;

public interface OrnRepository extends JpaRepository<OrnEntry,Long>
{


     List<OrnEntry> findTop5ByOrderByTransactionDateDesc();

      boolean existsByOrnNo(String ornNo);

      @Query("SELECT o FROM OrnEntry o WHERE o.ornNo = :ornNo")
      Optional<OrnEntry> findByOrnNo(String ornNo);

      List<OrnEntry> findAll();

        long countByOrnNo(String ornNo);      

        List<OrnEntry> findByTransactionDateBetween(LocalDate fromDate,LocalDate toDate);
              
        List<OrnEntry> findByTransactionDateBetweenAndStatus(LocalDate fromDate,LocalDate toDate,String status);
        List<OrnEntry> findByStatus(String status);

        // ---- Added for per-user data isolation ----
        // Mirror of the finders above, scoped to the record owner (createdBy).
        // Used for non-ADMIN users so they only ever see their own entries.
        List<OrnEntry> findByCreatedBy(String createdBy);

        List<OrnEntry> findByCreatedByAndTransactionDateBetween(String createdBy, LocalDate fromDate, LocalDate toDate);

        List<OrnEntry> findByCreatedByAndTransactionDateBetweenAndStatus(String createdBy, LocalDate fromDate, LocalDate toDate, String status);

        List<OrnEntry> findByCreatedByAndStatus(String createdBy, String status);

        List<OrnEntry> findTop5ByCreatedByOrderByTransactionDateDesc(String createdBy);
}
