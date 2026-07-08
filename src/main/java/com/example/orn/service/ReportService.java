package com.example.orn.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.example.orn.dto.ReportDTO;
import com.example.orn.dto.ReportSummary;

import jakarta.servlet.http.HttpServletResponse;

public interface ReportService 
{
    ReportSummary getSummary();
    
    List<ReportDTO> getReports(LocalDate from,LocalDate to,String status);

    void exportExcel(HttpServletResponse response, LocalDate from, LocalDate to, String status) throws IOException;

    void exportPdf(HttpServletResponse response, LocalDate from, LocalDate to, String status) throws Exception;
}
