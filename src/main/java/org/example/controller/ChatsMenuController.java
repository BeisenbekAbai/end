package org.example.controller;

import org.example.DatabaseManager;
import org.example.entity.Chat;
import org.example.entity.User;
import org.example.service.ChatService;
import org.example.service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatsMenuController {
    private User mainUser;
    private final DatabaseManager dbManager;
    private List<String> orderedChatsList = new ArrayList<>();

    // Constructor
    public ChatsMenuController(DatabaseManager dbManager) {
        this.dbManager = dbManager;

    }

    // Setter
    public void setUser(User user) {
        orderedChatsList = new ArrayList<>();
        this.mainUser = user;
    }

    // Display chats
    public void displayChats() throws SQLException {
        ChatService chatService = new ChatService(dbManager);
        List<Chat> chats = chatService.getPermittedChats(mainUser.getUserId());
        orderedChatsList = new ArrayList<>();
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
                        System.out.println(4+i + ". " + user.getUserName());
                    }
                }
            }
            // Display group name
            else {
                System.out.println(4+i + ". " + chat.getChatName());
            }
            orderedChatsList.add(chat.getChatId());
        }
    }

    // Open chat
    public void openChat(int chatsOrder) throws SQLException {
        ChatController chatController = new ChatController(dbManager, orderedChatsList.get(chatsOrder - 4), mainUser.getUserId());
        chatController.start();
    }
}
