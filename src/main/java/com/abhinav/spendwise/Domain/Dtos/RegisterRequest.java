package com.abhinav.spendwise.Domain.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message="Name is mandatory")
    private String name;
    @Email
    @NotBlank(message="Email is mandatory")
    private String Email;
    @Size(min = 8)
    private String password;
}
