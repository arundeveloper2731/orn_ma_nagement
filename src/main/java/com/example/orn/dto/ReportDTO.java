package com.example.orn.dto;

import lombok.Data;

@Data
public class ReportDTO {
    
    private String ornNumber;
    private String customer;
    private String location;
    private String status;
    private double expense;
    private String date;
    
}
