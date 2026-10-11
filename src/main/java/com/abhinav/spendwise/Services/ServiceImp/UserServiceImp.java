package com.abhinav.spendwise.Services.ServiceImp;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.abhinav.spendwise.Domain.Dtos.RegisterRequest;
import com.abhinav.spendwise.Domain.Entity.User;
import com.abhinav.spendwise.Repository.UserRepository;
import com.abhinav.spendwise.Services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    /**
     * Registers a new user.
     * - Encodes password before saving.
     * - Validates email uniqueness.
     * - Assigns default role "USER".
     */
    @Override
    public User registerUser(RegisterRequest request) {
        // Encode raw password before persisting
        request.setPassword(encoder.encode(request.getPassword()));

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DataIntegrityViolationException("User with email " + request.getEmail() + " already exists");
        }

        // Build new user entity
        User user = User.builder()
                        .username(request.getName())
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .role("USER") // Default role
                        .build();

        // Save user to DB
        return userRepository.save(user);
    }
}
