package com.ust.employee.service;


import com.ust.employee.entity.User;
import com.ust.employee.model.CustomUserDetails;
import com.ust.employee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * A custom implementation of UserDetailsService that retrieves a user from the UserRepository
 * <p>
 * based on the provided email and returns an instance of CustomUserDetails that contains the
 * <p>
 * user's details.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves a user from the UserRepository based on the provided email and returns
     * an instance of CustomUserDetails that contains the user's details.
     *
     * @param username the email of the user to retrieve
     * @return an instance of CustomUserDetails that contains the user's details
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        return new CustomUserDetails(user);
    }
}