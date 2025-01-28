package org.example.service;

import org.example.DatabaseManager;
import org.example.entity.Chat;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ChatService {
    private final DatabaseManager dbManager;

    private static final String INSERT_CHAT = "insert into chats (chatid, chatname, isgroup) values (?,?,?::boolean)";
    private static final String INSERT_PARTICIPANT = "insert into chatparticipants(chatid, userid) values(?,?)";

    // Constructor
    public ChatService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    // Create chat
    public void createChat(String[] usersid, String chatName, boolean isGroup) throws SQLException {
        String chatid = UUID.randomUUID().toString();
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(INSERT_CHAT)) {
            stmt.setString(1, chatid);
            stmt.setString(2, isGroup ? chatName : " ");
            stmt.setString(3, String.valueOf(false));
            stmt.executeUpdate();
        }
        for (String userid : usersid) {
            addParticipant(chatid, userid);
        }
    }

    // Add participants
    public void addParticipant(String chatid, String userid) throws SQLException {
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(INSERT_PARTICIPANT)) {
            stmt.setString(1, chatid);
            stmt.setString(2, userid);
            stmt.executeUpdate();
        }
    }
}
