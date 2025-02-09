package org.example.controller;

import org.example.DatabaseManager;
import org.example.entity.User;
import org.example.service.UserService;

import java.sql.SQLException;
import java.util.Scanner;

public class UserController {
    private final DatabaseManager dbManager;
    Scanner scanner = new Scanner(System.in);

    // Constructor
    public UserController(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    // Sign up logic
    public User signUp() {
        // Form
        System.out.println("Fill in the following fields");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Confirm Password: ");
        String confirmPassword = scanner.nextLine();

        // Check passwords for matchability
        if (password.equals(confirmPassword)) {
            User user = null;
            UserService userService = new UserService(dbManager);
            try {
                userService.addUser(username, email, password);
                System.out.println("You have successfully registered!");
                user = userService.getUser(email, password);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return user;
        } else {
            System.out.println("Passwords do not match!");
            return null;
        }
    }

    // Login logic
    public User login() {
        System.out.println("Fill in the following fields");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Get user data
        User user = null;
        try {
            UserService userService = new UserService(dbManager);
            user = userService.getUser(email, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (user != null) {
            System.out.println(" ");
            System.out.println("You have successfully logged in!");
        } else {
            System.out.println(" ");
            System.out.println("Wrong email or password!");
        }
        return user;
    }
}
