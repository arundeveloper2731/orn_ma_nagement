package com.example.orn.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.orn.model.ExcelUpload;
import com.example.orn.service.ExcelUploadService;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin(origins="*")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class ExcelUploadController {
    private final ExcelUploadService excelUploadService;

    public ExcelUploadController(ExcelUploadService excelUploadService) {
        this.excelUploadService = excelUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {

        try {
            excelUploadService.uploadExcel(file);
            return ResponseEntity.ok("Uploaded Successfully");

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/all")
    public ResponseEntity<List<ExcelUpload>> getAllUploadedData() {

        return ResponseEntity.ok(excelUploadService.getAllUploadedData());

    }

    @GetMapping("/{orn}")
    public ResponseEntity<ExcelUpload> getByOrn(@PathVariable String orn) {

        ExcelUpload excel = excelUploadService.getByOrn(orn);

        if (excel == null) {

            return ResponseEntity.notFound().build();

        }

        return ResponseEntity.ok(excel);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {

        excelUploadService.deleteById(id);

        return ResponseEntity.ok("Record deleted successfully.");

    }

    @GetMapping("/files")
    public ResponseEntity<List<Map<String, Object>>> getUploadedFiles() {
        return ResponseEntity.ok(excelUploadService.getUploadedFileSummaries());
    }

    @GetMapping("/by-file")
    public ResponseEntity<List<ExcelUpload>> getByFileName(@RequestParam String fileName) {
        return ResponseEntity.ok(excelUploadService.getByFileName(fileName));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateExcel(@PathVariable Long id, @RequestBody ExcelUpload updated) {

        try {
            ExcelUpload result = excelUploadService.updateExcel(id, updated);
            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-file")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteByFileName(@RequestParam String fileName) {
        excelUploadService.deleteByFileName(fileName);
        return ResponseEntity.ok("All records for file '" + fileName + "' deleted successfully.");
    }

}
