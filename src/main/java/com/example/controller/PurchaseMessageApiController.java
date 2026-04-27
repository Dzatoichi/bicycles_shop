package com.example.controller;

import com.example.jms.PurchaseMessage;
import com.example.services.PurchaseMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/purchase/messages")
@PreAuthorize("hasRole('ADMIN')")
public class PurchaseMessageApiController {

    @Autowired
    private PurchaseMessageService purchaseMessageService;

    @GetMapping
    public ResponseEntity<List<PurchaseMessage>> getAllPurchaseMessages() {
        return ResponseEntity.ok(purchaseMessageService.getAllPurchaseMessages());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<PurchaseMessage>> getPurchaseMessagesByUser(
            @PathVariable String username) {
        return ResponseEntity.ok(purchaseMessageService.getPurchaseMessagesByUser(username));
    }

    @GetMapping("/bicycle/{bicycleId}")
    public ResponseEntity<List<PurchaseMessage>> getPurchaseMessagesByBicycle(
            @PathVariable Long bicycleId) {
        return ResponseEntity.ok(purchaseMessageService.getPurchaseMessagesByBicycle(bicycleId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseMessage> getPurchaseMessageById(@PathVariable Long id) {
        PurchaseMessage message = purchaseMessageService.getMessageById(id);
        if (message != null) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseMessage(@PathVariable Long id) {
        purchaseMessageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats/count")
    public ResponseEntity<Long> getPurchaseMessagesCount() {
        // Можно добавить метод в сервис для подсчета
        return ResponseEntity.ok((long) purchaseMessageService.getAllPurchaseMessages().size());
    }

    @GetMapping("/stats/top-users")
    public ResponseEntity<List<Object[]>> getTopUsers() {
        // Пример: возвращаем топ пользователей по количеству покупок
        // Нужно будет реализовать в сервисе
        return ResponseEntity.ok().build();
    }
}