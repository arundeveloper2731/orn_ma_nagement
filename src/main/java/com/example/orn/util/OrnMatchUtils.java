package com.example.orn.util;

import java.time.LocalDate;


public final class OrnMatchUtils {

    private OrnMatchUtils() {
    }

    public static String normalise(String value) {
        return value == null ? "" : value.trim().toUpperCase();
    }

    public static boolean amountsEqual(double manualAmount, Double excelPrice) {
        if (excelPrice == null) return manualAmount == 0.0;
        return Math.abs(manualAmount - excelPrice) < 0.01;
    }

    public static boolean datesEqual(LocalDate manualDate, LocalDate excelDate) {
        if (manualDate == null && excelDate == null) return true;
        if (manualDate == null || excelDate == null) return false;
        return manualDate.isEqual(excelDate);
    }
}
