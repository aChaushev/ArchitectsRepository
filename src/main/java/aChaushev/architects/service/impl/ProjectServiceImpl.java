package aChaushev.architects.service.impl;

import aChaushev.architects.model.dto.ProjectAddDTO;
import aChaushev.architects.model.entity.ArchProjectType;
import aChaushev.architects.model.entity.Project;
import aChaushev.architects.model.entity.User;
import aChaushev.architects.repository.ArchProjectTypeRepository;
import aChaushev.architects.repository.ProjectRepository;
import aChaushev.architects.repository.UserRepository;
import aChaushev.architects.service.ProjectService;
import aChaushev.architects.user.LoggedUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ArchProjectTypeRepository archProjectTypeRepository;
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final LoggedUser loggedUser;

    public ProjectServiceImpl(ArchProjectTypeRepository archProjectTypeRepository, ProjectRepository projectRepository, ModelMapper modelMapper, UserRepository userRepository, LoggedUser loggedUser) {
        this.archProjectTypeRepository = archProjectTypeRepository;
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.loggedUser = loggedUser;
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
}
