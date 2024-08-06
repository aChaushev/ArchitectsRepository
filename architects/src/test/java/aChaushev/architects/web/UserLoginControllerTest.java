package aChaushev.architects.web;

import aChaushev.architects.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class UserLoginControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserLoginController userLoginController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userLoginController).build();
    }

    @Test
    public void testLoginPageWithoutError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("users/login"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("error"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testLoginPageWithError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/login")
                        .param("error", "true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("users/login"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", "Invalid username or password."))
                .andDo(MockMvcResultHandlers.print());
    }
}
