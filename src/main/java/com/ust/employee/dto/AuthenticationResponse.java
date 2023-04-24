package com.ust.employee.dto;


import com.ust.employee.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * Represents the response returned upon successful authentication.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    /**
     * The JSON Web Token (JWT) generated upon successful authentication.
     */
    private String token;

    /**
     * The user associated with the authenticated token.
     */

    private User user;
}
