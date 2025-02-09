package org.example.entity;

public class Message {
    private String messageId;
    private String chatId;
    private String senderId;
    private String content;
    private String sentTime;

    public Message(String messageId, String chatId, String senderId, String content, String sentTime) {
        this.messageId = messageId;
        this.chatId = chatId;
        this.senderId = senderId;
        this.content = content;
        this.sentTime = sentTime;
    }

    // Getters
    public String getMessageId() {
        return messageId;
    }
    public String getChatId() {
        return chatId;
    }
    public String getSenderId() {
        return senderId;
    }
    public String getContent() {
        return content;
    }
    public String getSentTime() {
        return sentTime;
    }

    // Setters
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    //Chat
}
