package aChaushev.architects.web;



import aChaushev.architects.model.user.AppUserDetails;
import aChaushev.architects.service.ProjectService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProjectService projectService;

    public HomeController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/")
    public String getIndexPage(@AuthenticationPrincipal
                                   UserDetails userDetails,
                               Model model) {

        if (userDetails instanceof AppUserDetails appUserDetails) {
            model.addAttribute("welcomeMessage", appUserDetails.getUsername()); // or getFullName()
        } else {
            model.addAttribute("welcomeMessage", "Anonymous");
        }

        return "index";

    }

    @GetMapping("/home")
    public String getHomePage() {
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
