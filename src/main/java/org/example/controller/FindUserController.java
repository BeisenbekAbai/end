package org.example.controller;

import org.example.App;
import org.example.DatabaseManager;
import org.example.service.ChatService;
import org.example.service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FindUserController {
    private String mainUserId;
    private boolean quit = false;
    private final DatabaseManager dbManager;
    private final Scanner scanner = new Scanner(System.in);
    private final List<String> orderedUsersList = new ArrayList<>();

    // Constructor
    public FindUserController(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    // Setter
    public void setMainUserId(String mainUserId) {
        this.mainUserId = mainUserId;
        orderedUsersList.clear();
    }
    public void setQuit(boolean quit) {
        this.quit = quit;
    }

    // Display list of users
    public void displayUsers() throws SQLException {
        UserService userService = new UserService(dbManager);
        List<String> usersId = userService.getAllUsersId();
        int counter = 0;
        for (String userId : usersId) {
            if (!userId.equals(mainUserId)) {
                System.out.println((2 + counter) + ". " + userService.getUserById(userId).getUserName());
                orderedUsersList.add(userId);
                counter++;
            }
        }
    }

    // Open the chat with interlocutor, if there is
    public boolean openChat(String interlocutorId) throws SQLException {
        ChatService chatService = new ChatService(dbManager);
        // Get IDs of all chats with the main user
        List<String> usersChats = chatService.getChatsByUser(mainUserId);
        for (String chatId : usersChats) {
            // Get ID list of participants for each chat
            List<String> participants = chatService.getParticipants(chatId);
            for (String participant : participants) {
                // Check:
                // 1) Is there interlocutor in the participants' list
                // 2) Isn't this chat а group
                if (participant.equals(interlocutorId) && !chatService.getChat(chatId).isGroup()) {
                    // Open the chat
                    ChatController chatController = new ChatController(dbManager, chatId, mainUserId);
                    chatController.start();
                    return true;
                }
            }
        }
        return false;
    }

    // Create chat with interlocutor
    public void createChat(String interlocutorId) throws SQLException {
        // Create new chat
        ChatService chatService = new ChatService(dbManager);
        chatService.createChat(Arrays.asList(interlocutorId, mainUserId), " ", false);
        // Get the id of this chat, then pass it into chatController
        List<String> mainUsersChats = chatService.getChatsByUser(mainUserId);
        String lastChatId = mainUsersChats.get(mainUsersChats.size()-1);
        ChatController chatController = new ChatController(dbManager, lastChatId, mainUserId);
        // Open the chat
        chatController.start();
    }

    // Entry point
    public void run() throws SQLException {
        while (!quit) {
            App.addSpace();
            System.out.println("Choose an option:");
            System.out.println("1. Back");
            System.out.println();
            System.out.println("Users:");
            displayUsers();
            System.out.println();
            int choice = scanner.nextInt();
            if (choice == 1) {
                quit = true;
            } else if (!openChat(orderedUsersList.get(choice - 2))) {
                createChat(orderedUsersList.get(choice - 2));
            }
        }
    }
}
