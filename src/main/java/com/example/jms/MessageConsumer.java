package com.example.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.example.core.config.JMS.JmsConfig;
import com.example.services.AdminMessageService;
import com.example.services.PurchaseMessageService;

@Component
public class MessageConsumer {

    private final AdminMessageService adminMessageService;
    private final PurchaseMessageService purchaseMessageService;

    public MessageConsumer(AdminMessageService adminMessageService,
                           PurchaseMessageService purchaseMessageService) {
        this.adminMessageService = adminMessageService;
        this.purchaseMessageService = purchaseMessageService;
    }

    @JmsListener(destination = JmsConfig.ADMIN_QUEUE)
    public void processAdminMessage(AdminMessage message) {
        // Сохраняем сообщение в БД
        adminMessageService.saveAdminMessage(message);

        System.out.println("=== ADMIN NOTIFICATION ===");
        System.out.println("Operation: " + message.getOperation());
        System.out.println("Bicycle: " + message.getBicycle());
        System.out.println("User: " + message.getUsername());
        System.out.println("Timestamp: " + message.getTimestamp());
        System.out.println("==========================");
    }

    @JmsListener(destination = JmsConfig.PURCHASE_QUEUE)
    public void receivePurchaseMessage(PurchaseMessage message) {
        // Сохраняем сообщение о покупке в БД
        purchaseMessageService.savePurchaseMessage(message);

        System.out.println("=== PURCHASE NOTIFICATION ===");
        System.out.println("Bicycle purchased: " + message.getBicycle());
        System.out.println("Buyer: " + message.getUsername());
        System.out.println("Timestamp: " + message.getTimestamp());
        System.out.println("=============================");
    }
}