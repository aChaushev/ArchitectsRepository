package aChaushev.architects.web.it;

import aChaushev.architects.model.dto.UserRegisterDTO;
import aChaushev.architects.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRegistrationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testShowRegistrationForm() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/register"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/users/register"))
                .andExpect(model().attributeExists("userRegisterDTO"));
    }

    @Test
    public void testRegisterUserSuccess() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("testuser");
        userRegisterDTO.setEmail("testuser@example.com");
        userRegisterDTO.setPassword("password");
        userRegisterDTO.setConfirmPassword("password");

        when(userService.register(any(UserRegisterDTO.class))).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .flashAttr("userRegisterDTO", userRegisterDTO))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));

        verify(userService, times(1)).register(any(UserRegisterDTO.class));
    }

    @Test
    public void testRegisterUserWithErrors() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("te"); // invalid username
        userRegisterDTO.setEmail("testuser"); // invalid email
        userRegisterDTO.setPassword("pass"); // invalid password length
        userRegisterDTO.setConfirmPassword("password"); // passwords do not match

        this.mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .flashAttr("userRegisterDTO", userRegisterDTO))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"))
                .andExpect(flash().attributeExists("userRegisterDTO"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.userRegisterDTO"));

        verify(userService, never()).register(any(UserRegisterDTO.class));
    }
}

