package shared.dto;

import java.io.Serializable;

public class UserBlockedConnection implements Serializable {
    private Integer blockerUserId;
    private Integer blockedUserId;

    public UserBlockedConnection() {
    }

    public UserBlockedConnection(Integer blockerUserId, Integer blockedUserId) {
        this.blockerUserId = blockerUserId;
        this.blockedUserId = blockedUserId;
    }

    public Integer getBlockerUserId() {
        return blockerUserId;
    }

    public Integer getBlockedUserId() {
        return blockedUserId;
    }

    public void setBlockerUserId(Integer blockerUserId) {
        this.blockerUserId = blockerUserId;
    }

    public void setBlockedUserId(Integer blockedUserId) {
        this.blockedUserId = blockedUserId;
    }


    @Override
    public String toString() {
        return "UserBlockedConnection{" +
                "blockerUserId=" + blockerUserId +
                ", blockedUserId=" + blockedUserId +
                '}';
    }
}
