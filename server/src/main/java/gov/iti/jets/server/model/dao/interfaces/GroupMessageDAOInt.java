package gov.iti.jets.server.model.dao.interfaces;

import shared.dto.GroupMessage;

import java.sql.SQLException;
import java.util.List;

public interface GroupMessageDAOInt {
    void addGroupMessage(GroupMessage message) throws SQLException;
    GroupMessage getGroupMessageById(int messageId) throws SQLException;
    List<GroupMessage> getAllGroupMessages() throws SQLException;
    void updateGroupMessage(GroupMessage message) throws SQLException;
    void deleteGroupMessage(int messageId) throws SQLException;
    GroupMessage getLatestMessageInGroup(int groupId) throws SQLException ;

    }
