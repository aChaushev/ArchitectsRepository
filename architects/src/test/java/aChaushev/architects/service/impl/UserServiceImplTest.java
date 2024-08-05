// може да улови User, който е подаден в даден мок (mockUserRepository)

package aChaushev.architects.service.impl;

import aChaushev.architects.model.dto.UserRegisterDTO;
import aChaushev.architects.model.entity.User;
import aChaushev.architects.model.entity.UserRole;
import aChaushev.architects.model.enums.UserRoleEnum;
import aChaushev.architects.repository.UserRepository;
import aChaushev.architects.repository.UserRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

//@Disabled
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private UserServiceImpl toTest;

    @Captor
    private ArgumentCaptor<User> userEntityCaptor;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserRoleRepository mockUserRoleRepository; // Add mock for UserRoleRepository

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private ModelMapper mockModelMapper;

    @BeforeEach
    void setUp() {
        toTest = new UserServiceImpl(mockUserRepository, mockUserRoleRepository, mockPasswordEncoder, mockModelMapper);

        // Use lenient stubbing for ModelMapper
        lenient().when(mockModelMapper.map(any(UserRegisterDTO.class), eq(User.class))).thenAnswer(invocation -> {
            UserRegisterDTO dto = invocation.getArgument(0);
            User user = new User();
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword());
            return user;
        });
    }

    @Test
    void testUserRegistration() {
        // Arrange
        UserRegisterDTO userRegistrationDTO = new UserRegisterDTO();
        userRegistrationDTO.setUsername("user43");
        userRegistrationDTO.setPassword("123");
        userRegistrationDTO.setConfirmPassword("123");
        userRegistrationDTO.setEmail("u43@u43");

        when(mockPasswordEncoder.encode(userRegistrationDTO.getPassword()))
                .thenReturn(userRegistrationDTO.getPassword() + userRegistrationDTO.getPassword());

        when(mockUserRepository.findByUsernameAndEmail(userRegistrationDTO.getUsername(), userRegistrationDTO.getEmail()))
                .thenReturn(Optional.empty());

        UserRole userRole = new UserRole();
        userRole.setRole(UserRoleEnum.USER);
        when(mockUserRoleRepository.findByRole(UserRoleEnum.USER))
                .thenReturn(Optional.of(userRole));

        // ACT
        boolean isRegistered = toTest.register(userRegistrationDTO);

        // Assert
        Assertions.assertTrue(isRegistered);

        verify(mockUserRepository).save(userEntityCaptor.capture());
        User actualSavedEntity = userEntityCaptor.getValue();

        Assertions.assertNotNull(actualSavedEntity);
        Assertions.assertEquals(userRegistrationDTO.getUsername(), actualSavedEntity.getUsername());
        Assertions.assertEquals(userRegistrationDTO.getPassword() + userRegistrationDTO.getPassword(),
                actualSavedEntity.getPassword());
        Assertions.assertEquals(userRegistrationDTO.getEmail(), actualSavedEntity.getEmail());
    }

    @Test
    void testUserRegistrationPasswordsDoNotMatch() {
        // Arrange
        UserRegisterDTO userRegistrationDTO = new UserRegisterDTO();
        userRegistrationDTO.setUsername("user43");
        userRegistrationDTO.setPassword("123");
        userRegistrationDTO.setConfirmPassword("321"); // Different confirm password
        userRegistrationDTO.setEmail("u43@u43");

        // Act
        boolean isRegistered = toTest.register(userRegistrationDTO);

        // Assert
        Assertions.assertFalse(isRegistered);
        verify(mockUserRepository, never()).save(any());
    }

    @Test
    void testUserRegistrationUserAlreadyExists() {
        // Arrange
        UserRegisterDTO userRegistrationDTO = new UserRegisterDTO();
        userRegistrationDTO.setUsername("user43");
        userRegistrationDTO.setPassword("123");
        userRegistrationDTO.setConfirmPassword("123");
        userRegistrationDTO.setEmail("u43@u43");

        User existingUser = new User();
        when(mockUserRepository.findByUsernameAndEmail(userRegistrationDTO.getUsername(), userRegistrationDTO.getEmail()))
                .thenReturn(Optional.of(existingUser));

        // Act
        boolean isRegistered = toTest.register(userRegistrationDTO);

        // Assert
        Assertions.assertFalse(isRegistered);
        verify(mockUserRepository, never()).save(any());
    }

}
