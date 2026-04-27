package com.example.services;

import com.example.jms.AdminMessage;
import com.example.jms.PurchaseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.core.config.JMS.JmsConfig;

@Service
public class JmsService {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendAdminNotification(String operation, Object entity) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AdminMessage message = new AdminMessage(operation, (com.example.entity.Bicycle) entity, username);
        jmsTemplate.convertAndSend(JmsConfig.ADMIN_QUEUE, message);
    }

    public void sendPurchaseNotification(Object entity) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PurchaseMessage message = new PurchaseMessage((com.example.entity.Bicycle) entity, username);
        jmsTemplate.convertAndSend(JmsConfig.PURCHASE_QUEUE, message);
    }
}