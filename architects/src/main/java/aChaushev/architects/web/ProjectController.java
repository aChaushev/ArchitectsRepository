package aChaushev.architects.web;

import aChaushev.architects.model.dto.ProjectAddDTO;
import aChaushev.architects.model.dto.ProjectDTO;
import aChaushev.architects.model.enums.ArchProjectTypeName;
import aChaushev.architects.model.user.AppUserDetails;
import aChaushev.architects.repository.ProjectRepository;
import aChaushev.architects.service.ProjectService;
import aChaushev.architects.service.exception.ObjectAlreadyExistsException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/project")
@Controller
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    public ProjectController(ProjectService projectService, ProjectRepository projectRepository, ModelMapper modelMapper) {
        this.projectService = projectService;
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
    }


    @ModelAttribute("projectAddDTO")
    public ProjectAddDTO initProjectAddDTO() {
        return new ProjectAddDTO();
    }

    @ModelAttribute("allArchTypes")
    public ArchProjectTypeName[] allArchTypes() {
        return ArchProjectTypeName.values();
    }

    @GetMapping("/all")
    public String getAllProjects(@AuthenticationPrincipal AppUserDetails userDetails, Model model) {
        Long userId = userDetails.getId();

        List<ProjectDTO> allProjects = projectService.getAllProjects();
        model.addAttribute("allProjects", allProjects);

        List<ProjectDTO> currentArchitectProjects = projectService.getCurrentArchitectProjects(userId);
        model.addAttribute("currentArchitectProjects", currentArchitectProjects);

        List<ProjectDTO> otherProjects = projectService.getOtherArchitectsProjects(userId);
        model.addAttribute("otherProjects", otherProjects);

        List<ProjectDTO> favouriteProjects = projectService.getFavouriteProjects(userId);
        model.addAttribute("favouriteProjects", favouriteProjects);

        return "projects/all-projects";
    }

    @GetMapping("/add")
    public String getAddProject(Model model) {
        if (!model.containsAttribute("projectAddDTO")) {
            model.addAttribute("projectAddDTO", new ProjectAddDTO());
        }
        return "projects/add-project";
    }

    @PostMapping("/add")
    public String doAddProject(
            @Valid @ModelAttribute("projectAddDTO") ProjectAddDTO projectAddDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal AppUserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("projectAddDTO", projectAddDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.projectAddDTO", bindingResult);

            return "redirect:/project/add";
        }

        if (projectRepository.findByName(projectAddDTO.getName()).isPresent()) {
            throw new ObjectAlreadyExistsException("Project with that name already exist!");
        }

        this.projectService.addProject(projectAddDTO, userDetails.getId());

        return "redirect:/project/all";
    }


    @GetMapping("/favourites/{id}")
    public String addToFavourites(@PathVariable("id") Long projectId,
                                  @AuthenticationPrincipal AppUserDetails userDetails) {
        projectService.addToFavourites(userDetails.getId(), projectId);
        return "redirect:/project/all";
    }


    @GetMapping("/favourites-remove/{id}")
    public String removeFromFavourites(@PathVariable("id") Long projectId,
                                       @AuthenticationPrincipal AppUserDetails userDetails) {
        projectService.removeFromFavourites(userDetails.getId(), projectId);

        return "redirect:/project/all";
    }


    @GetMapping("/{id}")
    public String projectDetails(@PathVariable("id") Long projectId, Model model) {

        model.addAttribute("projectDetails", projectService.getProjectDetails(projectId));

        return "/projects/details";
    }

    @DeleteMapping("/remove/{id}")
    public String removeProject(@PathVariable("id") Long projectId,
                                @AuthenticationPrincipal AppUserDetails userDetails) {
        if (!projectService.isProjectOwner(projectId, userDetails.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this project.");
        }
        this.projectService.removeProject(projectId);
        return "redirect:/project/all";
    }
}


