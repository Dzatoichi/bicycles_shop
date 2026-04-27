package com.example.services;

import com.example.jms.AdminMessage;
import com.example.repo.AdminMessageRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminMessageService {

    private final AdminMessageRepository adminMessageRepository;

    public AdminMessageService(AdminMessageRepository adminMessageRepository) {
        this.adminMessageRepository = adminMessageRepository;
    }

    public void saveAdminMessage(AdminMessage message) {
        adminMessageRepository.save(message);
    }

    public List<AdminMessage> getAllAdminMessages() {
        return adminMessageRepository.findAllOrderByTimestampDesc();
    }

    public List<AdminMessage> getAdminMessagesByOperation(String operation) {
        return adminMessageRepository.findByOperationOrderByTimestampDesc(operation);
    }

    public List<AdminMessage> getAdminMessagesByUser(String username) {
        return adminMessageRepository.findByUsernameOrderByTimestampDesc(username);
    }

    public AdminMessage getMessageById(Long id) {
        return adminMessageRepository.findById(id);
    }

    public void deleteMessage(Long id) {
        adminMessageRepository.deleteById(id);
    }
}