package com.abhinav.spendwise.Services;

import com.abhinav.spendwise.Domain.Dtos.RegisterRequest;
import com.abhinav.spendwise.Domain.Entity.User;

public interface UserService {

    User registerUser(RegisterRequest request);
}
