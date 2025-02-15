package gov.iti.jets.server.model.dao.implementations;


import gov.iti.jets.server.model.dao.interfaces.UserGroupsDAOInt;
import shared.dto.UserGroups;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserGroupsDAOImpl implements UserGroupsDAOInt {
    private final Connection connection;

    public UserGroupsDAOImpl(Connection connection) {
        this.connection = connection;
    }

    public boolean addUserToGroup(int userId, int groupId) throws SQLException {
        String query = "INSERT INTO user_groups (user_id, group_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, groupId);
            return preparedStatement.executeUpdate() > 0;
        }
    }


    public List<UserGroups> getGroupsByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM user_groups WHERE user_id = ?";
        List<UserGroups> userGroupsList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    userGroupsList.add(new UserGroups(
                            resultSet.getInt("user_id"),
                            resultSet.getInt("group_id")
                    ));
                }
            }
        }
        return userGroupsList;
    }


    public List<UserGroups> getUserGroupsByGroupId(int groupId) throws SQLException {
        String query = "SELECT * FROM user_groups WHERE group_id = ?";
        List<UserGroups> userGroupsList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, groupId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    userGroupsList.add(new UserGroups(
                            resultSet.getInt("user_id"),
                            resultSet.getInt("group_id")
                    ));
                }
            }
        }
        return userGroupsList;
    }


    public boolean removeUserFromGroup(int userId, int groupId) throws SQLException {
        String query = "DELETE FROM user_groups WHERE user_id = ? AND group_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, groupId);
            return preparedStatement.executeUpdate() > 0;
        }
    }


    public boolean removeAllUsersFromGroup(int groupId) throws SQLException {
        String query = "DELETE FROM user_groups WHERE group_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, groupId);
            return preparedStatement.executeUpdate() > 0;
        }
    }


    public boolean isUserInGroup(int userId, int groupId) throws SQLException {
        String query = "SELECT 1 FROM user_groups WHERE user_id = ? AND group_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, groupId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public List<Integer> getUsersByGroupId(int groupId) throws SQLException {
        String query = "SELECT user_id FROM user_groups WHERE group_id = ?";
        List<Integer> usersIDs = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, groupId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    usersIDs.add (resultSet.getInt("user_id"));

                }
            }
        }
        return usersIDs;
    }



}

