package aChaushev.architects.web;

import aChaushev.architects.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
@RequestMapping("/users")
public class UserLoginController {

    private final UserService userService;
    private final MessageSource messageSource;

    public UserLoginController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model, Locale locale) {
        if (error != null) {
            String errorMessage = messageSource.getMessage("login.form.error_msg.overall", null, locale);
            model.addAttribute("error", errorMessage);
        }
        return "users/login";
    }

}

