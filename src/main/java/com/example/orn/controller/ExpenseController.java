package com.example.orn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.orn.model.Expense;
import com.example.orn.service.ExpenseService;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins="*")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public Expense savExpense(@RequestBody Expense expense){
        return expenseService.saveExpense(expense);

    }
    @GetMapping 
    public List<Expense> getAllExpenses(){
        return expenseService.getAllExpenses();
    }
    
    @GetMapping("/{id}")
    public Expense getExpense(@PathVariable Long id)
    {
        return expenseService.getExpenseById(id);

    }
    @PutMapping("/{id}")
    public Expense updateExpense(@PathVariable Long id,@RequestBody Expense expense){
        return expenseService.updateExpense(id, expense);
    }

    @DeleteMapping("{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public String deletedExpense(@PathVariable Long id){
            expenseService.deleteExpense(id);

            return "Expense delete successfully";
        }
    
    
}
