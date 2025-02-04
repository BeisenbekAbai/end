package org.example.controller;

import org.example.DatabaseManager;
import org.example.entity.Chat;
import org.example.entity.User;
import org.example.service.ChatService;
import org.example.service.UserService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ChatsMenuController {
    private User mainUser;
    private DatabaseManager dbManager;
    Scanner scanner = new Scanner(System.in);

    // Constructor
    public ChatsMenuController(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    // Setter
    public void setUser(User user) {
        this.mainUser = user;
    }

    // Display chats
    public void displayChats() throws SQLException {
        ChatService chatService = new ChatService(dbManager);
        List<Chat> chats = chatService.getChats(mainUser.getUserId());
        int action;
        //Display list of chats
        System.out.println("Chats: ");
        for (int i = 0; i < chats.size(); i++) {
            Chat chat = chats.get(i);
            // Display the name of interlocutor: get all participants of this chat, then choose interlocutor, and get his name
            if (!chat.isGroup()) {
                List<String> participants = chatService.getParticipants(chat.getChatId());
                UserService userService = new UserService(dbManager);
                for (String participant : participants) {
                    User user = userService.getUserById(participant);
                    if (!user.getUserName().equals(mainUser.getUserName())) {
                        System.out.println(3+i + ". " + user.getUserName());
                    }
                }
            }
            // Display group name
            else {
                System.out.println(3+i + ". " + chat.getChatName());
            }
        }
        action = scanner.nextInt() - 2;
        switch (action) {
            case 1:
                break;
        }
    }
}
