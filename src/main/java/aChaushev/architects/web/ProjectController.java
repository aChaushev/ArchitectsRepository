package aChaushev.architects.web;

import aChaushev.architects.model.dto.ProjectAddDTO;
import aChaushev.architects.model.enums.ArchProjectTypeName;
import aChaushev.architects.service.ProjectService;
import aChaushev.architects.user.LoggedUser;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/add")
    public String getAddProject() {
        if(loggedUser.isLogged()){
            return "projects/add-project";
        }

        return "redirect:/users/login";
    }

    @PostMapping("/add")
    public String doAddProject(
            @Valid ProjectAddDTO projectAddDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (!loggedUser.isLogged()) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("projectAddDTO", projectAddDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.projectAddDTO", bindingResult);

            return "redirect:/project/add";
        }

        this.projectService.addProject(projectAddDTO);

        return "redirect:/home";
    }

//    @GetMapping("/word/remove/{id}")
//    public String removeWord(@PathVariable("id") Long WordId) {
//        if(loggedUser.isLogged()){
//            this.wordService.removeWord(WordId);
//            return "redirect:/home";
//        }
//
//        return "redirect:/";
//    }
//
//    @GetMapping("/word/remove-all")
//    public String removeAllWords() {
//        if(loggedUser.isLogged()){
//            this.wordService.removeAllWords();
//            return "redirect:/home";
//        }
//
//        return "redirect:/";
//    }


}
