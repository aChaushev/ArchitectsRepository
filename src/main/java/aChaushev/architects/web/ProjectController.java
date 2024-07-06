package aChaushev.architects.web;

import aChaushev.architects.model.dto.ProjectAddDTO;
import aChaushev.architects.model.dto.ProjectDTO;
import aChaushev.architects.model.enums.ArchProjectTypeName;
import aChaushev.architects.model.user.ArchRepoUserDetails;
import aChaushev.architects.service.ProjectService;
import aChaushev.architects.service.UserService;
import jakarta.validation.Valid;
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
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
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
    public String getAllProjects(@AuthenticationPrincipal ArchRepoUserDetails userDetails, Model model) {
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
    public String getAddProject() {
        return "projects/add-project";
    }

    @PostMapping("/add")
    public String doAddProject(
            @Valid ProjectAddDTO projectAddDTO,
            @AuthenticationPrincipal ArchRepoUserDetails userDetails,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("projectAddDTO", projectAddDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.projectAddDTO", bindingResult);

            return "redirect:/project/add";
        }

        this.projectService.addProject(projectAddDTO, userDetails.getId());

        return "redirect:/project/all";
    }

    //TODO: show all users favourites, should show current user favourites
    @GetMapping("/favourites/{id}")
    public String addToFavourites(@PathVariable("id") Long projectId,
                                  @AuthenticationPrincipal ArchRepoUserDetails userDetails) {

        projectService.addToFavourites(userDetails.getId(), projectId);

        return "redirect:/project/all";
    }

//    @GetMapping("/favourites-remove/{id}")
//    public String removeFromFavourites(@PathVariable("id") Long projectId) {
//        if (!loggedUser.isLogged()) {
//            return "redirect:/users/login";
//        }
//
//        projectService.removeFromFavourites(loggedUser.getId(), projectId);
//
//        return "redirect:/project/add";
//    }

    @GetMapping("/{id}")
    public String projectDetails(@PathVariable("id") Long projectId, Model model) {

        model.addAttribute("projectDetails", projectService.getProjectDetails(projectId));

        return "/projects/details";
    }


    //for delete request use <form> in html and "spring.mvc.hiddenmethod.filter.enabled=true" in app.prop to work
    // correctly
    @DeleteMapping("/remove/{id}")
    public String removeProject(@PathVariable("id") Long projectId) {

        this.projectService.removeProject(projectId);
        return "redirect:/project/all";

    }


}
