package gov.iti.jets.server.model.dao.interfaces;

import shared.dto.ServerAnnouncement;

import java.sql.SQLException;
import java.util.List;

public interface ServerAnnouncementDAOInt {
    void addServerAnnouncement(ServerAnnouncement announcement) throws SQLException;
    ServerAnnouncement getServerAnnouncementById(int announcementId) throws SQLException;
    List<ServerAnnouncement> getAllServerAnnouncements() throws SQLException;
    void updateServerAnnouncement(ServerAnnouncement announcement) throws SQLException;
    void deleteServerAnnouncement(int announcementId) throws SQLException;
    ServerAnnouncement getLatestAnnouncement()  throws SQLException ;
    List<ServerAnnouncement> getAllServerAnnouncementsBasedOnCreatedTime(int userID) throws SQLException ;

    }
