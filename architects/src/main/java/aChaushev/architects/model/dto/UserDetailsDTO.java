package aChaushev.architects.model.dto;

import aChaushev.architects.model.entity.UserRole;
import aChaushev.architects.model.enums.UserRoleEnum;

import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsDTO {

    private Long id;
    private String username;
    private String email;
    private List<UserRoleEnum> roles;
    private String password;

    public UserDetailsDTO(Long id, String username, String email, List<UserRole> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles.stream().map(UserRole::getRole).collect(Collectors.toList());
        // Convert UserRole to UserRoleEnum
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<UserRoleEnum> getRoles() { return roles; }
    public void setRoles(List<UserRoleEnum> roles) { this.roles = roles; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRoleNames() {
        return roles.stream()
                .map(UserRoleEnum::name)
                .collect(Collectors.joining(", "));
    }
}
