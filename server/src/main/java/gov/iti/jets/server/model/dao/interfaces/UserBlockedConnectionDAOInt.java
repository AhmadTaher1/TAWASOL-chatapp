package gov.iti.jets.server.model.dao.interfaces;

import shared.dto.UserBlockedConnection;

import java.util.List;

public interface UserBlockedConnectionDAOInt {
    boolean insertBlockedConnection(UserBlockedConnection blockedConnection);

    boolean deleteBlockedConnection(UserBlockedConnection blockedConnection);

    boolean deleteBlockedConnection(int blockerUserId, int blockedUserId);

    UserBlockedConnection getBlockedConnection(int blockerUserId, int blockedUserId);

    List<UserBlockedConnection> getAllBlockedConnectionsForUser(int blockerUserId);
}
