package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(Model model, Principal principal) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("newUser", new User()); // Для формы создания нового пользователя
        model.addAttribute("roles", roleService.findAll()); // Для выбора ролей
        model.addAttribute("user", userService.findByEmail(principal.getName())); // Для выбора пользователя
        return "admin"; // Возвращаем имя шаблона
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("newUser") User newUser) {
        userService.saveUser(newUser);
        return "redirect:/admin"; // Перенаправление на страницу администрирования
    }

    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, @ModelAttribute User user) {
        user.setId(id); // Устанавливаем ID редактируемого пользователя
        userService.saveUser(user);
        return "redirect:/admin"; // Перенаправление на страницу администрирования
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin"; // Перенаправление на страницу администрирования
    }
}
