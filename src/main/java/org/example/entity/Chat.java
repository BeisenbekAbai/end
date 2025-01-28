package org.example.entity;

public class Chat {
    private String chatId;
    private String chatName;
    private boolean isGroup;

    // Constructor
    public Chat(String chatId, String chatName, boolean isGroup) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.isGroup = isGroup;
    }

    // Getters:
    public String getChatId() {
        return chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public boolean isGroup() {
        return isGroup;
    }

    // Setters:
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }
}
