package com.example.orn.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.example.orn.model.ExcelUpload;

public interface ExcelUploadService {
    public void uploadExcel(MultipartFile file);

    List<ExcelUpload> getAllUploadedData();

    ExcelUpload getByOrn(String orn);

    void deleteById(Long id);

    public void deleteFile(Long id);


    List<Map<String, Object>> getUploadedFileSummaries();

    List<ExcelUpload> getByFileName(String fileName);

    void deleteByFileName(String fileName);

    ExcelUpload updateExcel(Long id, ExcelUpload updated);
}
