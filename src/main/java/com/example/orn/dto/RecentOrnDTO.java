package com.example.orn.dto;



public class RecentOrnDTO 
{
    private String ornNo;
    private String customerName;
    private String date;
    private String status;

    
    public RecentOrnDTO() {
    }


    public RecentOrnDTO(String ornNo, String customerName, String date, String status) {
        this.ornNo = ornNo;
        this.customerName = customerName;
        this.date = date;
        this.status = status;
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


    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    

    
}
