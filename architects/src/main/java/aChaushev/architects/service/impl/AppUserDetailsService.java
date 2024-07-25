package aChaushev.architects.service.impl;

import aChaushev.architects.model.entity.User;
import aChaushev.architects.model.entity.UserRole;
import aChaushev.architects.model.enums.UserRoleEnum;
import aChaushev.architects.model.user.AppUserDetails;
import aChaushev.architects.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .map(AppUserDetailsService::mapToUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));
    }

    private static UserDetails mapToUserDetails(User user) {
        return new AppUserDetails(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(UserRole::getRole)
                        .map(AppUserDetailsService::mapToUserRole)
                        .toList(),
                user.getId(),
                user.getEmail()
        );
    }

    private static GrantedAuthority mapToUserRole(UserRoleEnum role) {
        return new SimpleGrantedAuthority(
                "ROLE_" + role
        );
    }
}


