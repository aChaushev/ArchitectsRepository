package aChaushev.architects.web;

import aChaushev.architects.model.dto.UserRegisterDTO;
import aChaushev.architects.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/users")
@Controller
public class UserRegistrationController {

  private final UserService userService;

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("userRegisterDTO")
  public UserRegisterDTO initRegisterDTO() {
    return new UserRegisterDTO();
  }

  @GetMapping("/register")
  public String register() {
    return "/users/register";
  }

  @PostMapping("/register")
  public String confirmRegister(@Valid UserRegisterDTO userRegisterDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors() || !this.userService.register(userRegisterDTO)) {
      redirectAttributes
              .addFlashAttribute("userRegisterDTO", userRegisterDTO)
              .addFlashAttribute("org.springframework.validation.BindingResult.userRegisterDTO", bindingResult);

      return "redirect:/users/register";
    }

    this.userService.register(userRegisterDTO);

    return "redirect:/users/login";
  }
}
