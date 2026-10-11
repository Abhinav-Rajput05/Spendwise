package com.abhinav.spendwise.Services.ServiceImp;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abhinav.spendwise.Domain.Dtos.ExpenseRequest;
import com.abhinav.spendwise.Domain.Dtos.ExpenseResponse;
import com.abhinav.spendwise.Domain.Entity.Category;
import com.abhinav.spendwise.Domain.Entity.Expense;
import com.abhinav.spendwise.Domain.Entity.User;
import com.abhinav.spendwise.Mapper.ExpenseMapper;
import com.abhinav.spendwise.Repository.ExpenseRepository;
import com.abhinav.spendwise.Services.CategoryService;
import com.abhinav.spendwise.Services.ExpenseService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImp implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;
    private final ExpenseMapper expenseMapper;

    /**
     * Creates a new expense for the given user.
     * Validates category access before saving.
     */
    @Override
    @Transactional
    public ExpenseResponse createExpense(ExpenseRequest request, User user) {
        // Ensure category belongs to the user
        categoryService.validateCategoryAccess(request.getCategoryId(), user);

        // Map request DTO to entity
        Expense expense = expenseMapper.toEntity(request);
        expense.setUser(user);
        expense.setCategory(categoryService.getCategoryById(request.getCategoryId()));

        // Save expense and map back to response DTO
        Expense savedExpense = expenseRepository.save(expense);
        return expenseMapper.toResponse(savedExpense);
    }

    /**
     * Fetches all expenses for the current month for a given user.
     * Returns empty list if no expenses exist.
     */
    @Override
    public List<ExpenseResponse> getCurrentMonthExpenses(User user) {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        List<Expense> expenses = expenseRepository.findByUserAndDateBetweenOrderByDateDesc(
                user, firstDayOfMonth, lastDayOfMonth);

        return expenses.stream()
                       .map(expenseMapper::toResponse)
                       .toList();
    }

    /**
     * Calculates total expense amount for the current month.
     * Returns 0.0 if no expenses are found.
     */
    @Override
    public Double getCurrentMonthTotal(User user) {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        return expenseRepository.getTotalAmountByUserAndDateRange(user, firstDayOfMonth, lastDayOfMonth)
                                .orElse(0.0);
    }

    /**
     * Fetches all expenses for a given month/year.
     */
    @Override
    public List<ExpenseResponse> getExpensesByMonth(User user, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Expense> expenses = expenseRepository.findByUserAndDateBetweenOrderByDateDesc(
                user, startDate, endDate);

        return expenses.stream()
                       .map(expenseMapper::toResponse)
                       .toList();
    }

    /**
     * Fetches a specific expense by ID for a given user.
     * Throws EntityNotFoundException if not found.
     */
    @Override
    public ExpenseResponse getExpenseById(UUID id, User user) {
        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found with id: " + id));

        return expenseMapper.toResponse(expense);
    }

    /**
     * Updates an existing expense.
     * Validates category access if category is changed.
     */
    @Override
    @Transactional
    public ExpenseResponse updateExpense(UUID id, ExpenseRequest request, User user) {
        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found with id: " + id));

        // If category changed, validate access
        if (!expense.getCategory().getId().equals(request.getCategoryId())) {
            categoryService.validateCategoryAccess(request.getCategoryId(), user);
            expense.setCategory(categoryService.getCategoryById(request.getCategoryId()));
        }

        // Update fields
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setDate(request.getDate() != null ? request.getDate() : expense.getDate());

        Expense updatedExpense = expenseRepository.save(expense);
        return expenseMapper.toResponse(updatedExpense);
    }

    /**
     * Deletes an expense by ID for the given user.
     * Throws EntityNotFoundException if not found.
     */
    @Override
    public void deleteExpense(UUID id, User user) {
        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found with id: " + id));

        expenseRepository.delete(expense);
    }

    /**
     * Fetches all expenses for a category for a given user.
     * Validates category access before fetching.
     */
    @Override
    public List<ExpenseResponse> getExpenseByCategory(UUID categoryId, User user) {
        categoryService.validateCategoryAccess(categoryId, user);
        Category category = categoryService.getCategoryById(categoryId);

        List<Expense> expenses = expenseRepository.findByCategoryAndUser(category, user);

        return expenses.stream()
                       .map(expenseMapper::toResponse)
                       .toList();
    }
}
