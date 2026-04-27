package com.example.jms;

import com.example.entity.Bicycle;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

public class PurchaseMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Bicycle bicycle;
    private String username;
    private LocalDateTime timestamp;

    public PurchaseMessage() {}

    public PurchaseMessage(Bicycle bicycle, String username) {
        this.bicycle = bicycle;
        this.username = username;
        this.timestamp = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public Long getId(){ return id; }
    public void setId( Long id ){ this.id = id; }

    public Bicycle getBicycle() { return bicycle; }
    public void setBicycle(Bicycle bicycle) { this.bicycle = bicycle; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "PurchaseMessage{" +
                "id=" + id +
                ", bicycle=" + bicycle +
                ", username='" + username + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}