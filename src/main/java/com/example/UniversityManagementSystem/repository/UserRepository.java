package com.example.UniversityManagementSystem.repository;

import com.example.UniversityManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


/**
 *
 * This is the UserRepository that helps the UserDetailsServiceImpl to load the user by username and get the user details for authentication and authorization
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);
}
