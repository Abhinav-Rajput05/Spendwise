package com.abhinav.spendwise.Security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.abhinav.spendwise.Domain.Entity.User;
import com.abhinav.spendwise.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExpenseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                                    .orElseThrow(() -> new UsernameNotFoundException("User not found with Email : " + email));

                        
                    return new ExpenseUserDetails(user);
    }

}
