package gov.iti.jets.server.model.dao.implementations;



import gov.iti.jets.server.model.dao.interfaces.UserConnectionDAOInt;
import shared.dto.UserConnection;
import shared.utils.DB_UtilityClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserConnectionDAOImpl implements UserConnectionDAOInt {


    public  boolean insertUserConnection(UserConnection userConnection) {

        String sql = "INSERT INTO user_connection (user_id, connected_user_id, relationship) VALUES (?, ?, ?)";
        try (Connection conn = DB_UtilityClass.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userConnection.getUserId());
            pstmt.setInt(2, userConnection.getConnectedUserId());
            pstmt.setString(3, userConnection.getRelationship());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public  boolean updateUserConnectionRelationship(int userId, int connectedUserId, String newRelationship) {

        String sql = "UPDATE user_connection SET relationship = ? WHERE user_id = ? AND connected_user_id = ?";
        try (Connection conn = DB_UtilityClass.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, newRelationship);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, connectedUserId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public  boolean deleteUserConnection(int userId, int connectedUserId) {
        String sql = "DELETE FROM user_connection WHERE user_id = ? AND connected_user_id = ?";
        try (Connection conn = DB_UtilityClass.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, connectedUserId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public  UserConnection getUserConnection(int userId, int connectedUserId) {

        UserConnection userConnection=null;
        String sql = "SELECT * FROM user_connection WHERE user_id = ? AND connected_user_id = ?";
        try (Connection conn = DB_UtilityClass.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, connectedUserId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                userConnection= new UserConnection(
                        rs.getInt("user_id"),
                        rs.getInt("connected_user_id"),
                        rs.getString("relationship")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userConnection;
    }


    public  List<UserConnection> getAllConnectionsForUser(int userId) {
        List<UserConnection> connections = new ArrayList<>();
        String sql = "SELECT * FROM user_connection WHERE user_id = ?";
        try (Connection conn = DB_UtilityClass.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            System.out.println("userid in func" + userId);

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                UserConnection userConnection=new UserConnection(rs.getInt("user_id"),rs.getInt("connected_user_id"),rs.getString("relationship"));
               connections.add(userConnection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connections;
    }
}

