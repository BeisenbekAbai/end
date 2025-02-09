package org.example.controller;

import org.example.App;
import org.example.DatabaseManager;
import org.example.service.ChatService;
import org.example.service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CreateGroupController {
    private final DatabaseManager dbManager;
    private boolean quit = false;
    private String mainUserId;
    private final List<Integer> choices = new ArrayList<>();
    private final List<String> orderedUsersList = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    // Constructor
    public CreateGroupController(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        this.mainUserId = mainUserId;
    }

    // Setter
    public void setQuit(boolean quit) {
        this.quit = quit;
    }
    public void setMainUserId(String mainUserId) {
        this.mainUserId = mainUserId;
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

    // Entry point
    public void run() throws SQLException {
        while (!quit) {
            choices.clear();
            App.addSpace();
            System.out.println("Select the users");
            System.out.println("1. Back");
            System.out.println();
            System.out.println("Users:");
            displayUsers();
            System.out.println();
            // Split a string into numbers
            String[] lineOfIndexes = scanner.nextLine().split(" ");
            for (String index : lineOfIndexes) {
                choices.add(Integer.parseInt(index));
            }
            if (choices.get(0).equals(1)) {
                quit = true;
            } else {
                System.out.print("Group name: ");
                String groupName = scanner.nextLine();
                // Converting the selected indexes to user IDs
                List<String> participantsId = new ArrayList<>();
                participantsId.add(mainUserId);
                for (int participantInd : choices) {
                    participantsId.add(orderedUsersList.get(participantInd - 2));
                }
                // Create group
                ChatService chatService = new ChatService(dbManager);
                chatService.createChat(participantsId, groupName, true);
                // Get the id of this chat, then pass it into chatController
                List<String> mainUsersChats = chatService.getChatsByUser(mainUserId);
                String lastChatId = mainUsersChats.get(mainUsersChats.size()-1);
                ChatController chatController = new ChatController(dbManager, lastChatId, mainUserId);
                // Open the chat
                chatController.start();
            }
        }
    }
}
