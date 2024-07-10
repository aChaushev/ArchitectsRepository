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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
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
    private final RestClient projectsRestClient;

    public ProjectServiceImpl(ArchProjectTypeRepository archProjectTypeRepository
            , ProjectRepository projectRepository
            , ModelMapper modelMapper
            , UserRepository userRepository
            , ExRateService exRateService, @Qualifier("projectsRestClient") RestClient projectsRestClient) {
        this.archProjectTypeRepository = archProjectTypeRepository;
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.exRateService = exRateService;
        this.projectsRestClient = projectsRestClient;
    }


    @Override
    public void addProject(ProjectAddDTO projectAddDTO, Long userId) {
        // without REST API
//        Optional<User> user = this.userRepository.findById(userId);
//        Project project = this.modelMapper.map(projectAddDTO, Project.class);
//
//        ArchProjectType archProjectType = archProjectTypeRepository.findByProjectTypeName(projectAddDTO.getTypeName());
//
//        project.setArchProjectType(archProjectType);
//        project.setArchitect(user.get());
//        projectRepository.save(project);

        // todo - fix baseUrl
        //REST API
        LOGGER.debug("Creating new project...");

        projectsRestClient
                .post()
                .uri("http://localhost:8081/project")
                .body(projectAddDTO)
                .retrieve();
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        // without REST API
//        List<Project> projects = this.projectRepository.findAll();
//        List<ProjectDTO> projectDTOs = new ArrayList<>();
//
//        for (Project project : projects) {
//            if(project.getArchitect() != null) {
//                ProjectDTO projectDTO = this.modelMapper.map(project, ProjectDTO.class);
//                projectDTOs.add(projectDTO);
//            }
//        }
//
//        return projectDTOs;

        LOGGER.info("Get all projects...");

        return projectsRestClient
                .get()
                .uri("http://localhost:8081/project")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }

    @Override
    public List<ProjectDTO> getCurrentArchitectProjects(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        List<Project> projects = this.projectRepository.findByArchitect(user);
        List<ProjectDTO> currentArchProjectDTOs = new ArrayList<>();

        for (Project project : projects)  {
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

        for (Project project : projects)  {
            ProjectDTO otherArchProject = this.modelMapper.map(project, ProjectDTO.class);
            otherArchProjectDTOs.add(otherArchProject);
        }

        return otherArchProjectDTOs;
    }

    @Override
    public List<ProjectDTO> getFavouriteProjects(Long userId) {
//        User user = modelMapper.map(userService.getCurrentUserId(), User.class);
        User user = userRepository.findById(userId).orElse(null);
        List<Project> projects = projectRepository.findByisFavoriteIsTrue(user);
        List<ProjectDTO> favouriteProjectsDTOs = new ArrayList<>();

        for (Project project : projects)  {
            ProjectDTO favouriteProject = this.modelMapper.map(project, ProjectDTO.class);
            favouriteProjectsDTOs.add(favouriteProject);
        }

        return favouriteProjectsDTOs;
    }

    @Override
    @Transactional
    public void addToFavourites(Long userId, Long painingId) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            return;
        }

        Optional<Project> projectOpt = projectRepository.findById(painingId);

        if (projectOpt.isEmpty()) {
            return;
        }

        userOpt.get().addFavouriteProject(projectOpt.get());

        projectOpt.get().setFavorite(true);
        userRepository.save(userOpt.get());
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

//        return projectsRestClient
//                .get()
//                .uri("http://localhost:8081/project/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .body(ProjectDTO.class);

    }
//    @Override
//    public ProjectDTO getProjectDetails(Long id) {
//        return this.projectRepository
//                .findById(id)
//                .map(project ->
//                    this.modelMapper.map(project, ProjectDTO.class))
//                .orElse(null);
//
//    }




//    @Override
//    public void removeFromFavourites(Long userId, Long painingId) {
//        List<PaintingDTO> favouritePaintingDTOs = getFavouritePaintings(userId);
//
//        for (PaintingDTO favouritePaintingDTO : favouritePaintingDTOs) {
//            if(favouritePaintingDTO.getId().equals(painingId)){
//                removePainting(painingId);
//            }
//        }
//    }
}
