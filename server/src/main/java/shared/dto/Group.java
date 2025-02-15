package shared.dto;

import java.io.Serializable;
import java.security.Timestamp;

public class Group implements Serializable {
    private int groupId;
    private String groupName;
    private int createdBy;
    private Timestamp createdWhen;

    public Group() {
    }

    public Group(int groupId, String groupName, int createdBy, Timestamp createdWhen) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.createdBy = createdBy;
        this.createdWhen = createdWhen;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedWhen() {
        return createdWhen;
    }

    public void setCreatedWhen(Timestamp createdWhen) {
        this.createdWhen = createdWhen;
    }

    @Override
    public String toString() {
        return String.format("Group ID: %d, Group Name: %s, Created By: %d", groupId, groupName, createdBy);
    }
}
