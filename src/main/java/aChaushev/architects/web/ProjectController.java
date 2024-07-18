package aChaushev.architects.web;

import aChaushev.architects.model.dto.ProjectAddDTO;
import aChaushev.architects.model.dto.ProjectDTO;
import aChaushev.architects.model.enums.ArchProjectTypeName;
import aChaushev.architects.model.user.AppUserDetails;
import aChaushev.architects.service.ProjectService;
import aChaushev.architects.service.UserService;
import aChaushev.architects.service.exception.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
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
            @Valid ProjectAddDTO projectAddDTO,
            @AuthenticationPrincipal AppUserDetails userDetails,
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

    //TODO: now shows all users favourites -> should show current user favourites
    @GetMapping("/favourites/{id}")
    public String addToFavourites(@PathVariable("id") Long projectId,
                                  @AuthenticationPrincipal AppUserDetails userDetails) {

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

//    @ResponseStatus(code = HttpStatus.NOT_FOUND)
//    @ExceptionHandler(ObjectNotFoundException.class)
//    public ModelAndView handleObjectNotFound(ObjectNotFoundException objectNotFoundException) {
//        ModelAndView modelAndView = new ModelAndView("error/project-not-found");
//        modelAndView.addObject("projectId", objectNotFoundException.getId());
//        return modelAndView;
//    }


    //for delete request use <form> in html and "spring.mvc.hiddenmethod.filter.enabled=true" in app.prop to work
    // correctly
    @DeleteMapping("/remove/{id}")
    public String removeProject(@PathVariable("id") Long projectId) {

        this.projectService.removeProject(projectId);
        return "redirect:/project/all";

    }


}
