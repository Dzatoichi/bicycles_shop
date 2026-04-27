package com.example.controller;

import com.example.jms.AdminMessage;
import com.example.services.AdminMessageService;
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
public class AdminMessageController {

    @Autowired
    private AdminMessageService adminMessageService;

    @GetMapping("/messages")
    public ModelAndView viewMessages(
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) String username) {

        ModelAndView modelAndView = new ModelAndView("/admin-messages");
        List<AdminMessage> messages;

        if (operation != null && !operation.isEmpty()) {
            messages = adminMessageService.getAdminMessagesByOperation(operation);
        } else if (username != null && !username.isEmpty()) {
            messages = adminMessageService.getAdminMessagesByUser(username);
        } else {
            messages = adminMessageService.getAllAdminMessages();
        }
        modelAndView.addObject("messages", messages);
        modelAndView.addObject("filterOperation", operation);
        modelAndView.addObject("filterUsername", username);

        return modelAndView;
    }
}