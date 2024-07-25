package aChaushev.architects.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AppUserDetails extends User {

    private final Long id;
    private final String email;

    public AppUserDetails(String username, String password,
                          Collection<? extends GrantedAuthority> authorities, Long id, String email) {
        super(username, password, authorities);
        //        super(Objects.requireNonNull(username, "Username cannot be null"),
        //                Objects.requireNonNull(password, "Password cannot be null"),
        //                Objects.requireNonNull(authorities, "Authorities cannot be null"));
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


