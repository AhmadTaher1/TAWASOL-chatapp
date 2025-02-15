package shared.dto;

import java.io.Serializable;
import java.sql.Timestamp;


public class Card implements Serializable {

    private int id;
    private String type;
    private String senderName;
    private String messageContent;
    private Timestamp timestamp;
    private User.Status status;
    private String imagePath;

    public enum Type {
        user, group, announcement

    }

    public Card(int id, String type, String senderName, String messageContent, Timestamp timestamp, User.Status status, String imagePath) {
        this.id = id;
        this.type = type;
        this.senderName = senderName;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
        this.status = status;
        this.imagePath = imagePath;
    }

    public Card() {

    }


    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setTimeStamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(User.Status status) {
        this.status = status;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public User.Status getStatus() {
        return status;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getId() {
        return id;
    }


}
