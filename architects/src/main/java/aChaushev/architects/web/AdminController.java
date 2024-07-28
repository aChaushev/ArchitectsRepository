package aChaushev.architects.web;

import aChaushev.architects.model.dto.UserDetailsDTO;
import aChaushev.architects.model.entity.User;
import aChaushev.architects.model.enums.UserRoleEnum;
import aChaushev.architects.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.findAllUsers();
        // Convert to DTO with role names
        List<UserDetailsDTO> userDTOs = users.stream()
                .map(user -> new UserDetailsDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRoles() // Ensure this is a List<UserRole>
                ))
                .collect(Collectors.toList());
        model.addAttribute("users", userDTOs);
        return "admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRoles());

        model.addAttribute("user", userDetailsDTO);
        model.addAttribute("allRoles", Arrays.asList(UserRoleEnum.values())); // Provide all available roles
        return "admin/edit-user";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") UserDetailsDTO userDetails, @AuthenticationPrincipal UserDetails currentUser) {
        // Update user in the service layer
        userService.updateUser(id, userDetails);

        return "redirect:/admin/users";
    }


    @DeleteMapping("/users/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }


}

