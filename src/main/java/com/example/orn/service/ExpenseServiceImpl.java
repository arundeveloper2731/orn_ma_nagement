package com.example.orn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.orn.model.Expense;
import com.example.orn.repository.ExpenseRepository;

@Service
public class ExpenseServiceImpl implements ExpenseService
{
    @Autowired
    private ExpenseRepository repository;

    @Override
    public Expense saveExpense(Expense expense){
        return repository.save(expense);
    }

    @Override
    public List<Expense> getAllExpenses() {
        return repository.findAll();
    }
    
    @Override
    public Expense getExpenseById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Expense updateExpense(Long id, Expense expense) {
        Expense oldExpense = repository.findById(id).orElse(null);

        if(oldExpense !=null){
            oldExpense.setExpenseName(expense.getExpenseName());
            oldExpense.setAmount(expense.getAmount());
            oldExpense.setExpenseDate(expense.getExpenseDate());
            oldExpense.setOrn(expense.getOrn());
            oldExpense.setDescription(expense.getDescription());

            return repository.save(oldExpense);

        }
        return null;
    }

    @Override
    public void deleteExpense(Long id) {
        repository.deleteById(id);
    }
    
}
