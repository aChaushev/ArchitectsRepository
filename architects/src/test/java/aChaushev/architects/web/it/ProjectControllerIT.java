package aChaushev.architects.web.it;

import aChaushev.architects.model.dto.ProjectAddDTO;
import aChaushev.architects.model.dto.ProjectDTO;
import aChaushev.architects.model.entity.Project;
import aChaushev.architects.model.entity.User;
import aChaushev.architects.model.entity.UserRole;
import aChaushev.architects.model.enums.ArchProjectTypeName;
import aChaushev.architects.model.enums.UserRoleEnum;
import aChaushev.architects.repository.ProjectRepository;
import aChaushev.architects.repository.UserRepository;
import aChaushev.architects.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProjectControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    public void setup() {
        projectRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setRoles(Collections.singletonList(
                new UserRole().setId(1L).setRole(UserRoleEnum.USER)
        ));
        userRepository.save(user);

        Project project = new Project();
        project.setId(1L);
        project.setName("Project1");
        project.setDescription("Description of Project1");
        project.setArchitect(user);
        projectRepository.save(project);
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    public void testGetAllProjects() throws Exception {
        mockMvc.perform(get("/project/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("projects/all-projects"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allProjects"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("currentArchitectProjects"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("otherProjects"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("favouriteProjects"));
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    public void testAddProject() throws Exception {
        mockMvc.perform(get("/project/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("projects/add-project"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("projectAddDTO"));
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    public void testDoAddProject() throws Exception {
        ProjectAddDTO projectAddDTO = new ProjectAddDTO();
        projectAddDTO.setName("Valid Project Name");
        projectAddDTO.setDescription("Valid description of the project");
        projectAddDTO.setPrice(new BigDecimal("1500"));
        projectAddDTO.setInputDate(LocalDate.now());
        projectAddDTO.setImageURL("http://example.com/image.jpg");
        projectAddDTO.setTypeName(ArchProjectTypeName.RESIDENTIAL);

        mockMvc.perform(post("/project/add")
                        .flashAttr("projectAddDTO", projectAddDTO))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/project/all"));
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    public void testRemoveFromFavourites() throws Exception {
        mockMvc.perform(get("/project/favourites-remove/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/project/all"));

        Mockito.verify(projectService, Mockito.times(1)).removeFromFavourites(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    public void testProjectDetails() throws Exception {
        Mockito.when(projectService.getProjectDetails(Mockito.anyLong())).thenReturn(new ProjectDTO());

        mockMvc.perform(get("/project/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/projects/details"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("projectDetails"));
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    public void testRemoveProject() throws Exception {
        Mockito.when(projectService.isProjectOwner(Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/project/remove/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/project/all"));

        Mockito.verify(projectService, Mockito.times(1)).removeProject(Mockito.anyLong());
    }
}

