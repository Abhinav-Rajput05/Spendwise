package com.abhinav.spendwise.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abhinav.spendwise.Domain.Dtos.AuthResponse;
import com.abhinav.spendwise.Domain.Dtos.LoginRequest;
import com.abhinav.spendwise.Domain.Dtos.RegisterRequest;
import com.abhinav.spendwise.Services.AuthenticationService;
import com.abhinav.spendwise.Services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for handling authentication requests.
 * Exposes endpoints for user registration and login.
 *
 * Base path: /api/v1/auth
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    /**
     * Register a new user.
     *
     * @param registerRequest DTO containing user registration details.
     * @return 201 Created with success message if registration is successful.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
       try{
        userService.registerUser(registerRequest);
        log.info("User registered successfully: {}", registerRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                    .body("{\"message\": \"User registered successfully\"}");
       }catch(IllegalArgumentException e){
        log.error("Registration error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
    }catch(Exception e){
        log.error("Unexpected error during registration: {}", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Something went wrong. Please try again later.\"}");
    }
}

    /**
     * Authenticate a user and return a JWT token.
     *
     * @param request DTO containing login credentials (email, password).
     * @return AuthResponse containing JWT token and expiry.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
         try{
        // Validate user credentials
        UserDetails userDetails = authenticationService.authenticate(
                request.getEmail(),
                request.getPassword()
        );

        // Generate JWT token for authenticated user
        String tokenValue = authenticationService.generateToken(userDetails);

        // Build response with token and hardcoded expiry (24h = 86400 seconds)
        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expiresIn(86400)
                .build();
                return ResponseEntity.ok(authResponse);

     } catch(BadCredentialsException e){
            log.warn("User logged in successfully: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
     }catch(Exception e)
     {
        log.error("Unexpected error during login: {}", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
     }
        
    }
}
