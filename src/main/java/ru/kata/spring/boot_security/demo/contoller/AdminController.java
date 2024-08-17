package ru.kata.spring.boot_security.demo.contoller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.validation.Valid;
import java.util.List;

@Controller()
@RequestMapping
public class AdminController {

    private final UserService userService;
    private final RegistrationService registrationService;
    private final RoleService roleService;


    public AdminController(UserService userService, RegistrationServiceImpl registrationService, RoleService roleService) {
        this.userService = userService;
        this.registrationService = registrationService;
        this.roleService = roleService;
    }

//    @GetMapping("")
//    public String homePage() {
//        return "AdminPage";
//    }


    @GetMapping(value = "/admin")
    public String showUser(ModelMap model) {
        model.addAttribute("printUser", userService.getAllUsers());
        return "admin/adminPage";
    }


    @GetMapping("/new")
    public String newUser(@AuthenticationPrincipal User user, ModelMap model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin/new";
    }

    @PostMapping("/")
    public String createUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        registrationService.register(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String editUserForm(@RequestParam("id") Long id, Model model) {
        User user = userService.getUser(id);
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "user-form";
    }


    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/user-form";
        }
        userService.updateUser(user);
        return "redirect:/admin/";
    }
}
