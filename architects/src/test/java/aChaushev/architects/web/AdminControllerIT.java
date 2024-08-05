package aChaushev.architects.web;

import aChaushev.architects.model.dto.UserDetailsDTO;
import aChaushev.architects.model.entity.User;
import aChaushev.architects.model.entity.UserRole;
import aChaushev.architects.model.enums.UserRoleEnum;
import aChaushev.architects.repository.UserRepository;
import aChaushev.architects.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        // Clean up database before each test
        userRepository.deleteAll();
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
        userRepository.save(user);

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
        UserRole userRole = new UserRole();
        userRole.setId(1L);
        userRole.setRole(UserRoleEnum.USER);

        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setRoles(Collections.singletonList(userRole));
        userRepository.save(user);

        UserDetailsDTO expectedDTO = new UserDetailsDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles()
        );

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/users/edit/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("user", expectedDTO))
                .andExpect(MockMvcResultMatchers.view().name("admin/edit-user"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUpdateUser() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setRoles(Collections.singletonList(
                new UserRole().setId(1L).setRole(UserRoleEnum.USER)
        ));
        userRepository.save(user);

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(
                1L,
                "user1",
                "user1@example.com",
                Collections.singletonList(
                        new UserRole().setId(1L).setRole(UserRoleEnum.USER)
                )
        );

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/users/edit/1")
                        .flashAttr("user", userDetailsDTO))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/users"));

        User updatedUser = userRepository.findById(1L).orElseThrow();
        assertEquals(userDetailsDTO.getUsername(), updatedUser.getUsername());
        assertEquals(userDetailsDTO.getEmail(), updatedUser.getEmail());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteUser() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setRoles(Collections.singletonList(
                new UserRole().setId(1L).setRole(UserRoleEnum.USER)
        ));
        userRepository.save(user);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/users/delete/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/users"));

        assertEquals(0, userRepository.count());
    }
}
