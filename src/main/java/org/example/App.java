package org.example;

import org.example.controller.ChatsMenuController;
import org.example.controller.CreateGroupController;
import org.example.controller.FindUserController;
import org.example.controller.UserController;
import org.example.entity.User;

import java.sql.SQLException;
import java.util.Scanner;

public class App {
    // To add space between dialog boxes
    public static void addSpace() {
        System.out.println();
        System.out.println("===============================");
        System.out.println();
    }

    public static void main( String[] args ) {
//        try {
//            DatabaseManager dbManager = new DatabaseManager();
//            UserController userController = new UserController(dbManager);
//            dbManager.connect();
//            ChatService chatService = new ChatService(dbManager);
//            List<Chat> chats = chatService.getChats("3b7cad33-3c94-4833-913c-dbd42ccd5c63");
//            System.out.println(chats.toString());
//        } catch (SQLException e) {
//            System.out.println("Error: " + e.getMessage());
//        }

//        try {
//            DatabaseManager dbManager = new DatabaseManager();
//            UserController userController = new UserController(dbManager);
//            dbManager.connect();
//            ChatService chatService = new ChatService(dbManager);
//            String[] usersid = new String[]{
//                    "3b7cad33-3c94-4833-913c-dbd42ccd5c63",
//                    "7bbe155e-fb6e-4781-a73e-4537e23b138d",
//                    "a7dc1a7d-e436-4aa2-94cd-68d827331525"
//            };
//            chatService.createChat(usersid, "", true);
//        } catch (SQLException e) {
//            System.out.println("Error: " + e.getMessage());
//        }

        Scanner scanner = new Scanner(System.in);
        User user = null;
        try {
            DatabaseManager dbManager = new DatabaseManager();
            UserController userController = new UserController(dbManager);
            FindUserController findUserController = new FindUserController(dbManager);
            CreateGroupController createGroupController = new CreateGroupController(dbManager);
            ChatsMenuController chatsMenuController = new ChatsMenuController(dbManager);
            dbManager.connect();
            while(true) {
                // Auth menu
                System.out.println("Choose action");
                System.out.println("1. Login");
                System.out.println("2. Sign up");
                System.out.println("3. Exit");
                System.out.println(" ");
                int action = scanner.nextInt();
                addSpace();
                switch(action) {
                    case 1:
                        user = userController.login();
                        break;
                    case 2:
                        user = userController.signUp();
                        break;
                    case 3:
                        System.exit(0);
                }
                Thread.sleep(2000);
                addSpace();
                if(user != null) {
                    chatsMenuController.setUser(user);
                    findUserController.setMainUserId(user.getUserId());
                    createGroupController.setMainUserId(user.getUserId());
                }
                // Chats menu
                while (user != null) {
                    System.out.println("Choose action");
                    System.out.println("1. Logout");
                    System.out.println("2. Find user");
                    System.out.println("3. Create Group");
                    System.out.println(" ");
                    chatsMenuController.displayChats();
                    System.out.println(" ");
                    int action2 = scanner.nextInt();
                    switch(action2) {
                        case 1:
                            user = null;
                            addSpace();
                            break;
                        case 2:
                            findUserController.setQuit(false);
                            findUserController.run();
                            addSpace();
                            break;
                        case 3:
                            createGroupController.setQuit(false);
                            createGroupController.run();
                            addSpace();
                            break;
                        default:
                            System.out.println(" ");
                            chatsMenuController.openChat(action2);
                            break;
                    }
                }
            }
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
