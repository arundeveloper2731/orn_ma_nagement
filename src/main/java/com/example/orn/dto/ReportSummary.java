package com.example.orn.dto;

import lombok.Data;

@Data
public class ReportSummary {

    private long totalOrn;

    private long matched;

    private long unmatched;

    private long duplicate;

    private long missingInExcel;

    private long missingInManual;

    private double totalExpense;
}