package aChaushev.architects.web;



import aChaushev.architects.service.ProjectService;
import aChaushev.architects.user.LoggedUser;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProjectService projectService;

    private final LoggedUser loggedUser;

    public HomeController(ProjectService projectService, LoggedUser loggedUser) {
        this.projectService = projectService;
        this.loggedUser = loggedUser;
    }

    @GetMapping("/")
    public String getIndexPage() {
        if (loggedUser.isLogged()) {
            return "redirect:/home";
        }
        return "index";

    }

    @GetMapping("/home")
    public String getHomePage() {
        if (!loggedUser.isLogged()) {
            return "redirect:/";
        }
        return "home";
    }

//    @GetMapping("/home")
//    public String getHomePage(Model model) {
//        if (!loggedUser.isLogged()) {
//            return "redirect:/";
//        }
//
//        List<ProjectDTO> allProjects = projectService.getAllProjects();
//        model.addAttribute("allProjects", allProjects);
//
//        List<ProjectDTO> currentArchitectProjects = projectService.getCurrentArchitectProjects(this.loggedUser.getId());
//        model.addAttribute("currentArchitectProjects", currentArchitectProjects);
//
//        List<ProjectDTO> otherProjects = projectService.getOtherArchitectsProjects(this.loggedUser.getId());
//        model.addAttribute("otherProjects", otherProjects);
//
//        List<ProjectDTO> favouriteProjects = projectService.getFavouriteProjects(this.loggedUser.getId());
//        model.addAttribute("favouriteProjects", favouriteProjects);
//
//        return "home";
//    }
}
