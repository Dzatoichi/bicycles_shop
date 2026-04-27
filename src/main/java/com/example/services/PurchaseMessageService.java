package com.example.services;

import com.example.jms.PurchaseMessage;
import com.example.repo.PurchaseMessageRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PurchaseMessageService {

    private final PurchaseMessageRepository purchaseMessageRepository;

    public PurchaseMessageService(PurchaseMessageRepository purchaseMessageRepository) {
        this.purchaseMessageRepository = purchaseMessageRepository;
    }

    public void savePurchaseMessage(PurchaseMessage message) {
        purchaseMessageRepository.save(message);
    }

    public List<PurchaseMessage> getAllPurchaseMessages() {
        return purchaseMessageRepository.findAllOrderByTimestampDesc();
    }

    public List<PurchaseMessage> getPurchaseMessagesByUser(String username) {
        return purchaseMessageRepository.findByUsernameOrderByTimestampDesc(username);
    }

    public List<PurchaseMessage> getPurchaseMessagesByBicycle(Long bicycleId) {
        return purchaseMessageRepository.findByBicycleId(bicycleId);
    }

    public PurchaseMessage getMessageById(Long id) {
        return purchaseMessageRepository.findById(id);
    }

    public void deleteMessage(Long id) {
        purchaseMessageRepository.deleteById(id);
    }
}