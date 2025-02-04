package org.example.service;

import org.example.DatabaseManager;
import org.example.entity.Chat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatService {
    private final DatabaseManager dbManager;

    private static final String INSERT_CHAT = "insert into chats (chatid, chatname, isgroup) values (?,?,?::boolean)";
    private static final String INSERT_PARTICIPANT = "insert into chatparticipants(chatid, userid) values(?,?)";
    private static final String SELECT_CHAT_FROM_CHATS = "select * from chats where chatid=?";
    private static final String SELECT_CHATS_FROM_CHATPARTICIPANTS = "select * from chatparticipants where userid=?";
    private static final String SELECT_PARTICIPANTS = "select * from chatparticipants where chatid=?";

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

    // Get chat from Chats Table
    public Chat getChat(String chatid) throws SQLException {
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(SELECT_CHAT_FROM_CHATS)) {
            stmt.setString(1, chatid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Chat(rs.getString("chatid"), rs.getString("chatname"), rs.getBoolean("isgroup"));
            }
        }
        return null;
    }

    // Get permitted chats for user
    public List<Chat> getChats(String userId) throws SQLException {
        List<Chat> chats = new ArrayList<>();
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(SELECT_CHATS_FROM_CHATPARTICIPANTS)){
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                chats.add(getChat(rs.getString("chatid")));
            }
        }
        return chats;
    }

    // Get participants
    public List<String> getParticipants(String chatid) throws SQLException {
        List<String> participants = null;
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(SELECT_PARTICIPANTS)) {
            stmt.setString(1, chatid);
            ResultSet rs = stmt.executeQuery();
            participants = new ArrayList<>();
            while (rs.next()) {
                participants.add(rs.getString("userid"));
            }
        }
        return participants;
    }
}
