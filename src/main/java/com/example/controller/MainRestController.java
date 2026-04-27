package com.example.controller;

import com.example.entity.Bicycle;
import com.example.repo.BicycleDao;
import com.example.services.JmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bicycles")
public class MainRestController {

    private final BicycleDao bicycleDao;
    private final JmsService jmsService;

    @Autowired
    public MainRestController(BicycleDao bicycleDao, JmsService jmsService) {
        this.bicycleDao = bicycleDao;
        this.jmsService = jmsService;
    }

    // GET все велосипеды - доступно без аутентификации
    @GetMapping
    public List<Bicycle> getAllBicycles() {
        return bicycleDao.findAll();
    }

    // GET один велосипед - доступно без аутентификации
    @GetMapping("/{id}")
    public ResponseEntity<Bicycle> getBicycleById(@PathVariable Long id) {
        try {
            Bicycle bicycle = bicycleDao.findById(id);
            return ResponseEntity.ok(bicycle);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET поиск по цене - доступно без аутентификации
    @GetMapping("/search")
    public List<Bicycle> findBicyclesByCost(@RequestParam Integer cost) {
        return bicycleDao.find_cost(cost);
    }

    // POST добавить велосипед - требует аутентификации ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Bicycle> createBicycle(@RequestBody Bicycle bicycle) {
        try {
            bicycleDao.insert(bicycle);
            jmsService.sendAdminNotification("CREATE", bicycle);
            return ResponseEntity.ok(bicycle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT обновить велосипед - требует аутентификации ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Bicycle> updateBicycle(@PathVariable Long id, @RequestBody Bicycle bicycle) {
        try {
            bicycle.setId(id);
            bicycleDao.update(id, bicycle);
            jmsService.sendAdminNotification("UPDATE", bicycle);
            return ResponseEntity.ok(bicycle);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE удалить велосипед - требует аутентификации ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBicycle(@PathVariable Long id) {
        try {
            Bicycle bicycle = bicycleDao.findById(id);
            bicycleDao.delete(id);
            jmsService.sendAdminNotification("DELETE", bicycle);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/buy/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> buyBicycle(@PathVariable Long id) {
        try {
            Bicycle bicycle = bicycleDao.findById(id);
            if (bicycle != null) {
                bicycleDao.markAsPurchased(id);
                jmsService.sendPurchaseNotification(bicycle);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}