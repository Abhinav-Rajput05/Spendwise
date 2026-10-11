package com.abhinav.spendwise.Repository;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abhinav.spendwise.Domain.Entity.Category;
import com.abhinav.spendwise.Domain.Entity.Expense;
import com.abhinav.spendwise.Domain.Entity.User;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

     List<Expense> findByUserAndDateBetweenOrderByDateDesc(
            @Param("user") User user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

     @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e where e.user = :user " + 
                    "AND e.date BETWEEN :startDate AND :endDate")
     Optional<Double> getTotalAmountByUserAndDateRange(@Param("user") User user,
                              @Param("startDate") LocalDate startDate,
                              @Param("endDate") LocalDate endDate );
     
     Optional<Expense> findByIdAndUser(UUID id, User user);
     List<Expense> findByCategoryAndUser(Category category, User user);
     @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e where e.category = :category " + 
                    "AND e.user =:user AND e.date BETWEEN :startDate AND :endDate")
     Double getTotalAmountByCategoryAndUserAndDateRange(@Param("category") Category category, 
                                                                  @Param("user") User user,
                                                                  @Param("startDate") LocalDate startDate,
                                                                  @Param("endDate") LocalDate endDate); 
}
