package com.abhinav.spendwise.Configuration;

import java.util.List;

import com.abhinav.spendwise.Security.ExpenseUserDetailsService;
import com.abhinav.spendwise.Security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Spring Security configuration for the ExpenseTracker application.
 * Handles authentication, authorization, password encoding, and JWT filter integration.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ExpenseUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructor injection of dependencies.
     * @Lazy ensures JwtAuthenticationFilter is only created when needed, 
     * avoiding circular dependency issues.
     */
    public SecurityConfig(ExpenseUserDetailsService userDetailsService,
                          @Lazy JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * BCryptPasswordEncoder bean used to securely hash and verify passwords.
     * Strength 12 is chosen for good balance between security and performance.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * AuthenticationProvider configuration.
     * Uses DaoAuthenticationProvider with custom UserDetailsService
     * and BCrypt password encoder.
     */
    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // Load user from DB
        provider.setPasswordEncoder(passwordEncoder());     // Verify with BCrypt
        return provider;
    }

    /**
     * Core security filter chain.
     * - Disables CSRF for stateless APIs
     * - Defines authorization rules
     * - Enforces stateless session policy
     * - Registers custom authentication provider
     * - Inserts JWT filter before username/password filter
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF disabled for REST APIs (JWT handles security)
                .csrf(csrf -> csrf.disable())

                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Allow cross-origin

                // Endpoint access rules
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/auth/**").permitAll() // Public endpoints (login/register)
                        .anyRequest().authenticated())                  // Everything else requires auth

                // No HTTP sessions (each request authenticated with JWT)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Use custom authentication provider (Dao + BCrypt)
                .authenticationProvider(authProvider())

                // Validate JWT before hitting UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /*
     * CORS configuration to allow cross-origin requests.
     */

     @Bean
     public CorsConfigurationSource corsConfigurationSource() {
         CorsConfiguration configuration = new CorsConfiguration();
         configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Adjust as needed
         configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
         configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
         configuration.setAllowCredentials(true);
         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
         source.registerCorsConfiguration("/**", configuration);
         return source;
     }

    /**
     * Expose AuthenticationManager as a Spring bean.
     * Used by controllers (e.g. login) to authenticate users programmatically.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
