package com.example.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_operations_log")
public class AdminOperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operationType; // CREATE, UPDATE, DELETE
    private String entityType;    // Bicycle, User, etc.
    private Long entityId;
    private String entityDetails;
    private String username;
    private LocalDateTime operationTimestamp;
    private String message;

    // Конструкторы
    public AdminOperationLog() {}

    public AdminOperationLog(String operationType, String entityType, Long entityId,
                             String entityDetails, String username, String message) {
        this.operationType = operationType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.entityDetails = entityDetails;
        this.username = username;
        this.message = message;
        this.operationTimestamp = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }

    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }

    public String getEntityDetails() { return entityDetails; }
    public void setEntityDetails(String entityDetails) { this.entityDetails = entityDetails; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDateTime getOperationTimestamp() { return operationTimestamp; }
    public void setOperationTimestamp(LocalDateTime operationTimestamp) { this.operationTimestamp = operationTimestamp; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    @Override
    public String toString() {
        return "AdminOperationLog{" +
                "id=" + id +
                ", operationType='" + operationType + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                ", username='" + username + '\'' +
                ", timestamp=" + operationTimestamp +
                ", message='" + message + '\'' +
                '}';
    }
}