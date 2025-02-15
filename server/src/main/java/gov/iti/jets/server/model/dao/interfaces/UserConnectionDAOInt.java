package gov.iti.jets.server.model.dao.interfaces;

import shared.dto.UserConnection;

import java.util.List;

public interface UserConnectionDAOInt {
    boolean insertUserConnection(UserConnection userConnection);

    boolean updateUserConnectionRelationship(int userId, int connectedUserId, String newRelationship);

    boolean deleteUserConnection(int userId, int connectedUserId);

    UserConnection getUserConnection(int userId, int connectedUserId);

    List<UserConnection> getAllConnectionsForUser(int userId);


}
