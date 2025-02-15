package shared.dto;

import java.io.Serializable;

public class Chatbot implements Serializable {

    private int chatbotId;
    private int userId;
    private boolean isEnabled;
    private ChatbotType chatbotType;

    public enum ChatbotType {
        cleverbot,
        pandorabots,
        jabberwacky,
        aiml
    }

    public Chatbot(int chatbotId, int userId, boolean isEnabled, ChatbotType chatbotType) {
        this.chatbotId = chatbotId;
        this.userId = userId;
        this.isEnabled = isEnabled;
        this.chatbotType = chatbotType;
    }

    public int getChatbotId() {
        return chatbotId;
    }

    public void setChatbotId(int chatbotId) {
        this.chatbotId = chatbotId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public ChatbotType getChatbotType() {
        return chatbotType;
    }

    public void setChatbotType(ChatbotType chatbotType) {
        this.chatbotType = chatbotType;
    }
}
