package gov.iti.jets.server.model.dao.interfaces;

import shared.dto.DirectMessage;
import java.util.List;

public interface DirectMessageDAOInt {
    boolean insertDirectMessage(DirectMessage directMessage);
    DirectMessage getDirectMessageById(int messageId);
    List<DirectMessage> getMessagesBySenderId(int senderId);
    List<DirectMessage> getMessagesByReceiverId(int receiverId);
    boolean deleteDirectMessage(int messageId);
    //List<DirectMessage> getLastMessagesForUser(int receiverId) ;
     DirectMessage getLastMessageForUser(int senderId , int receiverId);
    }
