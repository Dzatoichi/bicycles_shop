package com.example.controller.api;

import com.example.jms.AdminMessage;
import com.example.services.AdminMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/messages")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMessageApiController {

    @Autowired
    private AdminMessageService adminMessageService;

    @GetMapping
    public ResponseEntity<List<AdminMessage>> getAllAdminMessages() {
        return ResponseEntity.ok(adminMessageService.getAllAdminMessages());
    }

    @GetMapping("/operation/{operation}")
    public ResponseEntity<List<AdminMessage>> getAdminMessagesByOperation(
            @PathVariable String operation) {
        return ResponseEntity.ok(adminMessageService.getAdminMessagesByOperation(operation));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<AdminMessage>> getAdminMessagesByUser(
            @PathVariable String username) {
        return ResponseEntity.ok(adminMessageService.getAdminMessagesByUser(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminMessage> getAdminMessageById(@PathVariable Long id) {
        AdminMessage message = adminMessageService.getMessageById(id);
        if (message != null) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminMessage(@PathVariable Long id) {
        adminMessageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<AdminMessage>> searchAdminMessages(
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) String username) {

        List<AdminMessage> messages;
        if (operation != null && username != null) {
            // Можно добавить комбинированный поиск если нужно
            messages = adminMessageService.getAllAdminMessages();
        } else if (operation != null) {
            messages = adminMessageService.getAdminMessagesByOperation(operation);
        } else if (username != null) {
            messages = adminMessageService.getAdminMessagesByUser(username);
        } else {
            messages = adminMessageService.getAllAdminMessages();
        }

        return ResponseEntity.ok(messages);
    }
}