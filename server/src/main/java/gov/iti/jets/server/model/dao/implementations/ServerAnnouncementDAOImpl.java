package gov.iti.jets.server.model.dao.implementations;



import gov.iti.jets.server.model.dao.interfaces.ServerAnnouncementDAOInt;
import shared.dto.ServerAnnouncement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServerAnnouncementDAOImpl implements ServerAnnouncementDAOInt {

    private Connection connection;

    public ServerAnnouncementDAOImpl(Connection connection) {
        this.connection = connection;
    }


    public void addServerAnnouncement(ServerAnnouncement announcement) throws SQLException {
        String sql = "INSERT INTO server_announcement (message, created_at) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, announcement.getMessage());
            stmt.setTimestamp(2, announcement.getCreatedAt());
            stmt.executeUpdate();

            // Retrieve the generated announcement_id
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    announcement.setAnnouncementId(generatedKeys.getInt(1));
                }
            }
        }
    }


    public ServerAnnouncement getServerAnnouncementById(int announcementId) throws SQLException {
        String sql = "SELECT * FROM server_announcement WHERE announcement_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, announcementId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToServerAnnouncement(rs);
                }
            }
        }
        return null;
    }


    public List<ServerAnnouncement> getAllServerAnnouncements() throws SQLException {
        List<ServerAnnouncement> announcements = new ArrayList<>();
        String sql = "SELECT * FROM server_announcement ORDER BY created_at ASC";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                announcements.add(mapResultSetToServerAnnouncement(rs));
            }
        }
        return announcements;
    }


    public void updateServerAnnouncement(ServerAnnouncement announcement) throws SQLException {
        String sql = "UPDATE server_announcement SET message = ?, created_at = ? WHERE announcement_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, announcement.getMessage());
            stmt.setTimestamp(2, announcement.getCreatedAt());
            stmt.setInt(3, announcement.getAnnouncementId());
            stmt.executeUpdate();
        }
    }


    public void deleteServerAnnouncement(int announcementId) throws SQLException {
        String sql = "DELETE FROM server_announcement WHERE announcement_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, announcementId);
            stmt.executeUpdate();
        }
    }

    @Override
    public ServerAnnouncement getLatestAnnouncement() throws SQLException {
        String sql = "SELECT announcement_id, message, created_at FROM server_announcement ORDER BY created_at DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new ServerAnnouncement(
                        rs.getInt("announcement_id"),
                        rs.getString("message"),
                        rs.getTimestamp("created_at")
                );
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ServerAnnouncement> getAllServerAnnouncementsBasedOnCreatedTime(int userID) throws SQLException {
        List<ServerAnnouncement> announcements = new ArrayList<>();
        String sql = "SELECT * from server_announcement where created_at > (SELECT last_seen from user where user_id= ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                announcements.add(mapResultSetToServerAnnouncement(rs));
            }
        }
        return announcements;
    }

    private ServerAnnouncement mapResultSetToServerAnnouncement(ResultSet rs) throws SQLException {
        return new ServerAnnouncement(
                rs.getInt("announcement_id"),
                rs.getString("message"),
                rs.getTimestamp("created_at")
        );
    }
}
