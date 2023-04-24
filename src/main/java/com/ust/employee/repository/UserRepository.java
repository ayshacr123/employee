package com.ust.employee.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<
        User, Long> {

    User findByUsername(String username);
    Optional<User> findByEmail(String email);
}