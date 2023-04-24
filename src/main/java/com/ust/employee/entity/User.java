package com.ust.employee.entity;

import com.ust.employee.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Set;
@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class User {
    private String email;
    private String password;
    private Set<Roles> role;

    public static Object builder() {
        return null;
    }
}
