package com.ust.employee.service;


import com.ust.employee.dto.AuthenticationRequest;
import com.ust.employee.dto.AuthenticationResponse;
import com.ust.employee.dto.RegisterRequest;
import com.ust.employee.exception.EmailAlreadyExistException;
import com.ust.employee.exception.UserNotFoundException;
import com.ust.employee.model.CustomUserDetails;
import com.ust.employee.model.Roles;
import com.ust.employee.entity.User;
import com.ust.employee.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 * Service responsible for user authentication and registration.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user with the provided information.
     *
     * @param request the register request containing the user information
     * @return an authentication response containing the user and null token
     * @throws EmailAlreadyExistException if the email is already in use
     */
    public AuthenticationResponse register(RegisterRequest request) throws EmailAlreadyExistException {
        Optional<User> existingUser = repository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistException("Email already in use");
        }

        Set<Roles> roles = request.getRoles();

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roles)
                .build();

        repository.save(user);
        return AuthenticationResponse.builder()
                .token(null)
                .user(user)
                .build();
    }

    /**
     * Authenticates the user with the provided information.
     *
     * @param request the authentication request containing the user information
     * @return an authentication response containing the user and JWT token
     * @throws UserNotFoundException if the user is not found
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws UserNotFoundException {
        Optional<User> existingUser = repository.findByEmail(request.getEmail());
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User not found with email: " + request.getEmail());
        }
        boolean isActive = true;

        if (isActive) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } else {
            throw new UserNotFoundException("User not authorized");
        }

        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        var jwtToken = jwtService.generateToken(customUserDetails);
        var user1 = User.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(user1)
                .build();
    }
}