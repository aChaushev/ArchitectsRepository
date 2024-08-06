package aChaushev.architects.web;

import aChaushev.architects.config.I18NConfig;
import aChaushev.architects.model.dto.ProjectAddDTO;
import aChaushev.architects.model.dto.ProjectDTO;
import aChaushev.architects.model.entity.User;
import aChaushev.architects.model.enums.ArchProjectTypeName;
import aChaushev.architects.model.user.AppUserDetails;
import aChaushev.architects.repository.ProjectRepository;
import aChaushev.architects.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProjectController.class)
@Import(I18NConfig.class) // Import the I18NConfig class to ensure the beans are available
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testGetAllProjects() throws Exception {
        // Arrange
        User mockArchitect = new User();
        mockArchitect.setUsername("architectUsername");
        mockArchitect.setId(1L); // Ensure ID is set if needed

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(1L);
        projectDTO.setName("Test Project");
        projectDTO.setDescription("Test Description");
        projectDTO.setImageURL("/images/test.png");
        projectDTO.setArchitect(mockArchitect); // Set architect here

        // Mock service methods
        Mockito.when(projectService.getAllProjects()).thenReturn(Collections.singletonList(projectDTO));
        Mockito.when(projectService.getCurrentArchitectProjects(anyLong())).thenReturn(Collections.singletonList(projectDTO));
        Mockito.when(projectService.getOtherArchitectsProjects(anyLong())).thenReturn(Collections.singletonList(projectDTO));
        Mockito.when(projectService.getFavouriteProjects(anyLong())).thenReturn(Collections.singletonList(projectDTO));

        // Act and Assert
        mockMvc.perform(get("/project/all")
                        .with(SecurityMockMvcRequestPostProcessors.user("testUser").password("password").roles("USER"))
                        .with(csrf())) // Add CSRF token if required
                .andExpect(status().isOk())
                .andExpect(view().name("projects/all-projects"))
                .andExpect(model().attributeExists("allProjects"))
                .andExpect(model().attributeExists("currentArchitectProjects"))
                .andExpect(model().attributeExists("otherProjects"))
                .andExpect(model().attributeExists("favouriteProjects"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAddProject() throws Exception {
        mockMvc.perform(get("/project/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/add-project"))
                .andExpect(model().attributeExists("projectAddDTO"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDoAddProject() throws Exception {
        ProjectAddDTO projectAddDTO = new ProjectAddDTO();
        projectAddDTO.setName("Test Project");
        projectAddDTO.setDescription("Test Description");
        projectAddDTO.setPrice(new BigDecimal("1000"));
        projectAddDTO.setInputDate(LocalDate.now());
        projectAddDTO.setImageURL("http://example.com/image.jpg");
        projectAddDTO.setTypeName(ArchProjectTypeName.RESIDENTIAL);

        when(projectRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Create a mock AppUserDetails
        AppUserDetails mockAppUserDetails = new AppUserDetails(
                "user",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                1L,
                "user@example.com"
        );

        mockMvc.perform(post("/project/add")
                        .with(SecurityMockMvcRequestPostProcessors.user(mockAppUserDetails)) // Set the mock user
                        .with(csrf()) // Add CSRF token
                        .flashAttr("projectAddDTO", projectAddDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/project/all"));

        verify(projectService, times(1)).addProject(any(ProjectAddDTO.class), anyLong());
    }



    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDoAddProjectWithErrors() throws Exception {
        ProjectAddDTO projectAddDTO = new ProjectAddDTO();
        projectAddDTO.setName(""); // Invalid name to trigger validation error
        projectAddDTO.setDescription("Test Description");
        projectAddDTO.setPrice(new BigDecimal("1000"));
        projectAddDTO.setInputDate(LocalDate.now());
        projectAddDTO.setImageURL("http://example.com/image.jpg");
        projectAddDTO.setTypeName(ArchProjectTypeName.RESIDENTIAL);

        mockMvc.perform(post("/project/add")
                        .flashAttr("projectAddDTO", projectAddDTO)
                        .with(csrf()))  // Add CSRF token
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/project/add"))
                .andExpect(flash().attributeExists("projectAddDTO"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.projectAddDTO"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAddToFavourites() throws Exception {
        mockMvc.perform(get("/project/favourites/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/project/all"));

        verify(projectService, times(1)).addToFavourites(anyLong(), eq(1L));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testRemoveFromFavourites() throws Exception {
        mockMvc.perform(get("/project/favourites-remove/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/project/all"));

        verify(projectService, times(1)).removeFromFavourites(anyLong(), eq(1L));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testProjectDetails() throws Exception {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(1L);
        projectDTO.setName("Test Project");
        projectDTO.setDescription("Test Description");

        when(projectService.getProjectDetails(anyLong())).thenReturn(projectDTO);

        mockMvc.perform(get("/project/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/projects/details"))
                .andExpect(model().attributeExists("projectDetails"))
                .andExpect(model().attribute("projectDetails", projectDTO));
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testRemoveProject() throws Exception {
        when(projectService.isProjectOwner(anyLong(), anyLong())).thenReturn(true);

        // Create a mock AppUserDetails
        AppUserDetails mockAppUserDetails = new AppUserDetails(
                "user",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                1L,
                "user@example.com"
        );

        mockMvc.perform(delete("/project/remove/1")
                        .with(SecurityMockMvcRequestPostProcessors.user(mockAppUserDetails)) // Set the mock user
                        .with(csrf())) // Add CSRF token
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/project/all"));

        verify(projectService, times(1)).removeProject(anyLong());
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testRemoveProjectAccessDenied() throws Exception {
        when(projectService.isProjectOwner(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(delete("/project/remove/1"))
                .andExpect(status().isForbidden());
    }
}
