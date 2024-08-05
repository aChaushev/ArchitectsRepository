package aChaushev.architects.model.dto;

import aChaushev.architects.model.entity.UserRole;

import java.util.List;
import java.util.Objects;
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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<UserRole> getRoles() { return roles; }
    public void setRoles(List<UserRole> roles) { this.roles = roles; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRoleNames() {
        return roles.stream()
                .map(role -> role.getRole().name()) // Assuming `getRole()` returns an enum
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsDTO that = (UserDetailsDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(email, that.email) &&
                Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, roles);
    }

}
