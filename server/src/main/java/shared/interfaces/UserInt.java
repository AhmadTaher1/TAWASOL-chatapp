package shared.interfaces;

import javafx.scene.layout.HBox;
import shared.dto.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface UserInt extends Remote {

    void register(ClientInt client) throws RemoteException;

    void unregister(ClientInt client) throws RemoteException;

    List<Card> getCards(User user) throws RemoteException;

    List<UserConnection> getUserConncectionById(int userId) throws RemoteException;

    User getUserById(int userId) throws RemoteException;

    boolean isUserFoundByPhoneNumber(String phone_number) throws RemoteException;

    boolean isUserFoundByEmail(String email) throws RemoteException;

    boolean addUsertoDB(User newUser) throws RemoteException;

    User isValidUser(String phoneNumber, String password) throws RemoteException;

    boolean editUserShownInfo(int userId, String name, User.Status status, String picPath, String bio) throws RemoteException;

    User getUserByPhoneNumber(String phone_number) throws RemoteException;

    boolean insertUserConnection(UserConnection userConnection) throws RemoteException;

    boolean addInvitation(Invitation invitation) throws RemoteException;

    Invitation getInvitationBySenderAndReciever(int senderId, int receiverId) throws RemoteException;

    List<BaseMessage> getGroupMessages(int groupId) throws RemoteException;

    List<ServerAnnouncement> getAllServerAnnouncements() throws RemoteException;

    List<BaseMessage> getMessagesBetweenTwo(int receiverId, int senderId) throws RemoteException;

    boolean insertDirectMessage(DirectMessage directMessage) throws RemoteException;

    void addGroupMessage(GroupMessage message) throws RemoteException;

    UserConnection getUserConnection(int userId, int connectedUserId) throws RemoteException;

    UserBlockedConnection getBlockedConnection(int blockerUserId, int blockedUserId) throws RemoteException;

    int createGroup(String groupName, int createdBy) throws RemoteException;

    void addUserToGroup(int userId, int groupId) throws RemoteException;

    List<Invitation> getAllInvitationsById(int userId) throws RemoteException;

    boolean addUserConnection(UserConnection userConnection) throws RemoteException;

    void deleteInvitation(int invitationId) throws RemoteException;

    boolean isUserConnection(int userId, int connectedUserId) throws RemoteException;

    String getCreatedGroupName(int groupId) throws RemoteException;

    public void addChatbot(Chatbot chatbot) throws RemoteException;

    boolean isChatbotEnabled(int userID) throws RemoteException;

    public void enableChatBot(int userID) throws RemoteException;

    public void disableChatBot(int userID) throws RemoteException;

    void addChatbotByUserID(int userID) throws RemoteException;

    public Chatbot getChatbotById(int userID) throws RemoteException;

    void pushSound(String phoneNumber) throws RemoteException;

    void reload(String phoneNumber, BaseMessage message, String type, int ID) throws RemoteException;

    List<Integer> getUsersByGroupId(int groupId) throws RemoteException;

    //to be implemented
    void reloadInvitationList(String phoneNumber) throws RemoteException;

    void reloadContactList(String phoneNumber, Card c) throws RemoteException;

    void reloadNotificationList(String phoneNumber) throws RemoteException;

    public String getSessionToken(String phoneNumber) throws RemoteException;

    public boolean validateToken(String phoneNumber, String token) throws RemoteException;

    List<Invitation> getAllAcceptedInvitationsBySenderId(int senderId) throws RemoteException;

    List<Invitation> getAllPendingInvitationsByReceiverId(int receiverId) throws RemoteException;

    void updateInvitationStatusById(int invitationId, Invitation.Status newStatus) throws RemoteException;

    UUID uploadFile(int senderId, Integer receiverId, Integer groupId, String fileName, String fileType, byte[] fileData) throws RemoteException;

    byte[] downloadFile(UUID fileId, int requesterId) throws RemoteException;

    String getFileName(UUID fileId) throws RemoteException;

    List<ServerAnnouncement> getAllServerAnnouncementsBasedOnCreatedTime(int userID) throws RemoteException;

    void reloadContactListwithAnnouncement() throws RemoteException;

}