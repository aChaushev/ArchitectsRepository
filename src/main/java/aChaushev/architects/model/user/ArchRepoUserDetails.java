package aChaushev.architects.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class ArchRepoUserDetails extends User {

//  private final String firstName;
//  private final String lastName;

  public ArchRepoUserDetails(
      String username,
      String password,
      Collection<? extends GrantedAuthority> authorities
//      ,String firstName,
//      String lastName
  ) {
    super(username, password, authorities);
//    this.firstName = firstName;
//    this.lastName = lastName;
  }

/*    public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFullName() {
    StringBuilder fullName = new StringBuilder();
    if (firstName != null) {
      fullName.append(firstName);
    }
    if (lastName != null) {
      if (!fullName.isEmpty()) {
        fullName.append(" ");
      }
      fullName.append(lastName);
    }

    return fullName.toString();
  }
  */
}
