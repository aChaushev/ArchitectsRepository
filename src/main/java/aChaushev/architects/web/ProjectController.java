package aChaushev.architects.web;

import aChaushev.architects.model.dto.ProjectAddDTO;
import aChaushev.architects.model.dto.ProjectDTO;
import aChaushev.architects.model.enums.ArchProjectTypeName;
import aChaushev.architects.service.ProjectService;
import aChaushev.architects.user.LoggedUser;
import jakarta.validation.Valid;
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

    private final LoggedUser loggedUser;

    public ProjectController(ProjectService projectService, LoggedUser loggedUser) {
        this.projectService = projectService;
        this.loggedUser = loggedUser;
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
    public String getAllProjects(Model model) {
        if (!loggedUser.isLogged()) {
            return "redirect:/users/login";
        }

        List<ProjectDTO> allProjects = projectService.getAllProjects();
        model.addAttribute("allProjects", allProjects);

        List<ProjectDTO> currentArchitectProjects = projectService.getCurrentArchitectProjects(this.loggedUser.getId());
        model.addAttribute("currentArchitectProjects", currentArchitectProjects);

        List<ProjectDTO> otherProjects = projectService.getOtherArchitectsProjects(this.loggedUser.getId());
        model.addAttribute("otherProjects", otherProjects);

        List<ProjectDTO> favouriteProjects = projectService.getFavouriteProjects(this.loggedUser.getId());
        model.addAttribute("favouriteProjects", favouriteProjects);

        return "projects/all-projects";
    }

    @GetMapping("/add")
    public String getAddProject() {
        if(!loggedUser.isLogged()){
            return "redirect:/users/login";

        }

        return "projects/add-project";
    }

    @PostMapping("/add")
    public String doAddProject(
            @Valid ProjectAddDTO projectAddDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (!loggedUser.isLogged()) {
            return "redirect:/users/login";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("projectAddDTO", projectAddDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.projectAddDTO", bindingResult);

            return "redirect:/project/add";
        }

        this.projectService.addProject(projectAddDTO);

        return "redirect:/project/all";
    }

    @GetMapping("/favourites/{id}")
    public String addToFavourites(@PathVariable("id") Long projectId) {
        if (!loggedUser.isLogged()) {
            return "redirect:/users/login";
        }

        projectService.addToFavourites(loggedUser.getId(), projectId);

        return "redirect:/project/add";
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
        if (!loggedUser.isLogged()) {
            return "redirect:/users/login";
        }

        model.addAttribute("projectDetails", projectService.getProjectDetails(projectId));

        return "/projects/details";
    }


    //TODO: check why @DeleteMapping not work
    @DeleteMapping("/remove/{id}")
    public String removeProject(@PathVariable("id") Long projectId) {
        if(!loggedUser.isLogged()){
            return "redirect:/users/login";
        }

        this.projectService.removeProject(projectId);
        return "redirect:/project/all";

    }


}
