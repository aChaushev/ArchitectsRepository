package aChaushev.architects.web;

import aChaushev.architects.model.dto.UserRegisterDTO;
import aChaushev.architects.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserRegistrationControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserRegistrationController userRegistrationController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userRegistrationController).build();
    }

    @Test
    public void testRegisterPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/register"))
                .andExpect(model().attributeExists("userRegisterDTO"));
    }

    @Test
    public void testConfirmRegisterSuccess() throws Exception {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("user");
        userRegisterDTO.setEmail("user@example.com");
        userRegisterDTO.setPassword("password");
        userRegisterDTO.setConfirmPassword("password");

        when(userService.register(any(UserRegisterDTO.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .flashAttr("userRegisterDTO", userRegisterDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));

        verify(userService).register(any(UserRegisterDTO.class));
    }

    @Test
    public void testConfirmRegisterFailure() throws Exception {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("user");
        userRegisterDTO.setEmail("user@example.com");
        userRegisterDTO.setPassword("password");
        userRegisterDTO.setConfirmPassword("wrongpassword");

        when(userService.register(any(UserRegisterDTO.class))).thenReturn(false);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .flashAttr("userRegisterDTO", userRegisterDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"))
                .andExpect(flash().attributeExists("userRegisterDTO"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.userRegisterDTO"));
    }

    @Test
    public void testConfirmRegisterWithErrors() throws Exception {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername(""); // Invalid username
        userRegisterDTO.setEmail("invalid-email"); // Invalid email
        userRegisterDTO.setPassword("short"); // Invalid password
        userRegisterDTO.setConfirmPassword("mismatch"); // Password mismatch

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .flashAttr("userRegisterDTO", userRegisterDTO)
                        .flashAttr("org.springframework.validation.BindingResult.userRegisterDTO", bindingResult))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/register"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("userRegisterDTO"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.userRegisterDTO"));
    }
}

