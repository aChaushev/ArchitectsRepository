package aChaushev.architects.service.impl;

import aChaushev.architects.model.entity.User;
import aChaushev.architects.model.entity.UserRoleEntity;
import aChaushev.architects.model.enums.UserRoleEnum;
import aChaushev.architects.model.user.ArchRepoUserDetails;
import aChaushev.architects.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ArchRepoUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public ArchRepoUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .map(ArchRepoUserDetailsService::customMap)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User with username " + username + " not found!"));
    }

    private static UserDetails customMap(User user) {

        return new ArchRepoUserDetails(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(UserRoleEntity::getRole).map(ArchRepoUserDetailsService::customMap).toList()
        );
    }

    private static GrantedAuthority customMap(UserRoleEnum role) {
        return new SimpleGrantedAuthority(
                "ROLE_" + role
        );
    }

}


