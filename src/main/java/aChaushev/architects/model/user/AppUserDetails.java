package aChaushev.architects.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AppUserDetails extends User {

//  private final String firstName;
//  private final String lastName;
    private final Long id;
    private final String email;

    public AppUserDetails(String username, String password,
                          Collection<? extends GrantedAuthority> authorities, Long id, String email) {
        super(username, password, authorities);
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}


