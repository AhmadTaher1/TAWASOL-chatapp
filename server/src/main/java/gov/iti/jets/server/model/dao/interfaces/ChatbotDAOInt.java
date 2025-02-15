package gov.iti.jets.server.model.dao.interfaces;


import shared.dto.Chatbot;

import java.sql.SQLException;
import java.util.List;

public interface ChatbotDAOInt {
    void addChatbot(Chatbot chatbot) throws SQLException;
    Chatbot getChatbotById(int chatbotId) throws SQLException;
    List<Chatbot> getAllChatbots() throws SQLException;
    void updateChatbot(Chatbot chatbot) throws SQLException;
    void deleteChatbot(int chatbotId) throws SQLException;
}
