package shared.dto;

import java.io.Serializable;

public class UserConnection implements Serializable {
    private Integer userId;
    private Integer connectedUserId;
    private String relationship;

    public UserConnection() {
    }

    public UserConnection(Integer userId, Integer connectedUserId, String relationship) {
        this.userId = userId;
        this.connectedUserId = connectedUserId;
        this.relationship = relationship;
    }


    public Integer getUserId() {
        return userId;
    }

    public Integer getConnectedUserId() {
        return connectedUserId;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setConnectedUserId(Integer connectedUserId) {
        this.connectedUserId = connectedUserId;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Override
    public String toString() {
        return "UserConnection{" +
                "userId=" + userId +
                ", connectedUserId=" + connectedUserId +
                ", relationship='" + relationship + '\'' +
                '}';
    }

}
