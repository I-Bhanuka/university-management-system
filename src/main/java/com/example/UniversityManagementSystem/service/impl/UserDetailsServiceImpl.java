package com.example.UniversityManagementSystem.service.impl;

import com.example.UniversityManagementSystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This is the UserDetailsServiceImpl that implements the UserDetailsService interface to load the user by username and get the user details for authentication and authorization
 *
 * This is a custom implementation as the loading of the user details can be different for each applocation.
 * There can be API loading, in memory loading, database loading etc.
 */

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // Constructor injection
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NullMarked
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Loading user by username: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username));

    }
}
