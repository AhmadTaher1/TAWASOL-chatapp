package gov.iti.jets.server.model.dao.interfaces;

import shared.dto.Invitation;

import java.util.List;

public interface InvitationDAOInt {
    boolean addInvitation(Invitation invitation);
    Invitation getInvitationById(int invitationId);
    void updateInvitationStatus(int invitationId, Invitation.Status newStatus);
    void deleteInvitation(int invitationId);
    List<Invitation> getAllInvitations();
    List<Invitation> getAllInvitationsByReceiverId(int receiverId);
    List<Invitation> getAllAcceptedInvitationsBySenderId(int senderId);
    List<Invitation> getAllPendingInvitationsByReceiverId(int receiverId);
    void updateInvitationStatusById(int invitationId, Invitation.Status newStatus); 
    
}
