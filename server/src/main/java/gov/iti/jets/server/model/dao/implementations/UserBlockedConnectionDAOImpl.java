package gov.iti.jets.server.model.dao.implementations;


import gov.iti.jets.server.model.dao.interfaces.UserBlockedConnectionDAOInt;
import gov.iti.jets.server.model.dao.interfaces.UserConnectionDAOInt;
import shared.dto.UserBlockedConnection;
import shared.utils.DB_UtilityClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserBlockedConnectionDAOImpl implements UserBlockedConnectionDAOInt {


    public boolean insertBlockedConnection(UserBlockedConnection blockedConnection) {

        //delete it from the connection list before inserting it in the blocklist
        //here I changed it from UserBlockedConnection to UserConnection
        UserConnectionDAOInt userConnectionDAO = new UserConnectionDAOImpl();
        userConnectionDAO.deleteUserConnection(blockedConnection.getBlockerUserId(), blockedConnection.getBlockedUserId());
        boolean result = false;
        String sql = "INSERT INTO user_blocked_connection (blocker_user_id, blocked_user_id) VALUES (?, ?)";
        try (Connection conn = DB_UtilityClass.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, blockedConnection.getBlockerUserId());
            pstmt.setInt(2, blockedConnection.getBlockedUserId());
            result = pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public boolean deleteBlockedConnection(UserBlockedConnection blockedConnection) {
        Boolean result = false;
        String sql = "DELETE FROM user_blocked_connection WHERE blocker_user_id = ? AND blocked_user_id = ?";
        try (Connection conn = DB_UtilityClass.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, blockedConnection.getBlockerUserId());
            pstmt.setInt(2, blockedConnection.getBlockedUserId());
            result = pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public boolean deleteBlockedConnection(int blockerUserId, int blockedUserId) {

        Boolean result = false;
        String sql = "DELETE FROM user_blocked_connection WHERE blocker_user_id = ? AND blocked_user_id = ?";
        try (Connection conn = DB_UtilityClass.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, blockerUserId);
            pstmt.setInt(2, blockedUserId);
            result = pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public UserBlockedConnection getBlockedConnection(int blockerUserId, int blockedUserId) {

        UserBlockedConnection userBlockedConnection = null;
        String sql = "SELECT * FROM user_blocked_connection " +
                "WHERE (blocker_user_id = ? AND blocked_user_id = ?) " +
                "   OR (blocker_user_id = ? AND blocked_user_id = ?)";
        try (Connection conn = DB_UtilityClass.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, blockerUserId);
            pstmt.setInt(2, blockedUserId);
            pstmt.setInt(3, blockedUserId);
            pstmt.setInt(4, blockerUserId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userBlockedConnection = new UserBlockedConnection(
                        rs.getInt("blocker_user_id"),
                        rs.getInt("blocked_user_id")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userBlockedConnection;
    }


    public List<UserBlockedConnection> getAllBlockedConnectionsForUser(int blockerUserId) {

        List<UserBlockedConnection> blockedConnections = new ArrayList<>();
        String sql = "SELECT * FROM user_blocked_connection WHERE blocker_user_id = ?";
        try (Connection conn = DB_UtilityClass.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, blockerUserId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                UserBlockedConnection userBlockedConnection = new UserBlockedConnection(
                        rs.getInt("blocker_user_id"),
                        rs.getInt("blocked_user_id")
                );

                blockedConnections.add(userBlockedConnection);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blockedConnections;
    }
}
