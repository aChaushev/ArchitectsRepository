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
            model.addAttribute("welcomeMessage", appUserDetails.getUsername());
        } else {
          model.addAttribute("welcomeMessage", "");
        }

        return "index";

    }

    @GetMapping("/home")
    public String getHomePage() {
        return "home";
    }

}
