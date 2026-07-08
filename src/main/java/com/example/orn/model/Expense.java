package com.example.orn.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "expense")
public class Expense 
{
  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String expenseName;
    private Double amount;
    private LocalDate expenseDate;

    private String orn;
    @Column(length = 1000)
    private String description;

    public Expense(){

    }
    public Expense(Long id, String expenseName, Double amount, LocalDate expenseDate, String orn, String description) {
        this.id = id;
        this.expenseName = expenseName;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.orn = orn;
        this.description = description;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getExpenseName() {
        return expenseName;
    }
    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public LocalDate getExpenseDate() {
        return expenseDate;
    }
    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }
    public String getOrn() {
        return orn;
    }
    public void setOrn(String orn) {
        this.orn = orn;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    

    
    
}
