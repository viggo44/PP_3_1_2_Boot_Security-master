package ru.kata.spring.boot_security.demo.Contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.Service.RegistrationService;
import ru.kata.spring.boot_security.demo.Service.RegistrationServiceImpl;
import ru.kata.spring.boot_security.demo.Service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.Service.UserServiceImpl;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.validation.Valid;
import java.util.List;

@Controller()
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userServiceImpl;
    private final RegistrationService registrationServiceImpl;
    private final RoleServiceImpl roleServiceImpl;


    public AdminController(UserServiceImpl userServiceImpl, RegistrationServiceImpl registrationServiceImpl, RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.registrationServiceImpl = registrationServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
    }

    @GetMapping("")
    public String homePage() {
        return "AdminPage";
    }


    @GetMapping(value = "/")
    public String showUser(ModelMap model) {
        List<User> cars = userServiceImpl.getAllUsers();
        model.addAttribute("printUser", cars);
        return "userr";
    }

    @GetMapping("/new")
    public String newUser(ModelMap model) {
        model.addAttribute("user", new User());
        return "neww";
    }

    @PostMapping("/")
    public String createUser(@ModelAttribute("user") User user) {
        registrationServiceImpl.register(user);
        return "redirect:/admin/";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userServiceImpl.deleteUser(id);
        return "redirect:/admin/";
    }

    @GetMapping("/edit")
    public String editUserForm(@RequestParam("id") Long id, Model model) {
        User user = userServiceImpl.getUser(id);
        List<Role> roles = roleServiceImpl.getAllRoles();
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
        userServiceImpl.updateUser(user);
        return "redirect:/admin/";
    }
}
