package com.example.orn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.orn.model.Expense;
import com.example.orn.repository.ExpenseRepository;
import com.example.orn.security.SecurityUtils;

@Service
public class ExpenseServiceImpl implements ExpenseService
{
    @Autowired
    private ExpenseRepository repository;

    @Override
    public Expense saveExpense(Expense expense){
        // Tag the new expense with its owner so it only shows up for this
        // user later (ADMIN can still see every expense).
        expense.setCreatedBy(SecurityUtils.getCurrentUsername());
        return repository.save(expense);
    }

    @Override
    public List<Expense> getAllExpenses() {
        return SecurityUtils.isCurrentUserAdmin()
                ? repository.findAll()
                : repository.findByCreatedBy(SecurityUtils.getCurrentUsername());
    }
    
    @Override
    public Expense getExpenseById(Long id) {
        Expense expense = repository.findById(id).orElse(null);
        if (expense == null || !ownedByCurrentUserOrAdmin(expense)) {
            return null;
        }
        return expense;
    }

    @Override
    public Expense updateExpense(Long id, Expense expense) {
        Expense oldExpense = repository.findById(id).orElse(null);

        if(oldExpense !=null && ownedByCurrentUserOrAdmin(oldExpense)){
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

    /**
     * Legacy rows created before this fix have createdBy = null and are
     * treated as ADMIN-only, matching the "no owner assigned" choice made
     * for the migration.
     */
    private boolean ownedByCurrentUserOrAdmin(Expense expense) {
        if (SecurityUtils.isCurrentUserAdmin()) {
            return true;
        }
        String currentUser = SecurityUtils.getCurrentUsername();
        return expense.getCreatedBy() != null && expense.getCreatedBy().equals(currentUser);
    }
    
}
