package com.example.orn.service;

import java.util.List;

import com.example.orn.model.Expense;

public interface ExpenseService {
    
    Expense saveExpense(Expense expense);
    List<Expense> getAllExpenses();
    Expense getExpenseById(Long id);
    Expense updateExpense(Long id,Expense expense);

    void deleteExpense(Long id);
    
}
