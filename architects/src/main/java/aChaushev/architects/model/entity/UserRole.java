package aChaushev.architects.model.entity;

import aChaushev.architects.model.enums.UserRoleEnum;
import jakarta.persistence.*;


@Entity
@Table(name = "roles")
public class UserRole {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private UserRoleEnum role;

  public Long getId() {
    return id;
  }

  public UserRole setId(Long id) {
    this.id = id;
    return this;
  }

  public UserRoleEnum getRole() {
    return role;
  }

  public UserRole setRole(UserRoleEnum role) {
    this.role = role;
    return this;
  }
}
