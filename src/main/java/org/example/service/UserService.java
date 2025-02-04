package org.example.service;

import org.example.DatabaseManager;
import org.example.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class UserService {
    private final DatabaseManager dbManager;

    private static final String  INSERT_USER = "insert into users(userid, username, email, password) values(?,?,?,?)";
    private static final String SELECT_USER = "select * from users where email = ? and password = ?";
    private static final String SELECT_USER_BY_ID = "select * from users where userid = ?";

    // Constructor
    public UserService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    // Add user
    public void addUser(String username, String email, String password) throws SQLException {
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(INSERT_USER)) {
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, username);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.executeUpdate();
        }
    }

    // Get user
    public User getUser(String email, String password) throws SQLException {
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(SELECT_USER)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userid"), rs.getString("username"), rs.getString("email"), rs.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return null;
    }

    // Get user by userid
    public User getUserById(String userid) throws SQLException {
        User user = null;
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(SELECT_USER_BY_ID)) {
            stmt.setString(1, userid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("userid"), rs.getString("username"), rs.getString("email"), rs.getString("password"));
            }
        }
        return user;
    }
}
