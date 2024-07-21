package aChaushev.architects.service.impl;

import aChaushev.architects.model.dto.ProjectAddDTO;
import aChaushev.architects.model.dto.ProjectDTO;
import aChaushev.architects.model.entity.ArchProjectType;
import aChaushev.architects.model.entity.Project;
import aChaushev.architects.model.entity.User;
import aChaushev.architects.repository.ArchProjectTypeRepository;
import aChaushev.architects.repository.ProjectRepository;
import aChaushev.architects.repository.UserRepository;
import aChaushev.architects.service.ExRateService;
import aChaushev.architects.service.ProjectService;
import aChaushev.architects.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ArchProjectTypeRepository archProjectTypeRepository;
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ExRateService exRateService;

    public ProjectServiceImpl(ArchProjectTypeRepository archProjectTypeRepository
            , ProjectRepository projectRepository
            , ModelMapper modelMapper
            , UserRepository userRepository
            , ExRateService exRateService
    ) {
        this.archProjectTypeRepository = archProjectTypeRepository;
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.exRateService = exRateService;
    }


    @Override
    public void addProject(ProjectAddDTO projectAddDTO, Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User not found", userId));
        Project project = this.modelMapper.map(projectAddDTO, Project.class);

        ArchProjectType archProjectType = archProjectTypeRepository.findByProjectTypeName(projectAddDTO.getTypeName());

        if (archProjectType == null) {
            throw new ObjectNotFoundException("Project type not found", projectAddDTO.getTypeName().toString());
        }

        project.setArchProjectType(archProjectType);
        project.setArchitect(user);
        projectRepository.save(project);

    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = this.projectRepository.findAll();
        List<ProjectDTO> projectDTOs = new ArrayList<>();

        for (Project project : projects) {
            if (project.getArchitect() != null) {
                ProjectDTO projectDTO = this.modelMapper.map(project, ProjectDTO.class);
                projectDTOs.add(projectDTO);
            }
        }

        return projectDTOs;
    }

    @Override
    public List<ProjectDTO> getCurrentArchitectProjects(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        List<Project> projects = this.projectRepository.findByArchitect(user);
        List<ProjectDTO> currentArchProjectDTOs = new ArrayList<>();

        for (Project project : projects) {
            ProjectDTO currentArchProject = this.modelMapper.map(project, ProjectDTO.class);
            currentArchProjectDTOs.add(currentArchProject);
        }

        return currentArchProjectDTOs;
    }

    @Override
    public void removeProject(Long ProjectId) {
        projectRepository.deleteById(ProjectId);
    }

    @Override
    public List<ProjectDTO> getOtherArchitectsProjects(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        List<Project> projects = this.projectRepository.findByArchitectIsNot(user);
        List<ProjectDTO> otherArchProjectDTOs = new ArrayList<>();

        for (Project project : projects) {
            ProjectDTO otherArchProject = this.modelMapper.map(project, ProjectDTO.class);
            otherArchProjectDTOs.add(otherArchProject);
        }

        return otherArchProjectDTOs;
    }

    @Override
    public List<ProjectDTO> getFavouriteProjects(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Collections.emptyList(); // Handle user not found
        }
        List<ProjectDTO> favouriteProjectsDTOs = new ArrayList<>();
        for (Project project : user.getFavouriteProjects()) {
            favouriteProjectsDTOs.add(this.modelMapper.map(project, ProjectDTO.class));
        }
        return favouriteProjectsDTOs;
    }


    @Override
    @Transactional
    public void addToFavourites(Long userId, Long projectId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Project> projectOpt = projectRepository.findById(projectId);

        if (userOpt.isEmpty() || projectOpt.isEmpty()) {
            return; // Handle errors or notify user
        }

        User user = userOpt.get();
        Project project = projectOpt.get();

        // Check if the project is already in the user's favourites
        if (!user.getFavouriteProjects().contains(project)) {
            user.getFavouriteProjects().add(project);
            project.setFavorite(true); // This might not be necessary if the relationship is enough
            userRepository.save(user); // Persist changes to the user
        }
    }


    @Override
    @Transactional
    public void removeFromFavourites(Long userId, Long projectId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Project> projectOpt = projectRepository.findById(projectId);

        if (userOpt.isEmpty() || projectOpt.isEmpty()) {
            return;
        }

        User user = userOpt.get();
        Project project = projectOpt.get();

        user.removeFavouriteProject(project);
        project.setFavorite(false);

        userRepository.save(user);
        projectRepository.save(project);
    }

    @Override
    public ProjectDTO getProjectDetails(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid project ID");
        }

        List<String> allCurrencies = exRateService.allSupportedCurrencies();
        List<String> filteredCurrencies = exRateService.filterCurrencies(allCurrencies);

        return this.projectRepository
                .findById(id)
                .map(project -> {
                    ProjectDTO projectDTO = this.modelMapper.map(project, ProjectDTO.class);
                    // Optionally, set the supported currencies to the ProjectDTO if needed
                    projectDTO.setAllCurrencies(filteredCurrencies);
                    return projectDTO;
                })
                .orElseThrow(() -> new ObjectNotFoundException("Project not found", id));
    }

    @Override
    public boolean isProjectOwner(Long projectId, Long userId) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            return project.getArchitect().getId().equals(userId);
        }
        return false;
    }

}


