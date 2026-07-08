package com.example.orn.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;

import com.example.orn.model.ExcelUpload;
import com.example.orn.repository.ExcelUploadRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class ExcelUploadServiceImpl implements ExcelUploadService {

    private final ExcelUploadRepository excelUploadRepository;

    

    public ExcelUploadServiceImpl(ExcelUploadRepository excelUploadRepository){

        this.excelUploadRepository = excelUploadRepository;
    }
    @Override
    public void uploadExcel(MultipartFile file) {

    try {

        String fileName = file.getOriginalFilename();

        if (fileName != null && fileName.endsWith(".xlsx")) {

            readExcel(file.getInputStream(), fileName);

        } else if (fileName != null && fileName.endsWith(".csv")) {

            readCsv(file.getInputStream(), fileName);

        } else {

            throw new RuntimeException("Unsupported file type");

        }

    } catch (Exception e) {

        throw new RuntimeException("Upload Failed", e);

    }
}

    @Override
    public List<Map<String, Object>> getUploadedFileSummaries() {

        List<ExcelUpload> all = excelUploadRepository.findAll();

        Map<String, List<ExcelUpload>> grouped = all.stream()
                .filter(e -> e.getFileName() != null)
                .collect(java.util.stream.Collectors.groupingBy(ExcelUpload::getFileName));

        List<Map<String, Object>> summaries = new java.util.ArrayList<>();

        for (Map.Entry<String, List<ExcelUpload>> entry : grouped.entrySet()) {

            String fName = entry.getKey();
            List<ExcelUpload> rows = entry.getValue();

            java.time.LocalDateTime latestUploadedAt = rows.stream()
                    .map(ExcelUpload::getUploadedAt)
                    .filter(java.util.Objects::nonNull)
                    .max(java.time.LocalDateTime::compareTo)
                    .orElse(null);

            Map<String, Object> summary = new java.util.LinkedHashMap<>();
            summary.put("fileName", fName);
            summary.put("recordCount", rows.size());
            summary.put("uploadedAt", latestUploadedAt);

            summaries.add(summary);
        }

        summaries.sort((a, b) -> {
            java.time.LocalDateTime t1 = (java.time.LocalDateTime) a.get("uploadedAt");
            java.time.LocalDateTime t2 = (java.time.LocalDateTime) b.get("uploadedAt");
            if (t1 == null || t2 == null) return 0;
            return t2.compareTo(t1);
        });

        return summaries;
    }

    @Override
    public List<ExcelUpload> getByFileName(String fileName) {
        return excelUploadRepository.findByFileName(fileName);
    }

    @Override
    public void deleteByFileName(String fileName) {
        excelUploadRepository.deleteByFileName(fileName);
    }

   
    private void readExcel(InputStream inputStream, String fileName) throws IOException {

    Workbook workbook = WorkbookFactory.create(inputStream);

    org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                ExcelUpload excel = new ExcelUpload();
                excel.setFileName(fileName);

                

                excel.setPartnerRoleType(getCellValue(row.getCell(0)));
                excel.setSchemeName(getCellValue(row.getCell(1)));
                excel.setSchemeNumber(getCellValue(row.getCell(2)));
                excel.setRdsId(getCellValue(row.getCell(3)));
                excel.setPartnerNameRds(getCellValue(row.getCell(4)));
                excel.setPartnerNameZd(getCellValue(row.getCell(5)));

                // Scheme From Date
                Cell fromDateCell = row.getCell(6);
                if (fromDateCell != null &&
                        fromDateCell.getCellType() == CellType.NUMERIC &&
                        DateUtil.isCellDateFormatted(fromDateCell)) {

                    excel.setSchemeFromDate(
                            fromDateCell.getDateCellValue()
                                    .toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate());
                }

                // Scheme To Date
                Cell toDateCell = row.getCell(7);
                if (toDateCell != null &&
                        toDateCell.getCellType() == CellType.NUMERIC &&
                        DateUtil.isCellDateFormatted(toDateCell)) {

                    excel.setSchemeToDate(
                            toDateCell.getDateCellValue()
                                    .toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate());
                }

                excel.setStateCode(getCellValue(row.getCell(8)));
                excel.setRegion(getCellValue(row.getCell(9)));
                excel.setPosAgentCode(getCellValue(row.getCell(10)));
                excel.setStateName(getCellValue(row.getCell(11)));
                excel.setPartnerRoleName(getCellValue(row.getCell(12)));
                excel.setJioCenter(getCellValue(row.getCell(13)));
                excel.setMnpFlag(getCellValue(row.getCell(14)));
                excel.setCafNumber(getCellValue(row.getCell(15)));
                excel.setMaktx(getCellValue(row.getCell(16)));
                excel.setSoldFrom(getCellValue(row.getCell(17)));
                excel.setOrn(getCellValue(row.getCell(18)));
                excel.setOfferCode(getCellValue(row.getCell(19)));
                excel.setRechargeCode(getCellValue(row.getCell(20)));
                excel.setSoldFromName(getCellValue(row.getCell(21)));
                excel.setRecharge(getCellValue(row.getCell(22)));

                // Quantity
                excel.setQuantity(parseInteger(getCellValue(row.getCell(23))));

                excel.setImeiRank(getCellValue(row.getCell(24)));
                excel.setFrCode(getCellValue(row.getCell(25)));
                excel.setPrime(getCellValue(row.getCell(26)));

                // Commission
                excel.setCommission(parseDouble(getCellValue(row.getCell(27))));

                // RH Activation Date
                Cell rhDate = row.getCell(28);
                if (rhDate != null &&
                        rhDate.getCellType() == CellType.NUMERIC &&
                        DateUtil.isCellDateFormatted(rhDate)) {

                    excel.setRhActivationDate(
                            rhDate.getDateCellValue()
                                    .toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate());
                }

                // CAF Date
                Cell cafDate = row.getCell(29);
                if (cafDate != null &&
                        cafDate.getCellType() == CellType.NUMERIC &&
                        DateUtil.isCellDateFormatted(cafDate)) {

                    excel.setCafDate(
                            cafDate.getDateCellValue()
                                    .toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate());
                }

                // Hierdate
                Cell hierDateCell = row.getCell(30);
                if (hierDateCell != null &&
                        hierDateCell.getCellType() == CellType.NUMERIC &&
                        DateUtil.isCellDateFormatted(hierDateCell)) {

                    excel.setHierdate(
                            hierDateCell.getDateCellValue()
                                    .toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate());
                }

                excel.setEligHeader(getCellValue(row.getCell(31)));

                // Price
                excel.setPrice(parseDouble(getCellValue(row.getCell(32))));

                // Recharge Days
                excel.setRechargeDays(parseInteger(getCellValue(row.getCell(33))));

                excel.setAdhaarrank(getCellValue(row.getCell(34)));
                excel.setRechargeNum(getCellValue(row.getCell(35)));
                excel.setMisc05(getCellValue(row.getCell(36)));
                excel.setMisc01(getCellValue(row.getCell(37)));
                excel.setMisc02(getCellValue(row.getCell(38)));

                System.out.println("Saving ORN : " + excel.getOrn());

                try {
                        excelUploadRepository.save(excel);
                        System.out.println("Saved : " + excel.getOrn());
                    } catch (Exception e) {
                        System.err.println("Failed ORN : " + excel.getOrn());
                        e.printStackTrace();
                    }

                System.out.println("Saved : " + excel.getOrn());            }

              workbook.close();
        }

            private void readCsv(InputStream inputStream, String fileName) throws IOException {

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            // Skip Header
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",", -1);

                ExcelUpload excel = new ExcelUpload();
                excel.setFileName(fileName);

                excel.setPartnerRoleType(getValue(data, 0));
                excel.setSchemeName(getValue(data, 1));
                excel.setSchemeNumber(getValue(data, 2));
                excel.setRdsId(getValue(data, 3));
                excel.setPartnerNameRds(getValue(data, 4));
                excel.setPartnerNameZd(getValue(data, 5));
                excel.setSchemeFromDate(parseDate(getValue(data, 6)));
                excel.setSchemeToDate(parseDate(getValue(data, 7)));
                excel.setStateCode(getValue(data, 8));
                excel.setRegion(getValue(data, 9));
                excel.setPosAgentCode(getValue(data, 10));
                excel.setStateName(getValue(data, 11));
                excel.setPartnerRoleName(getValue(data, 12));
                excel.setJioCenter(getValue(data, 13));
                excel.setMnpFlag(getValue(data, 14));
                excel.setCafNumber(getValue(data, 15));
                excel.setMaktx(getValue(data, 16));
                excel.setSoldFrom(getValue(data, 17));
                excel.setOrn(getValue(data, 18));
                excel.setOfferCode(getValue(data, 19));
                excel.setRechargeCode(getValue(data, 20));
                excel.setSoldFromName(getValue(data, 21));
                excel.setRecharge(getValue(data, 22));
                excel.setQuantity(parseInteger(getValue(data, 23)));
                excel.setImeiRank(getValue(data, 24));
                excel.setFrCode(getValue(data, 25));
                excel.setPrime(getValue(data, 26));
                excel.setCommission(parseDouble(getValue(data, 27)));
                excel.setRhActivationDate(parseDate(getValue(data, 28)));
                excel.setCafDate(parseDate(getValue(data, 29)));
                excel.setHierdate(parseDate(getValue(data, 30)));
                excel.setEligHeader(getValue(data, 31));
                excel.setPrice(parseDouble(getValue(data, 32)));
                excel.setRechargeDays(parseInteger(getValue(data, 33)));
                excel.setAdhaarrank(getValue(data, 34));
                excel.setRechargeNum(getValue(data, 35));
                excel.setMisc05(getValue(data, 36));
                excel.setMisc01(getValue(data, 37));
                excel.setMisc02(getValue(data, 38));

                if (excel.getOrn() == null || excel.getOrn().isBlank()) {
                    continue;
                }

                try {
                    excelUploadRepository.save(excel);
                } catch (Exception e) {
                    System.err.println("Failed to save CSV row for ORN: " + excel.getOrn());
                    e.printStackTrace();
                }
            }

            reader.close();
        }

            private String getValue(String[] data, int index) {

            if (index >= data.length) {
                return "";
            }

            return data[index].trim();
        }
            private String getCellValue(Cell cell) {

            if (cell == null) {
                return "";
            }

            switch (cell.getCellType()) {

                case STRING:
                    return cell.getStringCellValue().trim();

                case NUMERIC:

                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getLocalDateTimeCellValue()
                                .toLocalDate()
                                .toString();
                    }

                    double value = cell.getNumericCellValue();

                    if (value == (long) value) {
                        return String.valueOf((long) value);
                    }

                    return String.valueOf(value);

                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());

                case FORMULA:
                    return cell.getCellFormula();

                default:
                    return "";
            }
        }

        private Integer parseInteger(String value) {

            try {

                if (value == null || value.isBlank()) {
                    return null;
                }

                return (int) Double.parseDouble(value);

            } catch (Exception e) {

                return null;

            }
        }
        private Double parseDouble(String value) {

            try {

                if (value == null || value.isBlank()) {
                    return null;
                }

                return Double.parseDouble(value);

            } catch (Exception e) {

                return null;

            }
        }
        private LocalDate parseDate(String value) {

            try {

                if (value == null || value.isBlank()) {
                    return null;
                }

                String v = value.trim();

                if (v.contains(" ")) {
                    v = v.substring(0, v.indexOf(' '));
                }

                if (v.contains("/")) {
                    java.time.format.DateTimeFormatter fmt =
                            java.time.format.DateTimeFormatter.ofPattern("M/d/yyyy");
                    return LocalDate.parse(v, fmt);
                }

                return LocalDate.parse(v);

            } catch (Exception e) {

                return null;

            }
        }
        
            @Override
        public ExcelUpload updateExcel(Long id, ExcelUpload updated) {

            ExcelUpload existing = excelUploadRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Excel record not found: " + id));

            existing.setPartnerRoleType(updated.getPartnerRoleType());
            existing.setSchemeName(updated.getSchemeName());
            existing.setSchemeNumber(updated.getSchemeNumber());
            existing.setRdsId(updated.getRdsId());
            existing.setPartnerNameRds(updated.getPartnerNameRds());
            existing.setPartnerNameZd(updated.getPartnerNameZd());
            existing.setSchemeFromDate(updated.getSchemeFromDate());
            existing.setSchemeToDate(updated.getSchemeToDate());
            existing.setStateCode(updated.getStateCode());
            existing.setRegion(updated.getRegion());
            existing.setPosAgentCode(updated.getPosAgentCode());
            existing.setStateName(updated.getStateName());
            existing.setPartnerRoleName(updated.getPartnerRoleName());
            existing.setJioCenter(updated.getJioCenter());
            existing.setMnpFlag(updated.getMnpFlag());
            existing.setCafNumber(updated.getCafNumber());
            existing.setMaktx(updated.getMaktx());
            existing.setSoldFrom(updated.getSoldFrom());
            existing.setOrn(updated.getOrn());
            existing.setOfferCode(updated.getOfferCode());
            existing.setRechargeCode(updated.getRechargeCode());
            existing.setSoldFromName(updated.getSoldFromName());
            existing.setRecharge(updated.getRecharge());
            existing.setQuantity(updated.getQuantity());
            existing.setImeiRank(updated.getImeiRank());
            existing.setFrCode(updated.getFrCode());
            existing.setPrime(updated.getPrime());
            existing.setCommission(updated.getCommission());
            existing.setRhActivationDate(updated.getRhActivationDate());
            existing.setCafDate(updated.getCafDate());
            existing.setHierdate(updated.getHierdate());
            existing.setEligHeader(updated.getEligHeader());
            existing.setPrice(updated.getPrice());
            existing.setRechargeDays(updated.getRechargeDays());
            existing.setAdhaarrank(updated.getAdhaarrank());
            existing.setRechargeNum(updated.getRechargeNum());
            existing.setMisc05(updated.getMisc05());
            existing.setMisc01(updated.getMisc01());
            existing.setMisc02(updated.getMisc02());
            existing.setFileName(updated.getFileName());
            // id and uploadedAt are intentionally preserved, not overwritten

            return excelUploadRepository.save(existing);
        }

            @Override
        public List<ExcelUpload> getAllUploadedData() {

            return excelUploadRepository.findAll();

        }

           @Override
            public ExcelUpload getByOrn(String orn) {
                List<ExcelUpload> list = excelUploadRepository.findAllByOrn(orn);
                return list.isEmpty() ? null : list.get(0);
            }

            @Override
            public void deleteById(Long id) {
                excelUploadRepository.deleteById(id);

            }
            @Override
            public void deleteFile(Long id) {
                excelUploadRepository.deleteById(id);
            }
            public Optional<ExcelUpload> getExcelData(Long id){

             return excelUploadRepository.findById(id);

}

}