package com.abhinav.spendwise.Services;

import java.util.List;
import java.util.UUID;

import com.abhinav.spendwise.Domain.Dtos.ExpenseRequest;
import com.abhinav.spendwise.Domain.Dtos.ExpenseResponse;
import com.abhinav.spendwise.Domain.Entity.User;

public interface ExpenseService {

    ExpenseResponse createExpense(ExpenseRequest request, User user);
    List<ExpenseResponse> getCurrentMonthExpenses(User user);
    Double getCurrentMonthTotal(User user);
    List<ExpenseResponse> getExpensesByMonth(User user, int year, int month);
    ExpenseResponse getExpenseById(UUID id, User user);
    ExpenseResponse updateExpense(UUID id, ExpenseRequest request, User user);
    void deleteExpense(UUID id, User user);
    List<ExpenseResponse> getExpenseByCategory(UUID categoryId, User user);
}
