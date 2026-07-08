package com.example.orn.dto;

import java.util.List;

import com.example.orn.model.ExcelUpload;
import com.example.orn.model.OrnEntry;

public class ComparisonDTO 
{
     private OrnEntry manual;

    private ExcelUpload excel;

    private List<ExcelUpload> duplicateExcelRecords;

    public ComparisonDTO() {
    }

    public ComparisonDTO(OrnEntry manual, ExcelUpload excel,List<ExcelUpload> duplicateExcelRecords) {
        this.manual = manual;
        this.excel = excel;
        this.duplicateExcelRecords = duplicateExcelRecords;

    }



    public OrnEntry getManual() {
        return manual;
    }

    public void setManual(OrnEntry manual) {
        this.manual = manual;
    }

    public ExcelUpload getExcel() {
        return excel;
    }

    public void setExcel(ExcelUpload excel) {
        this.excel = excel;
    }



    public List<ExcelUpload> getDuplicateExcelRecords() {
        return duplicateExcelRecords;
    }



    public void setDuplicateExcelRecords(List<ExcelUpload> duplicateExcelRecords) {
        this.duplicateExcelRecords = duplicateExcelRecords;
    }

    
    
}
