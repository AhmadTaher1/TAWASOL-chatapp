package shared.dto;

import java.io.Serializable;

public class UserGroups implements Serializable {
    private int userId;
    private int groupId;

    public UserGroups() {
    }

    public UserGroups(int userId, int groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return String.format("User ID: %d, Group ID: %d", userId, groupId);
    }
}