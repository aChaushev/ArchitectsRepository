package aChaushev.architects.service.impl;

import aChaushev.architects.model.dto.ProjectAddDTO;
import aChaushev.architects.model.dto.ProjectDTO;
import aChaushev.architects.model.entity.ArchProjectType;
import aChaushev.architects.model.entity.Project;
import aChaushev.architects.model.entity.User;
import aChaushev.architects.model.enums.ArchProjectTypeName;
import aChaushev.architects.repository.ArchProjectTypeRepository;
import aChaushev.architects.repository.ProjectRepository;
import aChaushev.architects.repository.UserRepository;
import aChaushev.architects.service.ExRateService;
import aChaushev.architects.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplTest {

    private ProjectServiceImpl toTest;

    @Captor
    private ArgumentCaptor<Project> projectEntityCaptor;

    @Mock
    private ArchProjectTypeRepository mockArchProjectTypeRepository;

    @Mock
    private ProjectRepository mockProjectRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private ModelMapper mockModelMapper;

    @Mock
    private ExRateService mockExRateService;

    @BeforeEach
    void setUp() {
        toTest = new ProjectServiceImpl(mockArchProjectTypeRepository, mockProjectRepository, mockModelMapper, mockUserRepository, mockExRateService);

        // Use lenient stubbing for ModelMapper
        lenient().when(mockModelMapper.map(any(ProjectAddDTO.class), eq(Project.class))).thenAnswer(invocation -> {
            ProjectAddDTO dto = invocation.getArgument(0);
            Project project = new Project();
            project.setName(dto.getName());
            project.setDescription(dto.getDescription());
            project.setPrice(dto.getPrice());
            project.setInputDate(dto.getInputDate());
            project.setImageURL(dto.getImageURL());
            return project;
        });
    }

    @Test
    void testAddProject() {
        // Arrange
        ProjectAddDTO projectAddDTO = new ProjectAddDTO();
        projectAddDTO.setName("New Project");
        projectAddDTO.setDescription("Project Description");
        projectAddDTO.setPrice(new BigDecimal("1000"));
        projectAddDTO.setInputDate(LocalDate.now());
        projectAddDTO.setImageURL("http://image.url");
        projectAddDTO.setTypeName(ArchProjectTypeName.RESIDENTIAL);

        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));

        ArchProjectType archProjectType = new ArchProjectType();
        when(mockArchProjectTypeRepository.findByProjectTypeName(ArchProjectTypeName.RESIDENTIAL)).thenReturn(archProjectType);

        // Act
        toTest.addProject(projectAddDTO, userId);

        // Assert
        verify(mockProjectRepository).save(projectEntityCaptor.capture());
        Project actualSavedEntity = projectEntityCaptor.getValue();

        assertNotNull(actualSavedEntity);
        assertEquals(projectAddDTO.getName(), actualSavedEntity.getName());
        assertEquals(projectAddDTO.getDescription(), actualSavedEntity.getDescription());
        assertEquals(projectAddDTO.getPrice(), actualSavedEntity.getPrice());
        assertEquals(projectAddDTO.getInputDate(), actualSavedEntity.getInputDate());
        assertEquals(projectAddDTO.getImageURL(), actualSavedEntity.getImageURL());
        assertEquals(user, actualSavedEntity.getArchitect());
        assertEquals(archProjectType, actualSavedEntity.getArchProjectType());
    }

    @Test
    void testAddProject_UserNotFound() {
        // Arrange
        ProjectAddDTO projectAddDTO = new ProjectAddDTO();
        projectAddDTO.setName("New Project");
        projectAddDTO.setDescription("Project Description");
        projectAddDTO.setPrice(new BigDecimal("1000"));
        projectAddDTO.setInputDate(LocalDate.now());
        projectAddDTO.setImageURL("http://image.url");
        projectAddDTO.setTypeName(ArchProjectTypeName.RESIDENTIAL);

        Long userId = 1L;

        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class, () -> toTest.addProject(projectAddDTO, userId));
        verify(mockProjectRepository, never()).save(any());
    }

    @Test
    void testAddProject_ProjectTypeNotFound() {
        // Arrange
        ProjectAddDTO projectAddDTO = new ProjectAddDTO();
        projectAddDTO.setName("New Project");
        projectAddDTO.setDescription("Project Description");
        projectAddDTO.setPrice(new BigDecimal("1000"));
        projectAddDTO.setInputDate(LocalDate.now());
        projectAddDTO.setImageURL("http://image.url");
        projectAddDTO.setTypeName(ArchProjectTypeName.RESIDENTIAL);

        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mockArchProjectTypeRepository.findByProjectTypeName(ArchProjectTypeName.RESIDENTIAL)).thenReturn(null);

        // Act & Assert
        assertThrows(ObjectNotFoundException.class, () -> toTest.addProject(projectAddDTO, userId));
        verify(mockProjectRepository, never()).save(any());
    }

    @Test
    void testGetAllProjects() {
        // Arrange
        Project project = new Project();
        project.setName("Project 1");
        project.setDescription("Description 1");
        project.setPrice(new BigDecimal("1000"));
        project.setInputDate(LocalDate.now());
        project.setImageURL("http://image.url");
        User architect = new User();
        architect.setId(1L);
        project.setArchitect(architect);

        when(mockProjectRepository.findAll()).thenReturn(List.of(project));

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Project 1");
        projectDTO.setDescription("Description 1");
        projectDTO.setPrice(new BigDecimal("1000"));
        projectDTO.setInputDate(LocalDate.now());
        projectDTO.setImageURL("http://image.url");
        projectDTO.setArchitect(architect);
        when(mockModelMapper.map(project, ProjectDTO.class)).thenReturn(projectDTO);

        // Act
        List<ProjectDTO> result = toTest.getAllProjects();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Project 1", result.get(0).getName());
        assertEquals("Description 1", result.get(0).getDescription());
        assertEquals(new BigDecimal("1000"), result.get(0).getPrice());
        assertEquals(LocalDate.now(), result.get(0).getInputDate());
        assertEquals("http://image.url", result.get(0).getImageURL());
    }

    @Test
    void testGetCurrentArchitectProjects() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Project project = new Project();
        project.setName("Project 1");
        project.setDescription("Description 1");
        project.setPrice(new BigDecimal("1000"));
        project.setInputDate(LocalDate.now());
        project.setImageURL("http://image.url");
        project.setArchitect(user);

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mockProjectRepository.findByArchitect(user)).thenReturn(List.of(project));

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Project 1");
        projectDTO.setDescription("Description 1");
        projectDTO.setPrice(new BigDecimal("1000"));
        projectDTO.setInputDate(LocalDate.now());
        projectDTO.setImageURL("http://image.url");
        projectDTO.setArchitect(user);
        when(mockModelMapper.map(project, ProjectDTO.class)).thenReturn(projectDTO);

        // Act
        List<ProjectDTO> result = toTest.getCurrentArchitectProjects(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Project 1", result.get(0).getName());
        assertEquals("Description 1", result.get(0).getDescription());
        assertEquals(new BigDecimal("1000"), result.get(0).getPrice());
        assertEquals(LocalDate.now(), result.get(0).getInputDate());
        assertEquals("http://image.url", result.get(0).getImageURL());
    }

    // Add more tests for other methods...
}
