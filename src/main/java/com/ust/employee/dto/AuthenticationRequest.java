package com.ust.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a request to authenticate a user by their email and password.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    /** The email of the user to be authenticated. */
    private String email;

    /** The password of the user to be authenticated. */
    private String password;
}

