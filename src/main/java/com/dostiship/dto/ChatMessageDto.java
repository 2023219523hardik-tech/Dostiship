package com.dostiship.dto;

import com.dostiship.model.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ChatMessageDto {

    private Long id;

    @NotNull(message = "Recipient ID is required")
    private Long recipientId;

    @NotBlank(message = "Content is required")
    private String content;

    private Long senderId;
    private String senderName;
    private LocalDateTime timestamp;
    private ChatMessage.MessageStatus status;

    // Constructors
    public ChatMessageDto() {
    }

    public ChatMessageDto(Long recipientId, String content) {
        this.recipientId = recipientId;
        this.content = content;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ChatMessage.MessageStatus getStatus() {
        return status;
    }

    public void setStatus(ChatMessage.MessageStatus status) {
        this.status = status;
    }
}