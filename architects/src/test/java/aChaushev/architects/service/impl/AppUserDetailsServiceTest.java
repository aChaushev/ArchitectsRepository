package aChaushev.architects.service.impl;

import aChaushev.architects.model.entity.User;
import aChaushev.architects.model.entity.UserRole;
import aChaushev.architects.model.enums.UserRoleEnum;
import aChaushev.architects.model.user.AppUserDetails;
import aChaushev.architects.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppUserDetailsServiceTest {

    private static final String TEST_USERNAME = "user3";
    private static final String NOT_EXISTENT_USERNAME = "parhut";

    private AppUserDetailsService toTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        mockUserRepository = Mockito.mock(UserRepository.class);
        toTest = new AppUserDetailsService(mockUserRepository);
    }

    @Test
    void testLoadUserByUsername_UserFound() {

        // Arrange - приготвяме тестови данни
        User testUser = new User();
        testUser.setUsername(TEST_USERNAME);
        testUser.setPassword("123");
        testUser.setEmail("u3@u3");
        testUser.setRoles(List.of(
                new UserRole().setRole(UserRoleEnum.ADMIN),
                new UserRole().setRole(UserRoleEnum.USER)
        ));

        when(mockUserRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(testUser));

        // Act - извикваме класа, който ще се тества
        UserDetails userDetails = toTest.loadUserByUsername(TEST_USERNAME);

        // Assert - проверка дали реултата ни удовлетворява
        Assertions.assertInstanceOf(AppUserDetails.class, userDetails);

        AppUserDetails appUserDetails = (AppUserDetails) userDetails;

        Assertions.assertEquals(TEST_USERNAME, userDetails.getUsername());
        Assertions.assertEquals(testUser.getPassword(), userDetails.getPassword());
        Assertions.assertEquals(testUser.getEmail(), appUserDetails.getEmail());

        var expectedRoles = testUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().name()))
                .toList();
        var actualRoles = userDetails.getAuthorities().stream()
                .toList();

        Assertions.assertEquals(expectedRoles, actualRoles);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(mockUserRepository.findByUsername(NOT_EXISTENT_USERNAME))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> toTest.loadUserByUsername(NOT_EXISTENT_USERNAME)
        );
    }


}


