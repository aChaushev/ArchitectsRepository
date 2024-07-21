package aChaushev.architects.service;

import aChaushev.architects.model.dto.ProjectAddDTO;
import aChaushev.architects.model.dto.ProjectDTO;
import aChaushev.architects.model.entity.Project;
import aChaushev.architects.model.enums.ArchProjectTypeName;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProjectService {

    void addProject(ProjectAddDTO projectAddDTO, Long userId);

    List<ProjectDTO> getAllProjects();

    List<ProjectDTO> getCurrentArchitectProjects(Long id);

    void removeProject(Long PaintingId);

    List<ProjectDTO> getOtherArchitectsProjects(Long userId);

    List<ProjectDTO> getFavouriteProjects(Long userId);

    @Transactional
    void addToFavourites(Long userId, Long painingId);

    @Transactional
    void removeFromFavourites(Long userId, Long projectId);

    ProjectDTO getProjectDetails(Long id);

    boolean isProjectOwner(Long projectId, Long userId);

}
