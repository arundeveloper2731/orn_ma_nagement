package com.example.orn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.orn.model.ExcelUpload;

public interface ExcelUploadRepository extends JpaRepository<ExcelUpload, Long> {

    List<ExcelUpload> findAll();

    /**
     * Case-insensitive, trimmed lookup — avoids normalisation mismatch between
     * data saved with different casing/spaces.
     */
    @Query("SELECT e FROM ExcelUpload e WHERE TRIM(UPPER(e.orn)) = TRIM(UPPER(:orn))")
    List<ExcelUpload> findAllByOrn(@Param("orn") String orn);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
           "FROM ExcelUpload e WHERE TRIM(UPPER(e.orn)) = TRIM(UPPER(:orn))")
    boolean existsByOrn(@Param("orn") String orn);

    List<ExcelUpload> findByFileName(String fileName);

    @Transactional
    void deleteByFileName(String fileName);
}