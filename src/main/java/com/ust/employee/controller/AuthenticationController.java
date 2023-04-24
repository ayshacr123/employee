package com.ust.employee.controller;



import com.ust.employee.dto.AuthenticationRequest;
import com.ust.employee.dto.AuthenticationResponse;
import com.ust.employee.dto.ErrorResponse;
import com.ust.employee.dto.RegisterRequest;
import com.ust.employee.exception.EmailAlreadyExistException;
import com.ust.employee.exception.UserNotFoundException;
import com.ust.employee.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * A Rest controller for handling authentication related requests
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired

    private final AuthenticationService authenticationService;

    /**
     * Registers a new user and returns a response entity with authentication details
     * @param request A request object containing user details
     * @return A response entity with authentication details
     * @throws EmailAlreadyExistException if the provided email already exists in the system
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthenticationResponse response = authenticationService.register(request);
            return ResponseEntity.ok(response);
        } catch (EmailAlreadyExistException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), "Email already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    /**
     * Returns a simple string response for testing purposes
     * @return A simple string response
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TRAVELLER')")
    @GetMapping("/roletest")
    public String Roletest() {
        return "hello";
    }

    /**
     * Authenticates a user with provided credentials and returns a response entity with authentication details
     * @param request A request object containing user credentials
     * @return A response entity with authentication details
     * @throws UserNotFoundException if the user with provided credentials is not found in the system
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            AuthenticationResponse authresponse = authenticationService.authenticate(request);
            return ResponseEntity.ok(authresponse);
        } catch (UserNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found with email: " + request.getEmail());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

}
