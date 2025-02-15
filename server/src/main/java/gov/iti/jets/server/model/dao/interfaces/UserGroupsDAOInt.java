package gov.iti.jets.server.model.dao.interfaces;


import shared.dto.UserGroups;

import java.sql.SQLException;
import java.util.List;

public interface UserGroupsDAOInt {

    boolean addUserToGroup(int userId, int groupId) throws SQLException;

    List<UserGroups> getGroupsByUserId(int userId) throws SQLException;

    List<Integer> getUsersByGroupId(int groupId) throws SQLException;

    boolean removeUserFromGroup(int userId, int groupId) throws SQLException;

    boolean removeAllUsersFromGroup(int groupId) throws SQLException;

    boolean isUserInGroup(int userId, int groupId) throws SQLException;
}
