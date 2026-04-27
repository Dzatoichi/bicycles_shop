package com.example.controller;

import com.example.entity.Bicycle;
import com.example.repo.BicycleDao;
import com.example.services.JmsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class MainController {

    private final BicycleDao bicycleDao;
    private final JmsService jmsService;

    @Autowired
    public MainController(BicycleDao bicycleDao, JmsService jmsService) {
        this.bicycleDao = bicycleDao;
        this.jmsService = jmsService;
    }

    // Вспомогательные методы остаются
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return "Гость";
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }

    private boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
    }

    // MVC методы для HTML страниц
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Главная страница");
        model.addAttribute("username", getCurrentUsername());
        model.addAttribute("isAdmin", isAdmin());
        model.addAttribute("isAuthenticated", isAuthenticated());
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("message", "О нас");
        model.addAttribute("username", getCurrentUsername());
        model.addAttribute("isAuthenticated", isAuthenticated());
        return "about";
    }

    @GetMapping("/services")
    public String services(Model model) {
        model.addAttribute("message", "Наши услуги");
        model.addAttribute("username", getCurrentUsername());
        model.addAttribute("isAuthenticated", isAuthenticated());
        return "services";
    }

    @GetMapping("/bicycles")
    public String bicycles(Model model) {
        try {
            model.addAttribute("bicycles", bicycleDao.findAll());
            model.addAttribute("username", getCurrentUsername());
            model.addAttribute("isAdmin", isAdmin());
            model.addAttribute("isAuthenticated", isAuthenticated());
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка загрузки данных: " + e.getMessage());
        }
        return "bicycles";
    }

    // HTML форма добавления
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/bicycles/add")
    public String addBicycle(
            @RequestParam String model,
            @RequestParam String producer,
            @RequestParam String producingCountry,
            @RequestParam int gearsNum,
            @RequestParam int cost) {

        try {
            Bicycle bicycle = new Bicycle();
            bicycle.setModel(model);
            bicycle.setProducer(producer);
            bicycle.setProducingCountry(producingCountry);
            bicycle.setGearsNum(gearsNum);
            bicycle.setCost(cost);
            bicycleDao.insert(bicycle);

            // Отправка уведомления в JMS
            jmsService.sendAdminNotification("CREATE", bicycle);

        } catch (Exception e) {
            System.err.println("Error adding bicycle: " + e.getMessage());
        }
        return "redirect:/bicycles";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/bicycles/buy/{id}")
    public String buyBicycle(@PathVariable Long id) {
        try {
            Bicycle bicycle = bicycleDao.findById(id);
            if (bicycle != null) {
                // Помечаем как купленный
                bicycleDao.markAsPurchased(id);

                // Отправляем сообщение о покупке
                jmsService.sendPurchaseNotification(bicycle);
            }
        } catch (Exception e) {
            System.err.println("Error buying bicycle: " + e.getMessage());
        }
        return "redirect:/bicycles";
    }

    // HTML форма поиска
    @GetMapping("/bicycles/find")
    public String findBicyclesByCost(@RequestParam double cost, Model model) {
        try {
            model.addAttribute("bicycles", bicycleDao.find_cost((int) cost));
            model.addAttribute("username", getCurrentUsername());
            model.addAttribute("isAdmin", isAdmin());
            model.addAttribute("isAuthenticated", isAuthenticated());
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка поиска: " + e.getMessage());
        }
        return "bicycles";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminPanel(Model model) {
        try {
            model.addAttribute("bicycles", bicycleDao.findAll());
            model.addAttribute("username", getCurrentUsername());
            model.addAttribute("isAuthenticated", isAuthenticated());
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка загрузки данных: " + e.getMessage());
        }
        return "admin";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}