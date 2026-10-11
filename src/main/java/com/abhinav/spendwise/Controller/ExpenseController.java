package com.abhinav.spendwise.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.abhinav.spendwise.Domain.Dtos.ExpenseRequest;
import com.abhinav.spendwise.Domain.Dtos.ExpenseResponse;
import com.abhinav.spendwise.Domain.Entity.User;
import com.abhinav.spendwise.Services.ExpenseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
@Slf4j
public class ExpenseController {

    private final ExpenseService expenseService;

    /**
     * Create a new expense for the authenticated user
     */
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody ExpenseRequest request,
                                                         @AuthenticationPrincipal(expression = "user") User user) {
        log.info("Creating expense for user: {}", user.getEmail());
        ExpenseResponse response = expenseService.createExpense(request, user);
        log.debug("Expense created successfully: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all expenses for the current month
     */
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getCurrentMonthExpenses(@AuthenticationPrincipal(expression = "user") User user) {
        log.info("Fetching current month expenses for user: {}", user.getEmail());
        List<ExpenseResponse> expenseResponses = expenseService.getCurrentMonthExpenses(user);
        log.debug("Found {} expenses for current month", expenseResponses.size());
        return ResponseEntity.ok(expenseResponses);
    }

    /**
     * Get total expense for the current month
     */
    @GetMapping("/current-month/total")
    public ResponseEntity<Double> getCurrentMonthTotal(@AuthenticationPrincipal(expression = "user") User user) {
        log.info("Fetching current month total for user: {}", user.getEmail());
        Double total = expenseService.getCurrentMonthTotal(user);
        log.debug("Total expense for current month: {}", total);
        return ResponseEntity.ok(total);
    }

    /**
     * Get expenses by specific month and year
     */
    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByMonth(@PathVariable int year,
                                                                    @PathVariable int month,
                                                                    @AuthenticationPrincipal(expression = "user") User user) {
        log.info("Fetching expenses for user: {} for {}/{}", user.getEmail(), month, year);
        List<ExpenseResponse> expenses = expenseService.getExpensesByMonth(user, year, month);
        log.debug("Found {} expenses for {}/{}", expenses.size(), month, year);
        return ResponseEntity.ok(expenses);
    }

    /**
     * Get expense by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable UUID id,
                                                          @AuthenticationPrincipal(expression = "user") User user) {
        log.info("Fetching expense by ID: {} for user: {}", id, user.getEmail());
        ExpenseResponse expense = expenseService.getExpenseById(id, user);
        log.debug("Expense found: {}", expense);
        return ResponseEntity.ok(expense);
    }

    /**
     * Update an expense by ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable UUID id,
                                                         @Valid @RequestBody ExpenseRequest request,
                                                         @AuthenticationPrincipal(expression = "user") User user) {
        log.info("Updating expense with ID: {} for user: {}", id, user.getEmail());
        ExpenseResponse updatedExpense = expenseService.updateExpense(id, request, user);
        log.debug("Expense updated successfully: {}", updatedExpense);
        return ResponseEntity.ok(updatedExpense);
    }

    /**
     * Delete an expense by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable UUID id,
                                           @AuthenticationPrincipal(expression = "user") User user) {
        log.info("Deleting expense with ID: {} for user: {}", id, user.getEmail());
        expenseService.deleteExpense(id, user);
        log.debug("Expense deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all expenses for a given category
     */
    @GetMapping("/category/{categoryId}/expense")
    public ResponseEntity<List<ExpenseResponse>> getExpenseByCategory(@PathVariable UUID categoryId,
                                                                      @AuthenticationPrincipal(expression = "user") User user) {
        log.info("Fetching expenses for category: {} and user: {}", categoryId, user.getEmail());
        List<ExpenseResponse> expenses = expenseService.getExpenseByCategory(categoryId, user);
        log.debug("Found {} expenses for category: {}", expenses.size(), categoryId);
        return ResponseEntity.ok(expenses);
    }
}
