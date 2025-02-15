package gov.iti.jets.server.model.dao.implementations;


import gov.iti.jets.server.model.dao.interfaces.InvitationDAOInt;
import shared.dto.Invitation;
import shared.dto.UserBlockedConnection;
import shared.dto.UserConnection;
import shared.utils.DB_UtilityClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class InvitationDAOImpl implements InvitationDAOInt {


    public boolean addInvitation(Invitation invitation) {

        boolean result = false;
        String query = "INSERT INTO invitation (sender_id, receiver_id, status) VALUES (?, ?, ?)";
        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, invitation.getSenderId());
            statement.setInt(2, invitation.getReceiverId());
            statement.setString(3, invitation.getStatus().toString());
            result = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public Invitation getInvitationById(int invitationId) {

        Invitation invitation = null;
        String query = "SELECT * FROM invitation WHERE invitation_id = ?";
        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, invitationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    invitation = new Invitation(
                            resultSet.getInt("invitation_id"),
                            resultSet.getInt("sender_id"),
                            resultSet.getInt("receiver_id"),
                            Invitation.Status.valueOf(resultSet.getString("status"))

                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invitation;
    }


    public void updateInvitationStatus(int invitationId, Invitation.Status newStatus) {
        String query = "UPDATE invitation SET status = ? WHERE invitation_id = ?";
        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newStatus.toString());
            statement.setInt(2, invitationId);
            statement.executeUpdate();
            handleStatusChange(invitationId, newStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateInvitationStatusById(int invitationId, Invitation.Status newStatus) {
        String query = "UPDATE invitation SET status = ? WHERE invitation_id = ?";
        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newStatus.toString());
            statement.setInt(2, invitationId);
            statement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleStatusChange(int invitationId, Invitation.Status newStatus) {
        InvitationDAOInt invitationDAO = new InvitationDAOImpl();
        switch (newStatus) {
            case accepted -> {
                Invitation invitation = invitationDAO.getInvitationById(invitationId);
                UserConnection userConnection = new UserConnection(invitation.getReceiverId(), invitation.getSenderId(), "Family");
                new UserConnectionDAOImpl().insertUserConnection(userConnection);
                invitationDAO.deleteInvitation(invitationId);
            }
            case rejected -> {
                invitationDAO.deleteInvitation(invitationId);
            }
            case blocked -> {
                Invitation invitation = invitationDAO.getInvitationById(invitationId);
                UserBlockedConnection userBlockedConnection = new UserBlockedConnection(invitation.getReceiverId(), invitation.getSenderId());
                new UserBlockedConnectionDAOImpl().insertBlockedConnection(userBlockedConnection);
                invitationDAO.deleteInvitation(invitationId);
            }
            case pending -> {

            }
        }
    }

    public void deleteInvitation(int invitationId) {
        String query = "DELETE FROM invitation WHERE invitation_id = ?";
        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, invitationId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Invitation> getAllInvitations() {
        List<Invitation> invitations = new ArrayList<>();
        String query = "SELECT * FROM invitation";
        try (Connection connection = DB_UtilityClass.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {

                Invitation invitation = (new Invitation(
                        resultSet.getInt("invitation_id"),
                        resultSet.getInt("sender_id"),
                        resultSet.getInt("receiver_id"),
                        Invitation.Status.valueOf(resultSet.getString("status"))
                ));
                invitations.add(invitation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invitations;
    }


    public Invitation getInvitationBySenderAndReciever(int senderId, int receiverId) {
        {

            Invitation invitation = null;
            String query = "SELECT * FROM invitation WHERE sender_id  = ? And receiver_id = ?";
            try (Connection connection = DB_UtilityClass.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, senderId);
                statement.setInt(2, receiverId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        invitation = new Invitation(
                                resultSet.getInt("invitation_id"),
                                resultSet.getInt("sender_id"),
                                resultSet.getInt("receiver_id"),
                                Invitation.Status.valueOf(resultSet.getString("status"))

                        );
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return invitation;
        }

    }

    public List<Invitation> getAllInvitationsByReceiverId(int receiverId) {
        List<Invitation> invitations = new ArrayList<>();
        Invitation invitation = null;
        String query = "SELECT * FROM invitation WHERE receiver_id = ?";

        try (Connection connection = DB_UtilityClass.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
       statement.setInt(1, receiverId);
       try (ResultSet resultSet = statement.executeQuery()) {
           while (resultSet.next()) {
               invitation = new Invitation(
                       resultSet.getInt("invitation_id"),
                       resultSet.getInt("sender_id"),
                       resultSet.getInt("receiver_id"),
                       Invitation.Status.valueOf(resultSet.getString("status"))

               );
               invitations.add(invitation);
           }

               
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invitations;
    }

    public List<Invitation> getAllAcceptedInvitationsBySenderId(int senderId) {
        List<Invitation> invitations = new ArrayList<>();
        Invitation invitation = null;
        String query = "SELECT * FROM invitation WHERE sender_id = ? AND status = 'accepted'";

        try (Connection connection = DB_UtilityClass.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
       statement.setInt(1, senderId);

       try (ResultSet resultSet = statement.executeQuery()) {
           while (resultSet.next()) {
               invitation = new Invitation(
                       resultSet.getInt("invitation_id"),
                       resultSet.getInt("sender_id"),
                       resultSet.getInt("receiver_id"),
                       Invitation.Status.valueOf(resultSet.getString("status"))

               );
               invitations.add(invitation);
           }

               
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invitations;
    }

    public List<Invitation> getAllPendingInvitationsByReceiverId(int receiverId) {
        List<Invitation> invitations = new ArrayList<>();
        Invitation invitation = null;
        String query = "SELECT * FROM invitation WHERE receiver_id = ? AND status = 'pending'";

        try (Connection connection = DB_UtilityClass.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
       statement.setInt(1, receiverId);

       try (ResultSet resultSet = statement.executeQuery()) {
           while (resultSet.next()) {
               invitation = new Invitation(
                       resultSet.getInt("invitation_id"),
                       resultSet.getInt("sender_id"),
                       resultSet.getInt("receiver_id"),
                       Invitation.Status.valueOf(resultSet.getString("status"))

               );
               invitations.add(invitation);
           }

               
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invitations;
    }
}
