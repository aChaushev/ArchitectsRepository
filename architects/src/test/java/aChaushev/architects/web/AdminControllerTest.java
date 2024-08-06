package aChaushev.architects.web;

import aChaushev.architects.model.dto.UserDetailsDTO;
import aChaushev.architects.model.entity.User;
import aChaushev.architects.model.entity.UserRole;
import aChaushev.architects.model.enums.UserRoleEnum;
import aChaushev.architects.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


public class AdminControllerTest {


    private MockMvc mockMvc;

    @InjectMocks
    private AdminController adminController;

    @Mock
    private UserService userService;

    @Captor
    private ArgumentCaptor<UserDetailsDTO> userDetailsDTOCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testListUsers() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setRoles(Collections.singletonList(
                new UserRole().setId(1L).setRole(UserRoleEnum.USER)
        ));

        when(userService.findAllUsers()).thenReturn(Collections.singletonList(user));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin/users"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"))
                .andExpect(MockMvcResultMatchers.model().attribute("users", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("username", is("user1")),
                                hasProperty("email", is("user1@example.com")),
                                hasProperty("roles", hasItem(
                                        allOf(
                                                hasProperty("id", is(1L)),
                                                hasProperty("role", is(UserRoleEnum.USER))
                                        )
                                ))
                        )
                )));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testShowEditUserForm() throws Exception {
        // Arrange
        // Create and configure UserRole
        UserRole userRole = new UserRole();
        userRole.setId(1L);
        userRole.setRole(UserRoleEnum.USER);

        // Create and configure User
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setRoles(Collections.singletonList(userRole));

        // Create and configure expected UserDetailsDTO
        UserDetailsDTO expectedDTO = new UserDetailsDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles()
        );

        // Mocking service behavior
        when(userService.findUserById(1L)).thenReturn(Optional.of(user));

        // Perform GET request and validate response
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/users/edit/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("user", expectedDTO))
                .andExpect(MockMvcResultMatchers.view().name("admin/edit-user"));
    }





    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUpdateUser() throws Exception {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(
                1L,
                "user1",
                "user1@example.com",
                Collections.singletonList(
                        new UserRole().setId(1L).setRole(UserRoleEnum.USER)
                )
        );

        doNothing().when(userService).updateUser(eq(1L), any(UserDetailsDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/users/edit/1")
                        .flashAttr("user", userDetailsDTO))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/users"));

        verify(userService).updateUser(eq(1L), userDetailsDTOCaptor.capture());
        UserDetailsDTO capturedDTO = userDetailsDTOCaptor.getValue();

        assertEquals(userDetailsDTO.getId(), capturedDTO.getId());
        assertEquals(userDetailsDTO.getUsername(), capturedDTO.getUsername());
        assertEquals(userDetailsDTO.getEmail(), capturedDTO.getEmail());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/users/delete/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/users"));

        verify(userService).deleteUser(1L);
    }
}
