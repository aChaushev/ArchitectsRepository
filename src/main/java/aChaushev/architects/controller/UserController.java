//package aChaushev.architects.controller;
//
//import aChaushev.architects.service.UserService;
//import aChaushev.architects.user.LoggedUser;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class UserController {
//
//    private final UserService userService;
//
//    private final LoggedUser loggedUser;
//
//    public UserController(UserService userService, LoggedUser loggedUser) {
//        this.userService = userService;
//        this.loggedUser = loggedUser;
//    }
//
//    @ModelAttribute("userLoginDTO")
//    public UserLoginDTO initLoginBindingModel() {
//        return new UserLoginDTO();
//    }
//
//    @GetMapping("/login")
//    public String login() {
//        if (loggedUser.isLogged()) {
//            return "redirect:/home";
//        }
//        return "login";
//    }
//
//    @PostMapping("/login")
//    public String confirmLogin(@Valid UserLoginDTO userLoginDTO,
//                               BindingResult bindingResult,
//                               RedirectAttributes redirectAttributes) {
//
//        if (bindingResult.hasErrors()) {
//            redirectAttributes
//                    .addFlashAttribute("userLoginDTO", userLoginDTO)
//                    .addFlashAttribute("org.springframework.validation.BindingResult.userLoginDTO", bindingResult);
//
//            return "redirect:/login";
//        }
//
//        boolean hasValidCredentials = this.userService.login(userLoginDTO);
//
//        if (!hasValidCredentials) {
//            redirectAttributes
//                    .addFlashAttribute("userLoginDTO", userLoginDTO)
//                    .addFlashAttribute("badCredentials", true);
//
//            return "redirect:/login";
//        }
//
//        this.userService.login(userLoginDTO);
//        return "redirect:/home";
//    }
//
//    @GetMapping("/logout")
//    public String logout() {
//        if(loggedUser.isLogged()){
//            this.userService.logout();
//            return "redirect:/";
//        }
//        return "redirect:/";
//    }
//}
