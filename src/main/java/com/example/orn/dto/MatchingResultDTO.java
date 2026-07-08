package com.example.orn.dto;

public class MatchingResultDTO
 
{
     private String orn;
    private String customerName;
    private String mobileNumber;

    // NEW: explicit Manual ORN / Excel ORN columns required by the Matching grid.
    // Added additively — the original "orn" field is untouched and keeps working
    // exactly as before (used by the View button / details lookup).
    private String manualOrn;
    private String excelOrn;

    private String status;

    private int matchPercentage;

    public MatchingResultDTO() {
    }

    public MatchingResultDTO(String orn, String customerName, String mobileNumber, String status, int matchPercentage) {
        this.orn = orn;
        this.customerName = customerName;
        this.mobileNumber = mobileNumber;
        this.status = status;
        this.matchPercentage = matchPercentage;
    }

    public String getOrn() {
        return orn;
    }

    public void setOrn(String orn) {
        this.orn = orn;
    }

    public String getManualOrn() {
        return manualOrn;
    }

    public void setManualOrn(String manualOrn) {
        this.manualOrn = manualOrn;
    }

    public String getExcelOrn() {
        return excelOrn;
    }

    public void setExcelOrn(String excelOrn) {
        this.excelOrn = excelOrn;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(int matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    
    
}
