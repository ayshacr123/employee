package com.ust.employee.dto;


import com.ust.employee.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Represents a request to register a new user.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    /**
     * The email address of the user to be registered.
     */
    private String email;

    /**
     * The password of the user to be registered.
     */
    private String password;

    /**
     * The roles to be assigned to the user upon registration.
     */
    private Set<Roles> roles;

}
