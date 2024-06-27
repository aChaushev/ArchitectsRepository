package aChaushev.architects.web;


import aChaushev.architects.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/project")
public class ProjectDetailController {

//    private final ProjectService projectService;
//
//    public ProjectDetailController(ProjectService projectService) {
//        this.projectService = projectService;
//    }

//    @GetMapping("/{id}")
//    public String projectDetails(@PathVariable("id") Long id, Model model) {
//
//        model.addAttribute("projectDetails", projectService.getProjectDetails(id));
//
//        return "/projects/details";
//    }

//    @DeleteMapping("/{id}")
//    public String deleteOffer(@PathVariable("id") Long id) {
//
//        projectService.removeProject(id);
//
//        return "redirect:/home";
//    }

}
