package com.example.orn.dto;

import java.util.List;

import com.example.orn.model.OrnEntry;

public class DashboardDTO {
    private long totalORN;
    private long excelRecords;
    private List<OrnEntry> matchedORN;
    private List<OrnEntry> unmatchedORN;
    private double totalExpense;

    private long matchedCount;
    private long unmatchedCount;
    private List<OrnEntry> duplicateORN;
    private long duplicateCount;

    public long getDuplicateCount() {
        return duplicateCount;
    }

    public void setDuplicateCount(long duplicateCount) {
        this.duplicateCount = duplicateCount;
    }

    private List<RecentOrnDTO> recentEntries;

    public DashboardDTO() {
    }

    public DashboardDTO(long totalORN, long excelRecords, List<OrnEntry> matchedORN, List<OrnEntry> unmatchedORN, double totalExpense,
            List<RecentOrnDTO> recentEntries,List<OrnEntry> duplicateORN) {
        this.totalORN = totalORN;
        this.excelRecords = excelRecords;
        this.matchedORN = matchedORN;
        this.unmatchedORN = unmatchedORN;
        this.totalExpense = totalExpense;
        this.recentEntries = recentEntries;
        this.duplicateORN = duplicateORN;
    }

    

    public long getMatchedCount() {
        return matchedCount;
    }

    public void setMatchedCount(long matchedCount) {
        this.matchedCount = matchedCount;
    }

    public long getUnmatchedCount() {
        return unmatchedCount;
    }

    public void setUnmatchedCount(long unmatchedCount) {
        this.unmatchedCount = unmatchedCount;
    }

    public long getTotalORN() {
        return totalORN;
    }

    public void setTotalORN(long totalORN) {
        this.totalORN = totalORN;
    }

    public long getExcelRecords() {
        return excelRecords;
    }

    public void setExcelRecords(long excelRecords) {
        this.excelRecords = excelRecords;
    }

    public List<OrnEntry> getMatchedORN() {
        return matchedORN;
    }

    public void setMatchedORN(List<OrnEntry> matchedORN) {
        this.matchedORN = matchedORN;
    }

    public List<OrnEntry> getUnmatchedORN() {
        return unmatchedORN;
    }

    public void setUnmatchedORN(List<OrnEntry> unmatchedORN) {
        this.unmatchedORN = unmatchedORN;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public List<RecentOrnDTO> getRecentEntries() {
        return recentEntries;
    }

    public void setRecentEntries(List<RecentOrnDTO> recentEntries) {
        this.recentEntries = recentEntries;
    }

    public List<OrnEntry> getDuplicateORN() {
        return duplicateORN;
    }

    public void setDuplicateORN(List<OrnEntry> duplicateORN) {
        this.duplicateORN = duplicateORN;
    }

    
    
}
