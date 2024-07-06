package aChaushev.architects.service;

import aChaushev.architects.model.dto.ProjectAddDTO;
import aChaushev.architects.model.dto.ProjectDTO;
import aChaushev.architects.model.user.ArchRepoUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    ProjectDTO getProjectDetails(Long id);
}
