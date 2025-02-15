package gov.iti.jets.server.model.dao.interfaces;

import shared.dto.SocialNetwork;

import java.sql.SQLException;
import java.util.List;

public interface SocialNetworkDAOInt {
    void addSocialNetwork(SocialNetwork socialNetwork) throws SQLException;
    SocialNetwork getSocialNetworkById(int socialId) throws SQLException;
    List<SocialNetwork> getAllSocialNetworks() throws SQLException;
    void updateSocialNetwork(SocialNetwork socialNetwork) throws SQLException;
    void deleteSocialNetwork(int socialId) throws SQLException;
}
