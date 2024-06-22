package aChaushev.architects.controller;

import aChaushev.architects.model.dto.UserLoginDTO;
import aChaushev.architects.service.UserService;
import aChaushev.architects.user.LoggedUser;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/users")
@Controller
public class UserLoginController {


    private final UserService userService;
    private final LoggedUser loggedUser;

    public UserLoginController(UserService userService, LoggedUser loggedUser) {
        this.userService = userService;
        this.loggedUser = loggedUser;
    }

    @ModelAttribute("userLoginDTO")
    public UserLoginDTO initUserLoginDTO() {
        return new UserLoginDTO();
    }


    @GetMapping("/login")
    public String login() {
        if (loggedUser.isLogged()) {
            return "redirect:/home";
        }
        return "users/login";
    }


    @PostMapping("/login")
    public String confirmLogin(@Valid UserLoginDTO userLoginDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("userLoginDTO", userLoginDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userLoginDTO", bindingResult);

            return "redirect:/users/login";
        }

        boolean hasValidCredentials = this.userService.login(userLoginDTO);

        if (!hasValidCredentials) {
            redirectAttributes
                    .addFlashAttribute("userLoginDTO", userLoginDTO)
                    .addFlashAttribute("badCredentials", true);

            return "redirect:/users/login";
        }

        this.userService.login(userLoginDTO);
        return "redirect:/details";
    }

    @GetMapping("/logout")
    public String logout() {
        if (loggedUser.isLogged()) {
            this.userService.logout();
            return "redirect:/";
        }
        return "redirect:/";
    }


//  @PostMapping("/users/login-error")
//  public String onFailure(
//      @ModelAttribute("email") String email,
//      Model model) {
//
//    model.addAttribute("email", email);
//    model.addAttribute("bad_credentials", "true");
//
//    return "auth-login";
//  }
}
