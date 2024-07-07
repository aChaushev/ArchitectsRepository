package aChaushev.architects.web;

import aChaushev.architects.model.dto.UserLoginDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UserLoginController {

    @GetMapping("/login")
    public String login() {
        return "users/login";
    }

//TODO: check to show error message
//    @PostMapping("/users/login-error")
//    public String onFailure(
//            @ModelAttribute("username") String username,
//            Model model) {
//
//        model.addAttribute("username", username);
//        model.addAttribute("bad_credentials", "true");
//
//        return "users/login";
//    }
}
