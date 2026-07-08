package com.example.orn.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.orn.dto.ReportDTO;
import com.example.orn.dto.ReportSummary;
import com.example.orn.service.ReportService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins="https://ornmanagement.netlify.app")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/summary")
    public ReportSummary summary(){
        return reportService.getSummary();
    }

    @GetMapping
    public List<ReportDTO> reports(@RequestParam(required = false) LocalDate from,
                                    @RequestParam(required = false) LocalDate to,
                                    @RequestParam(defaultValue ="All") String status)
                                    {
                                        return reportService.getReports(from, to, status);
                                    }

    @GetMapping("/export/excel")
    public void exportExcel(@RequestParam(required = false) LocalDate from,
                             @RequestParam(required = false) LocalDate to,
                             @RequestParam(required = false) String status,
                             HttpServletResponse response) throws IOException{
        reportService.exportExcel(response, from, to, status);
    }

    @GetMapping("/export/pdf")
    public void exportPdf(@RequestParam(required = false) LocalDate from,
                           @RequestParam(required = false) LocalDate to,
                           @RequestParam(required = false) String status,
                           HttpServletResponse response) throws Exception {

        reportService.exportPdf(response, from, to, status);

    }
    
}
