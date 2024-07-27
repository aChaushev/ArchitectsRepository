package aChaushev.architects.model.enums;

public enum UserRoleEnum {
  ADMIN, //todo: admin have to manage user and arch profiles
//todo: if have time add ARCHITECT,
  USER;

  @Override
  public String toString() {
    return name(); // or customize the string representation
  }
}
