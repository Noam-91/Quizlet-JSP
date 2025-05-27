package com.bfs.quizlet.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private Long contactId;
    private String subject;
    private String message;
    private String email;
    private Boolean isActive;
    private Boolean isSolved;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Long updatedBy;

    // Additional field
    private String messageSummary;

    public Contact(String subject, String message, String email){
        this.subject = subject;
        this.message = message;
        this.email = email;
        this.messageSummary = message.length() > 50 ? message.substring(0, 50) + "..." : message;
    }
    public Contact(Long contactId, String subject, String message, String email, Boolean isActive, Boolean isSolved, Timestamp createdAt, Timestamp updatedAt, Long updatedBy){
        this.contactId = contactId;
        this.subject = subject;
        this.message = message;
        this.email = email;
        this.isActive = isActive;
        this.isSolved = isSolved;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.messageSummary = message.length() > 50 ? message.substring(0, 50) + "..." : message;
    }
}
