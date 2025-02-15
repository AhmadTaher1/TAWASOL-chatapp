package gov.iti.jets.server.model.dao.implementations;


import gov.iti.jets.server.model.dao.interfaces.SocialNetworkDAOInt;
import shared.dto.SocialNetwork;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SocialNetworkDAOImpl implements SocialNetworkDAOInt {

    private Connection connection;

    public SocialNetworkDAOImpl(Connection connection) {
        this.connection = connection;
    }

    public void addSocialNetwork(SocialNetwork socialNetwork) throws SQLException {
        String sql = "INSERT INTO social_network (user_id, platform, access_token) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, socialNetwork.getUserId());
            stmt.setString(2, socialNetwork.getPlatform().name());
            stmt.setString(3, socialNetwork.getAccessToken());
            stmt.executeUpdate();


            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int socialId = generatedKeys.getInt(1);
                    socialNetwork.setSocialId(socialId);
                }
            }
        }
    }

    public SocialNetwork getSocialNetworkById(int socialId) throws SQLException {
        String sql = "SELECT * FROM social_network WHERE social_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, socialId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SocialNetwork(
                        rs.getInt("social_id"),
                        rs.getInt("user_id"),
                        SocialNetwork.Platform.valueOf(rs.getString("platform")),
                        rs.getString("access_token")
                );
            }
        }
        return null;
    }

    public List<SocialNetwork> getAllSocialNetworks() throws SQLException {
        List<SocialNetwork> socialNetworks = new ArrayList<>();
        String sql = "SELECT * FROM social_network";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                socialNetworks.add(new SocialNetwork(
                        rs.getInt("social_id"),
                        rs.getInt("user_id"),
                        SocialNetwork.Platform.valueOf(rs.getString("platform")),
                        rs.getString("access_token")
                ));
            }
        }
        return socialNetworks;
    }

    public void updateSocialNetwork(SocialNetwork socialNetwork) throws SQLException {
        String sql = "UPDATE social_network SET user_id = ?, platform = ?, access_token = ? WHERE social_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, socialNetwork.getUserId());
            stmt.setString(2, socialNetwork.getPlatform().name());
            stmt.setString(3, socialNetwork.getAccessToken());
            stmt.setInt(4, socialNetwork.getSocialId());
            stmt.executeUpdate();
        }
    }

    public void deleteSocialNetwork(int socialId) throws SQLException {
        String sql = "DELETE FROM social_network WHERE social_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, socialId);
            stmt.executeUpdate();
        }
    }
}
