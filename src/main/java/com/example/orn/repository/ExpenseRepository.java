package com.example.orn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.orn.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense,Long>

{
    @Query("SELECT COALESCE(SUM(e.amount),0) FROM Expense e")
    Double getTotalExpense();

    List<Expense> findByOrn(String orn);

    // ---- Added for per-user data isolation ----
    List<Expense> findByCreatedBy(String createdBy);

    @Query("SELECT COALESCE(SUM(e.amount),0) FROM Expense e WHERE e.createdBy = :createdBy")
    Double getTotalExpenseByCreatedBy(@Param("createdBy") String createdBy);
    
}
