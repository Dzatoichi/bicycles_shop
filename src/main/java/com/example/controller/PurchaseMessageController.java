package com.example.controller;

import com.example.jms.PurchaseMessage;
import com.example.services.PurchaseMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class PurchaseMessageController {

    @Autowired
    private PurchaseMessageService purchaseMessageService;

    @GetMapping("/purchase-messages")
    public ModelAndView viewPurchaseMessages(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Long bicycleId) {

        ModelAndView modelAndView = new ModelAndView("/purchase-messages");
        List<PurchaseMessage> messages;

        if (username != null && !username.isEmpty()) {
            messages = purchaseMessageService.getPurchaseMessagesByUser(username);
        } else if (bicycleId != null) {
            messages = purchaseMessageService.getPurchaseMessagesByBicycle(bicycleId);
        } else {
            messages = purchaseMessageService.getAllPurchaseMessages();
        }

        modelAndView.addObject("messages", messages);
        modelAndView.addObject("filterUsername", username);
        modelAndView.addObject("filterBicycleId", bicycleId);

        return modelAndView;
    }
}