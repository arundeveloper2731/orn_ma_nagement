package com.example.orn.dto;

import java.time.LocalDate;

public class OrnResponseDTO 
{
    private Long id;
    private String ornNo;
    private String customerName;
    private long mobileNumber;
    private String location;
    private double amount;
    private LocalDate transactionDate;
    private String status;

    // Exposed so the frontend can decide whether to show Edit/Delete for
    // this row. This is read-only output — never trusted as input, since
    // OrnRequestDTO (the incoming DTO) has no such field.
    private String createdBy;

    private String message;


    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOrnNo() {
        return ornNo;
    }
    public void setOrnNo(String ornNo) {
        this.ornNo = ornNo;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public long getMobileNumber() {
        return mobileNumber;
    }
    public void setMobileNumber(long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public LocalDate getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    
    
}
