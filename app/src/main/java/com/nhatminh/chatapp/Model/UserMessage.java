package com.nhatminh.chatapp.Model;

public class UserMessage {
    String messageContent;
    String senderId;
    String createdAt;


    public UserMessage(String messageContent, String senderId, String createdAt) {
        this.messageContent = messageContent;
        this.senderId = senderId;
        this.createdAt = createdAt;
    }


    public UserMessage() {
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
