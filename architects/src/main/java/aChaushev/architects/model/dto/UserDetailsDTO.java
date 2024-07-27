package aChaushev.architects.model.dto;

import aChaushev.architects.model.entity.UserRole;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDetailsDTO {

    private Long id;
    private String username;
    private String email;
    private List<UserRole> roles;
    private String password;

    public UserDetailsDTO(Long id, String username, String email, List<UserRole> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    // Method to get role names as a comma-separated string
    public String getRoleNames() {
        return roles.stream()
                .map(role -> role.getRole().name()) // Assuming `getRole()` returns an enum
                .collect(Collectors.joining(", "));
    }

    public String getPassword() {
        return password;
    }
}
