package aChaushev.architects.model.enums;

public enum UserRoleEnum {
  ADMIN,
  USER;

  @Override
  public String toString() {
    return name(); // or customize the string representation
  }
}
