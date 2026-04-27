package com.example.jms;

import com.example.entity.Bicycle;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

public class AdminMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String operation;
    private Bicycle bicycle;
    private LocalDateTime timestamp;
    private String username;

    public AdminMessage() {}

    public AdminMessage(String operation, Bicycle bicycle, String username) {
        this.operation = operation;
        this.bicycle = bicycle;
        this.username = username;
        this.timestamp = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public Bicycle getBicycle() { return bicycle; }
    public void setBicycle(Bicycle bicycle) { this.bicycle = bicycle; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public String toString() {
        return "AdminMessage{" +
                "id=" + id +
                ", operation='" + operation + '\'' +
                ", bicycle=" + bicycle +
                ", timestamp=" + timestamp +
                ", username='" + username + '\'' +
                '}';
    }
}