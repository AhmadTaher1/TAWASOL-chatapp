package gov.iti.jets.server.model.dao.interfaces;

import shared.dto.User;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface GroupDAOInt {
    void createGroup(String groupName, int createdBy) throws SQLException;
    int createGroupWithId(String groupName, int createdBy) throws SQLException;
    List<Map<String, Object>> getAllGroups() throws SQLException;
    void updateGroupName(int groupId, String groupName) throws SQLException;
    void deleteGroup(int groupId) throws SQLException;
    List<Map<String, Object>> getGroupsByUser(int userId) throws SQLException;
    String getGroupNameById(int groupId) throws SQLException ;
    User getCreatedGroup(int groupId) throws SQLException ;
    Timestamp getTimeStampOfGroupById(int groupId) throws SQLException ;

    }
