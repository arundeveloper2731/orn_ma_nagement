package com.example.orn.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.orn.dto.ReportDTO;
import com.example.orn.dto.ReportSummary;
import com.example.orn.model.ExcelUpload;
import com.example.orn.model.OrnEntry;
import com.example.orn.repository.ExcelUploadRepository;
import com.example.orn.repository.ExpenseRepository;
import com.example.orn.repository.OrnRepository;
import com.example.orn.security.SecurityUtils;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrnRepository ornRepository;

    @Autowired
    private ExcelUploadRepository excelUploadRepository;

    @Autowired
    private ExpenseRepository expenseRepository;


    @Override
    public ReportSummary getSummary() {

        boolean isAdmin = SecurityUtils.isCurrentUserAdmin();
        String currentUser = SecurityUtils.getCurrentUsername();

        List<ExcelUpload> excelRows = isAdmin
                ? excelUploadRepository.findAll()
                : excelUploadRepository.findByCreatedBy(currentUser);

        List<OrnEntry> manualRows = isAdmin
                ? ornRepository.findAll()
                : ornRepository.findByCreatedBy(currentUser);

        Set<String> excelOrnSet = excelRows
                .stream()
                .filter(e -> e.getOrn() != null && !e.getOrn().trim().isEmpty())
                .map(e -> e.getOrn().trim().toUpperCase())
                .collect(Collectors.toSet());

        Map<String, Long> excelOrnCount = excelRows
                .stream()
                .filter(e -> e.getOrn() != null && !e.getOrn().trim().isEmpty())
                .collect(Collectors.groupingBy(
                        e -> e.getOrn().trim().toUpperCase(),
                        Collectors.counting()
                ));

        Map<String, Long> manualOrnCount = manualRows
                .stream()
                .filter(o -> o.getOrnNo() != null && !o.getOrnNo().trim().isEmpty())
                .collect(Collectors.groupingBy(
                        o -> o.getOrnNo().trim().toUpperCase(),
                        Collectors.counting()
                ));

        long totalOrn   = manualRows.size();
        long matched    = 0;
        long missingInExcel = 0;
        long duplicate  = 0;

        for (OrnEntry orn : manualRows) {

            if (orn.getOrnNo() == null || orn.getOrnNo().trim().isEmpty()) continue;

            String key = orn.getOrnNo().trim().toUpperCase();

            if (!excelOrnSet.contains(key)) {
                missingInExcel++;
            } else if (excelOrnCount.getOrDefault(key, 0L) > 1 ||
                       manualOrnCount.getOrDefault(key, 0L) > 1) {
                duplicate++;
            } else {
                matched++;
            }
        }

        long missingInManual = excelRows
                .stream()
                .filter(e -> e.getOrn() != null && !e.getOrn().trim().isEmpty())
                .map(e -> e.getOrn().trim().toUpperCase())
                .filter(key -> !manualOrnCount.containsKey(key))
                .distinct()
                .count();

        double totalExpense = (isAdmin
                ? expenseRepository.findAll()
                : expenseRepository.findByCreatedBy(currentUser))
                .stream()
                .mapToDouble(e -> e.getAmount())
                .sum();

        ReportSummary dto = new ReportSummary();
        dto.setTotalOrn(totalOrn);
        dto.setMatched(matched);
        dto.setUnmatched(missingInExcel + missingInManual);
        dto.setDuplicate(duplicate);
        dto.setMissingInExcel(missingInExcel);
        dto.setMissingInManual(missingInManual);
        dto.setTotalExpense(totalExpense);

        return dto;
    }


    @Override
    public List<ReportDTO> getReports(LocalDate from, LocalDate to, String status) {

        boolean isAdmin = SecurityUtils.isCurrentUserAdmin();
        String currentUser = SecurityUtils.getCurrentUsername();

        List<ExcelUpload> excelRows = isAdmin
                ? excelUploadRepository.findAll()
                : excelUploadRepository.findByCreatedBy(currentUser);

        List<OrnEntry> allManualRows = isAdmin
                ? ornRepository.findAll()
                : ornRepository.findByCreatedBy(currentUser);

        Set<String> excelOrnSet = excelRows
                .stream()
                .filter(e -> e.getOrn() != null && !e.getOrn().trim().isEmpty())
                .map(e -> e.getOrn().trim().toUpperCase())
                .collect(Collectors.toSet());

        Map<String, Long> excelOrnCount = excelRows
                .stream()
                .filter(e -> e.getOrn() != null && !e.getOrn().trim().isEmpty())
                .collect(Collectors.groupingBy(
                        e -> e.getOrn().trim().toUpperCase(),
                        Collectors.counting()
                ));

        Map<String, Long> manualOrnCount = allManualRows
                .stream()
                .filter(o -> o.getOrnNo() != null && !o.getOrnNo().trim().isEmpty())
                .collect(Collectors.groupingBy(
                        o -> o.getOrnNo().trim().toUpperCase(),
                        Collectors.counting()
                ));

        Map<String, Double> expenseMap = new HashMap<>();
        (isAdmin ? expenseRepository.findAll() : expenseRepository.findByCreatedBy(currentUser)).forEach(exp -> {
            if (exp.getOrn() != null) {
                String k = exp.getOrn().trim().toUpperCase();
                expenseMap.merge(k, exp.getAmount(), Double::sum);
            }
        });

        List<OrnEntry> entries;

        if (from != null && to != null) {
            entries = isAdmin
                    ? ornRepository.findByTransactionDateBetween(from, to)
                    : ornRepository.findByCreatedByAndTransactionDateBetween(currentUser, from, to);
        } else {
            entries = allManualRows;
        }

        List<ReportDTO> list = new ArrayList<>();

        for (OrnEntry orn : entries) {

            String key = orn.getOrnNo() == null ? "" : orn.getOrnNo().trim().toUpperCase();

            String resolvedStatus;

            if (!excelOrnSet.contains(key)) {
                resolvedStatus = "Missing in Excel";
            } else if (excelOrnCount.getOrDefault(key, 0L) > 1 ||
                       manualOrnCount.getOrDefault(key, 0L) > 1) {
                resolvedStatus = "Duplicate";
            } else {
                resolvedStatus = "Matched";
            }

            if (status != null && !status.equalsIgnoreCase("All") &&
                !resolvedStatus.equalsIgnoreCase(status)) {
                continue;
            }

            ReportDTO dto = new ReportDTO();
            dto.setOrnNumber(orn.getOrnNo());
            dto.setCustomer(orn.getCustomerName());
            dto.setLocation(orn.getLocation());
            dto.setStatus(resolvedStatus);
            dto.setExpense(expenseMap.getOrDefault(key, 0.0));
            dto.setDate(orn.getTransactionDate() != null
                    ? orn.getTransactionDate().toString() : "");

            list.add(dto);
        }

        if (status == null || status.equalsIgnoreCase("All") ||
            status.equalsIgnoreCase("Missing in Manual")) {

            Set<String> addedExcelOnly = new HashSet<>();

            for (ExcelUpload excel : excelRows) {

                if (excel.getOrn() == null || excel.getOrn().trim().isEmpty()) continue;

                String key = excel.getOrn().trim().toUpperCase();

                if (manualOrnCount.containsKey(key)) continue;  // already covered above
                if (!addedExcelOnly.add(key)) continue;         // deduplicate

                ReportDTO dto = new ReportDTO();
                dto.setOrnNumber(excel.getOrn());
                dto.setCustomer("-");
                dto.setLocation("-");
                dto.setStatus("Missing in Manual");
                dto.setExpense(0.0);
                dto.setDate("-");

                list.add(dto);
            }
        }

        return list;
    }


    @Override
    public void exportExcel(HttpServletResponse response, LocalDate from, LocalDate to, String status) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Report.xlsx");

        Workbook workbook = new XSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Reports");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ORN");
        header.createCell(1).setCellValue("Customer");
        header.createCell(2).setCellValue("Location");
        header.createCell(3).setCellValue("Status");
        header.createCell(4).setCellValue("Expense");
        header.createCell(5).setCellValue("Date");

        List<ReportDTO> reports = getReports(from, to, status == null ? "All" : status);

        int rowNum = 1;
        for (ReportDTO r : reports) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(r.getOrnNumber());
            row.createCell(1).setCellValue(r.getCustomer());
            row.createCell(2).setCellValue(r.getLocation());
            row.createCell(3).setCellValue(r.getStatus());
            row.createCell(4).setCellValue(r.getExpense());
            row.createCell(5).setCellValue(r.getDate());
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }


    @Override
    public void exportPdf(HttpServletResponse response, LocalDate from, LocalDate to, String status) throws Exception {

        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=Report.pdf");

            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            document.add(new Paragraph("ORN Report"));

            PdfPTable table = new PdfPTable(6);
            table.addCell("ORN");
            table.addCell("Customer");
            table.addCell("Location");
            table.addCell("Status");
            table.addCell("Expense");
            table.addCell("Date");

            List<ReportDTO> reports = getReports(from, to, status == null ? "All" : status);

            for (ReportDTO r : reports) {
                table.addCell(String.valueOf(r.getOrnNumber()));
                table.addCell(String.valueOf(r.getCustomer()));
                table.addCell(String.valueOf(r.getLocation()));
                table.addCell(String.valueOf(r.getStatus()));
                table.addCell(String.valueOf(r.getExpense()));
                table.addCell(String.valueOf(r.getDate()));
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}