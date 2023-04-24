package com.ust.employee.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.ust.employee.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * CustomUserDetails is a class that implements the UserDetails interface and
 * provides custom user details to the Spring Security framework.
 */
public class CustomUserDetails implements UserDetails {

    // The user's email address.
    private String email;

    // The user's password.
    private String password;

    // The user's role.
    private Set<Roles> role;

//    public CustomUserDetails(String email, String password, Set<Roles> role) {
//        this.email = email;
//        this.password = password;
//        this.role = role;
//    }
/**
     * Constructs a new CustomUserDetails object with the given User object.
     *
     * @param user The User object to get details from.
     */
        public CustomUserDetails(User user) {
            this.email = user.getEmail();
             this.password = user.getPassword();
             this.role = user.getRole();

}



    /**
     * Returns the authorities granted to the user.
     *
     * @return A collection of granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new LinkedHashSet<>();
        for (Roles role : role) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
        }
        return authorities;
    }

    /**
     * Returns the user's password.
     *
     * @return The user's password.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the user's email address.
     *
     * @return The user's email address.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Returns whether the user's account is not expired.
     *
     * @return True if the user's account is not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Returns whether the user's account is not locked.
     *
     * @return True if the user's account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Returns whether the user's credentials are not expired.
     *
     * @return True if the user's credentials are not expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Returns whether the user is enabled.
     *
     * @return True if the user is enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
