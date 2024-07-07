package aChaushev.architects.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AppUserDetails extends User {

//  private final String firstName;
//  private final String lastName;
    private final Long id;

    public AppUserDetails(String username, String password,
                          Collection<? extends GrantedAuthority> authorities, Long id) {
        super(username, password, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}


