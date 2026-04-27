package com.example.controller;

import com.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        // Проверка паролей
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "register";
        }

        // Проверка существования пользователя
        if (userService.userExists(username)) {
            model.addAttribute("error", "Пользователь с таким именем уже существует");
            return "register";
        }

        try {
            // Регистрируем пользователя с ролью USER
            userService.registerUser(username, password, "ROLE_USER");
            model.addAttribute("message", "Регистрация успешна! Теперь вы можете войти.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка регистрации: " + e.getMessage());
            return "register";
        }
    }
}