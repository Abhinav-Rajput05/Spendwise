package com.abhinav.spendwise.Domain.Dtos;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequest {

    @NotNull(message="category Id is mandatory")
    private UUID categoryId;

    @Positive(message="Amount must be positive")
    private Double amount;

    private String description;

    private LocalDate date;

}
