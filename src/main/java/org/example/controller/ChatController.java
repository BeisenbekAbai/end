package org.example.controller;

import org.example.DatabaseManager;
import org.example.entity.Message;
import org.example.service.ChatService;
import org.example.service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatController {
    private final String chatId;
    private final String userId;
    private boolean isOpen = true;
    private final DatabaseManager dbManager;
    private final Scanner scanner = new Scanner(System.in);

    // Constructor
    public ChatController(DatabaseManager dbManager, String chatId, String userId) {
        this.dbManager = dbManager;
        this.chatId = chatId;
        this.userId = userId;
    }

    // Display all messages of this chat
    public void displayMessages() throws SQLException {
        ChatService chatService = new ChatService(dbManager);
        UserService userService = new UserService(dbManager);
        ArrayList<Message> messages = chatService.getMessages(chatId);
        System.out.println("===============================");
        System.out.println();
        for (Message message : messages) {
            System.out.println(userService.getUserById(message.getSenderId()).getUserName() + " -");
            System.out.println(message.getContent());
            System.out.println();
        }
    }

    // Entry point
    public void start() throws SQLException {
        ChatService chatService = new ChatService(dbManager);
        while (isOpen) {
            displayMessages();
            System.out.println("Hint: Type the \"$\" symbol to exit.");
            String text = scanner.nextLine();
            System.out.println();
            if (text.equals("$")) {
                System.out.println("===============================");
                System.out.println();
                isOpen = false;
            } else {
                chatService.addMessage(chatId, userId, text);
            }
        }
    }
}
