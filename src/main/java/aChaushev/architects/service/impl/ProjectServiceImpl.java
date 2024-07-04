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
import aChaushev.architects.user.LoggedUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ArchProjectTypeRepository archProjectTypeRepository;
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final LoggedUser loggedUser;
    private final ExRateService exRateService;

    public ProjectServiceImpl(ArchProjectTypeRepository archProjectTypeRepository
            , ProjectRepository projectRepository
            , ModelMapper modelMapper
            , UserRepository userRepository
            , LoggedUser loggedUser
            , ExRateService exRateService) {
        this.archProjectTypeRepository = archProjectTypeRepository;
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.loggedUser = loggedUser;
        this.exRateService = exRateService;
    }


    @Override
    public void addProject(ProjectAddDTO projectAddDTO) {
        Optional<User> user = this.userRepository.findById(loggedUser.getId());
        Project project = this.modelMapper.map(projectAddDTO, Project.class);

        ArchProjectType archProjectType = archProjectTypeRepository.findByProjectTypeName(projectAddDTO.getTypeName());

        project.setArchProjectType(archProjectType);
        project.setArchitect(user.get());
        projectRepository.save(project);
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = this.projectRepository.findAll();
        List<ProjectDTO> projectDTOs = new ArrayList<>();

        for (Project project : projects) {
            if(project.getArchitect() != null) {
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
//        User user = modelMapper.map(this.loggedUser, User.class);
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
                .orElse(null);

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
