package com.abhinav.spendwise.Security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.abhinav.spendwise.Domain.Entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExpenseUserDetails implements UserDetails {

    private final User user;

    public ExpenseUserDetails(User user) {
        if (user == null) {
            log.error("Attempted to create ExpenseUserDetails with null user");
            throw new IllegalArgumentException("User cannot be null");
        }
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        try {
            // Default authority (can be expanded later if roles are added in DB)
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        } catch (Exception e) {
            log.error("Failed to fetch authorities for user: {}", user.getEmail(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public String getPassword() {
        if (user.getPassword() == null) {
            log.warn("Password is null for user: {}", user.getEmail());
            return "";
        }
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        if (user.getEmail() == null) {
            log.warn("Email (username) is null for user: {}", user.getUsername());
            return "";
        }
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        // Future enhancement: add account expiry logic if required
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Future enhancement: add account locking logic if required
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Future enhancement: add password expiry logic if required
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Future enhancement: check active status from DB (e.g., user.isActive())
        return true;
    }
}
