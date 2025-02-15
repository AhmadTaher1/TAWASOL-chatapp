package gov.iti.jets.server.model.dao.implementations;


import gov.iti.jets.server.model.dao.interfaces.ChatbotDAOInt;
import shared.dto.Chatbot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatbotDAOImpl implements ChatbotDAOInt {

    private Connection connection;

    public ChatbotDAOImpl(Connection connection) {
        this.connection = connection;
    }


    public void addChatbot(Chatbot chatbot) throws SQLException {
        String sql = "INSERT INTO chatbot (user_id, is_enabled, chatbot_type) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, chatbot.getUserId());
            stmt.setBoolean(2, chatbot.isEnabled());
            stmt.setString(3, chatbot.getChatbotType().name());
            stmt.executeUpdate();

            // Retrieve the generated chatbot_id
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    chatbot.setChatbotId(generatedKeys.getInt(1));
                }
            }
        }
    }


    public Chatbot getChatbotById(int chatbotId) throws SQLException {
        String sql = "SELECT * FROM chatbot WHERE chatbot_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, chatbotId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Chatbot(
                            rs.getInt("chatbot_id"),
                            rs.getInt("user_id"),
                            rs.getBoolean("is_enabled"),
                            Chatbot.ChatbotType.valueOf(rs.getString("chatbot_type"))
                    );
                }
            }
        }
        return null;
    }

    // Retrieve all Chatbot records
    public List<Chatbot> getAllChatbots() throws SQLException {
        List<Chatbot> chatbots = new ArrayList<>();
        String sql = "SELECT * FROM chatbot";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                chatbots.add(new Chatbot(
                        rs.getInt("chatbot_id"),
                        rs.getInt("user_id"),
                        rs.getBoolean("is_enabled"),
                        Chatbot.ChatbotType.valueOf(rs.getString("chatbot_type"))
                ));
            }
        }
        return chatbots;
    }


    public void updateChatbot(Chatbot chatbot) throws SQLException {
        String sql = "UPDATE chatbot SET user_id = ?, is_enabled = ?, chatbot_type = ? WHERE chatbot_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, chatbot.getUserId());
            stmt.setBoolean(2, chatbot.isEnabled());
            stmt.setString(3, chatbot.getChatbotType().name());
            stmt.setInt(4, chatbot.getChatbotId());
            stmt.executeUpdate();
        }
    }


    public void deleteChatbot(int chatbotId) throws SQLException {
        String sql = "DELETE FROM chatbot WHERE chatbot_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, chatbotId);
            stmt.executeUpdate();
        }
    }

    public void enableChatBot(int userID) throws SQLException {
        String sql = "UPDATE chatbot SET is_enabled = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1,true);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
        }
    }

    public void DisableChatBot(int userID) throws SQLException {
        String sql = "UPDATE chatbot SET is_enabled = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1,false);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
        }
    }

    public boolean isChatbotEnabled(int userID) throws SQLException {
        String sql = "SELECT is_enabled FROM chatbot WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_enabled");
                }
            }
        }
        return false;
    }

    public void addChatbotByUserID(int userID) {
        String sql = "INSERT INTO chatbot (user_id, is_enabled, chatbot_type) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setBoolean(2, false);
            stmt.setString(3, "pandorabots");
            stmt.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public Chatbot getChatbotByUserId(int userID) throws SQLException {
        String sql = "SELECT * FROM chatbot WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Chatbot(
                            rs.getInt("chatbot_id"),
                            rs.getInt("user_id"),
                            rs.getBoolean("is_enabled"),
                            Chatbot.ChatbotType.valueOf(rs.getString("chatbot_type"))
                    );
                }
            }
        }
        return null;
    }


















}
