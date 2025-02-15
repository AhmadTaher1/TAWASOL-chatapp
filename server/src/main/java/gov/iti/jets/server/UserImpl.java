package gov.iti.jets.server;


import gov.iti.jets.server.model.dao.implementations.*;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import shared.dto.Invitation;
import shared.dto.User;
import shared.dto.UserConnection;
import gov.iti.jets.server.model.dao.interfaces.DirectMessageDAOInt;
import shared.dto.*;
import shared.interfaces.ClientInt;
import shared.interfaces.UserInt;
import shared.utils.DB_UtilityClass;
import shared.utils.JakartaMail;
import shared.utils.SecureStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserImpl extends UnicastRemoteObject implements UserInt {


    static List<ClientInt> OnlineClintsList = new ArrayList<>();
    private final AdminDAOImpl adminDAO;
    private final ChatbotDAOImpl chatbotDAO;
    private final DirectMessageDAOImpl directMessageDAO;
    private final FileTransferDAOImpl fileTransferDAO;
    private final GroupDAOImpl groupDAO;
    private final GroupMessageDAOImpl groupMessageDAO;
    private final InvitationDAOImpl invitationDAO;
    private final ServerAnnouncementDAOImpl serverAnnouncementDAO;
    private final SocialNetworkDAOImpl socialNetworkDAO;
    private final UserBlockedConnectionDAOImpl userBlockedConnectionDAO;
    private final UserConnectionDAOImpl userConnectionDAO;
    private final UserDAOImpl userDAO;
    private final UserGroupsDAOImpl userGroupsDAO;
    private Connection connection = DB_UtilityClass.getConnection();
    private final HashMap<String, String> sessionTokens;
    private final java.nio.file.Path storagePath = Paths.get("./server_files/");
    //private Path storagePath;


    protected UserImpl() throws RemoteException {

        this.userConnectionDAO = new UserConnectionDAOImpl();
        this.adminDAO = new AdminDAOImpl(connection);
        this.chatbotDAO = new ChatbotDAOImpl(connection);
        this.directMessageDAO = new DirectMessageDAOImpl();
        this.fileTransferDAO = new FileTransferDAOImpl();
        this.groupDAO = new GroupDAOImpl(connection);
        this.groupMessageDAO = new GroupMessageDAOImpl(connection);
        this.invitationDAO = new InvitationDAOImpl();
        this.serverAnnouncementDAO = new ServerAnnouncementDAOImpl(connection);
        this.socialNetworkDAO = new SocialNetworkDAOImpl(connection);
        this.userBlockedConnectionDAO = new UserBlockedConnectionDAOImpl();
        this.userDAO = new UserDAOImpl();
        this.userGroupsDAO = new UserGroupsDAOImpl(connection);
        sessionTokens = new HashMap<>();

        try {
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create storage directory", e);
        }

    }


    @Override
    public void register(ClientInt client) throws RemoteException {

        OnlineClintsList.add(client);


    }

    @Override
    public void unregister(ClientInt client) throws RemoteException {

        System.out.println("Before: " + OnlineClintsList.size());
        OnlineClintsList.remove(client);
        System.out.println("After: " + OnlineClintsList.size());
        //deletes the session token
//        sessionTokens.remove(client.getPhoneNumber());


    }

    @Override
    public List<Card> getCards(User user) throws RemoteException {

        List<Card> cardList = new ArrayList<>();

        List<UserConnection> listofConnections = userConnectionDAO.getAllConnectionsForUser(user.getUserId());
        System.out.println(listofConnections.size());

        for (UserConnection userConnection : listofConnections) {

            DirectMessage Message = directMessageDAO.getLastMessageForUser(user.getUserId(), userConnection.getConnectedUserId());
            FileTransfer fileTransfer = fileTransferDAO.getLastFileBetweenUsers(user.getUserId(), userConnection.getConnectedUserId());
            System.out.println(fileTransfer);
            User connecterUser = userDAO.getUserById(userConnection.getConnectedUserId());


            Card card = new Card();
            card.setId(connecterUser.getUserId());
            card.setType(Card.Type.user.toString());
            card.setSenderName(connecterUser.getDisplayName());
            card.setStatus(connecterUser.getStatus());
            card.setImagePath(connecterUser.getProfilePicturePath());


            if (Message.getMessageContent() == null && fileTransfer == null) {
                card.setMessageContent("");
                card.setTimeStamp(connecterUser.getLastSeen());
            } else {
                if (fileTransfer.getFileName() == null && Message.getMessageContent() != null) {
                    card.setMessageContent(Message.getMessageContent());
                    card.setTimeStamp(Message.getTimestamp());
                } else if (fileTransfer.getFileName() != null && Message.getMessageContent() == null) {
                    card.setMessageContent(fileTransfer.getFileName());
                    card.setTimeStamp(Message.getTimestamp());
                } else if (Message.getMessageContent() != null && fileTransfer.getFileName() != null) {
                    if (Message.getTimestamp().after(fileTransfer.getTimestamp())) {
                        card.setMessageContent(Message.getMessageContent());
                        card.setTimeStamp(Message.getTimestamp());
                    } else {
                        card.setMessageContent(fileTransfer.getFileName());
                        card.setTimeStamp(fileTransfer.getTimestamp());

                    }
                }
            }


            cardList.add(card);
        }


        try {

            List<UserGroups> groupsList = userGroupsDAO.getGroupsByUserId(user.getUserId());
            for (UserGroups group : groupsList) {
                Card card = new Card();
                //System.out.println(group.getGroupId());
                GroupMessage groupMessage = groupMessageDAO.getLatestMessageInGroup(group.getGroupId());
                FileTransfer fileTransfer = fileTransferDAO.getLastFileByGroupId(group.getGroupId());
                if (groupMessage.getMessageContent() == null && fileTransfer.getFileName() == null) {
                    card.setId(group.getGroupId());
                    card.setType(Card.Type.group.toString());
                    // User sender = userDAO.getUserById(groupMessage.getSenderId());
                    card.setStatus(User.Status.AVAILABLE);
                    card.setSenderName(groupDAO.getGroupNameById(group.getGroupId()));
                    card.setImagePath("/img/people.png");
                    card.setMessageContent("");
                    card.setTimeStamp(groupDAO.getTimeStampOfGroupById(group.getGroupId()));
                } else if (groupMessage.getMessageContent() != null && fileTransfer.getFileName() == null) {
                    card.setId(group.getGroupId());
                    card.setType(Card.Type.group.toString());
                    // User sender = userDAO.getUserById(groupMessage.getSenderId());
                    card.setStatus(User.Status.AVAILABLE);
                    card.setSenderName(groupDAO.getGroupNameById(group.getGroupId()));
                    card.setImagePath("/img/people.png");
                    card.setMessageContent(groupMessage.getMessageContent());
                    card.setTimeStamp(groupMessage.getTimestamp());
                } else if (groupMessage.getMessageContent() == null && fileTransfer.getFileName() != null) {
                    card.setId(group.getGroupId());
                    card.setType(Card.Type.group.toString());
                    // User sender = userDAO.getUserById(groupMessage.getSenderId());
                    card.setStatus(User.Status.AVAILABLE);
                    card.setSenderName(groupDAO.getGroupNameById(group.getGroupId()));
                    card.setImagePath("/img/people.png");
                    card.setMessageContent(fileTransfer.getFileName());
                    card.setTimeStamp(fileTransfer.getTimestamp());
                } else {
                    card.setId(group.getGroupId());
                    card.setType(Card.Type.group.toString());
                    // User sender = userDAO.getUserById(groupMessage.getSenderId());
                    card.setStatus(User.Status.AVAILABLE);
                    card.setSenderName(groupDAO.getGroupNameById(group.getGroupId()));
                    card.setImagePath("/img/people.png");

                    if (groupMessage.getTimestamp().after(fileTransfer.getTimestamp())) {
                        card.setMessageContent(groupMessage.getMessageContent());
                        card.setTimeStamp(groupMessage.getTimestamp());
                    } else {
                        card.setMessageContent(fileTransfer.getFileName());
                        card.setTimeStamp(fileTransfer.getTimestamp());
                    }
                }
                cardList.add(card);

            }

            ServerAnnouncement serverAnnouncement = serverAnnouncementDAO.getLatestAnnouncement();
            if (serverAnnouncement != null) {
                Card announcementCard = new Card();
                announcementCard.setId(serverAnnouncement.getAnnouncementId());
                announcementCard.setType(Card.Type.announcement.toString());


                if (serverAnnouncement.getCreatedAt().after(user.getLastSeen())) {
                    //System.out.println("heeereeee");
                    announcementCard.setTimeStamp(serverAnnouncement.getCreatedAt());
                    announcementCard.setMessageContent(serverAnnouncement.getMessage());
                } else {
                    announcementCard.setTimeStamp(serverAnnouncement.getCreatedAt());
                    announcementCard.setMessageContent("");
                }
                announcementCard.setSenderName("TAWASOL");
                announcementCard.setStatus(User.Status.AVAILABLE);
                announcementCard.setImagePath("/img/setting.png");
                cardList.add(announcementCard);
            }

        } catch (SQLException ex) {
            System.out.println("SQL Exception");
        }


        cardList.sort(Comparator.comparing(Card::getTimestamp, Comparator.nullsLast(Comparator.reverseOrder())));
        return cardList;
    }

    // List<UserConnection> getAllConnectionsForUser(int userId);
    @Override
    public List<UserConnection> getUserConncectionById(int userId) throws RemoteException {
        System.out.println("hello");
        return userConnectionDAO.getAllConnectionsForUser(userId);
    }

    @Override
    public User getUserById(int userId) throws RemoteException {
        return userDAO.getUserById(userId);
    }

    public static int getOnlineUsers() {
        return OnlineClintsList.size();
    }

    @Override
    public boolean isUserFoundByPhoneNumber(String phone_number) {
        return userDAO.isUserFoundByPhoneNumber(phone_number);
    }

    @Override
    public boolean isUserFoundByEmail(String email) throws RemoteException {
        return userDAO.isUserFoundByEmail(email);
    }

    @Override
    public boolean addUsertoDB(User newUser) throws RemoteException {
        return userDAO.createUserWithoutID(newUser);
    }

    @Override
    public User isValidUser(String phoneNumber, String password) throws RemoteException {
        return userDAO.getUserByPhoneNumberAndPassword(phoneNumber, password);
    }

    @Override
    public boolean editUserShownInfo(int userId, String name, User.Status status, String picPath, String bio) throws RemoteException {
        return userDAO.editUserShownInfo(userId, name, status, picPath, bio);
    }

    public User getUserByPhoneNumber(String phone_number) {

        return userDAO.getUserByPhoneNumber(phone_number);
    }

    @Override
    public boolean insertUserConnection(UserConnection userConnection) throws RemoteException {
        return userConnectionDAO.insertUserConnection(userConnection);
    }

    @Override
    public boolean addInvitation(Invitation invitation) throws RemoteException {


        User sender = userDAO.getUserById(invitation.getSenderId());
        String senderPhone = sender.getPhoneNumber();
        String senderName = sender.getDisplayName();

        User reciever = userDAO.getUserById(invitation.getReceiverId());
        String email = reciever.getEmail();
        Thread t1 = new Thread(() -> JakartaMail.mailService(senderPhone, email, senderName));
        t1.start();

        return invitationDAO.addInvitation(invitation);
    }

    @Override
    public Invitation getInvitationBySenderAndReciever(int senderId, int receiverId) throws RemoteException {
        return invitationDAO.getInvitationBySenderAndReciever(senderId, receiverId);
    }

    @Override
    public String getCreatedGroupName(int groupId) throws RemoteException {
        String name = null;
        try {
            name = groupDAO.getCreatedGroup(groupId).getDisplayName();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    public void addChatbot(Chatbot chatbot) throws RemoteException {
        try {
            chatbotDAO.addChatbot(chatbot);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isChatbotEnabled(int userID) throws RemoteException {
        try {
            return chatbotDAO.isChatbotEnabled(userID);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void enableChatBot(int userID) throws RemoteException {
        try {
            chatbotDAO.enableChatBot(userID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disableChatBot(int userID) throws RemoteException {
        try {
            chatbotDAO.DisableChatBot(userID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addChatbotByUserID(int userID) throws RemoteException {
        chatbotDAO.addChatbotByUserID(userID);
    }

    @Override
    public Chatbot getChatbotById(int userID) throws RemoteException {
        try {
            return chatbotDAO.getChatbotByUserId(userID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseMessage> getGroupMessages(int groupId) throws RemoteException {
        List<BaseMessage> messages = new ArrayList<>();

        try {
            List<GroupMessage> groupMessages = groupMessageDAO.getGroupMessageByGroupId(groupId);
            messages.addAll(groupMessages);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<FileTransfer> fileTransfers = fileTransferDAO.getFilesByGroupId(groupId);
        messages.addAll(fileTransfers);

        messages.sort(Comparator.comparing(BaseMessage::getTimeStamp2));

        return messages;
    }

    @Override
    public List<ServerAnnouncement> getAllServerAnnouncements() {
        try {
            return serverAnnouncementDAO.getAllServerAnnouncements();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BaseMessage> getMessagesBetweenTwo(int receiverId, int senderId) throws RemoteException {
        List<BaseMessage> messages = new ArrayList<>();

        List<DirectMessage> directMessages = directMessageDAO.getMessagesBetweenTwo(receiverId, senderId);
        messages.addAll(directMessages);

        List<FileTransfer> fileTransfers = fileTransferDAO.getFilesBetweenUsers(senderId, receiverId);
        messages.addAll(fileTransfers);

        // Sort messages by timestamp (assuming getTimeStamp2() returns a Timestamp)
        messages.sort(Comparator.comparing(BaseMessage::getTimeStamp2));

        //        return directMessageDAO.getMessagesBetweenTwo(receiverId, senderId);
        System.out.println(messages);
        return messages;
    }

    @Override
    public boolean insertDirectMessage(DirectMessage directMessage) {
        try {
            directMessageDAO.insertDirectMessage(directMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    @Override
    public void addGroupMessage(GroupMessage message) throws RemoteException {

        try {
            groupMessageDAO.addGroupMessage(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserConnection getUserConnection(int userId, int connectedUserId) throws RemoteException {
        return userConnectionDAO.getUserConnection(userId, connectedUserId);
    }

    @Override
    public UserBlockedConnection getBlockedConnection(int blockerUserId, int blockedUserId) throws RemoteException {
        return userBlockedConnectionDAO.getBlockedConnection(blockerUserId, blockedUserId);
    }


    @Override
    public int createGroup(String groupName, int createdBy) {
        try {
            return groupDAO.createGroupWithId(groupName, createdBy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void addUserToGroup(int userId, int groupId) {
        try {
            userGroupsDAO.addUserToGroup(userId, groupId);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public List<Invitation> getAllInvitationsById(int userId) {
        return invitationDAO.getAllInvitationsByReceiverId(userId);
    }

    @Override
    public boolean addUserConnection(UserConnection userConnection) {
        return userConnectionDAO.insertUserConnection(userConnection);
    }

    @Override
    public void deleteInvitation(int invitationId) {
        invitationDAO.deleteInvitation(invitationId);
    }

    @Override
    public boolean isUserConnection(int userId, int connectedUserId) {
        UserConnection userConnection = userConnectionDAO.getUserConnection(userId, connectedUserId);
        if (userConnection == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void pushSound(String phoneNumber) {
        for (ClientInt client : OnlineClintsList) {
            try {
                if (client.getPhoneNumber().equals(phoneNumber)) {
                    try {
                        client.playNotificationSound();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void reload(String phoneNumber, BaseMessage message, String type, int ID) throws RemoteException {
        for (ClientInt client : OnlineClintsList) {
            try {
                if (client.getPhoneNumber().equals(phoneNumber)) {
                    client.refreshChatList(message, type, ID);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Integer> getUsersByGroupId(int groupId) throws RemoteException {
        try {
            return userGroupsDAO.getUsersByGroupId(groupId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void reloadInvitationList(String phoneNumber) throws RemoteException {
        for (ClientInt client : OnlineClintsList) {
            try {
                if (client.getPhoneNumber().equals(phoneNumber)) {
                    client.refreshInvitationList();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void reloadContactList(String phoneNumber, Card c) throws RemoteException {

        for (ClientInt client : OnlineClintsList) {
            try {
                if (client.getPhoneNumber().equals(phoneNumber)) {
                    System.out.println("UserImpl , reloadContactList");
                    client.refreshContactList(c);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void reloadNotificationList(String phoneNumber) throws RemoteException {

        for (ClientInt client : OnlineClintsList) {
            try {
                if (client.getPhoneNumber().equals(phoneNumber)) {
                    client.refreshNotificationList();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String getSessionToken(String phoneNumber) throws RemoteException {
        String token = UUID.randomUUID().toString();
        sessionTokens.put(phoneNumber, token);
        return token;
    }

    @Override
    public boolean validateToken(String phoneNumber, String token) throws RemoteException {
        return sessionTokens.containsKey(phoneNumber) && sessionTokens.get(phoneNumber).equals(token);
    }

    @Override
    public List<Invitation> getAllAcceptedInvitationsBySenderId(int senderId) {
        return invitationDAO.getAllAcceptedInvitationsBySenderId(senderId);
    }

    @Override
    public List<Invitation> getAllPendingInvitationsByReceiverId(int receiverId) {
        return invitationDAO.getAllPendingInvitationsByReceiverId(receiverId);
    }

    @Override
    public void updateInvitationStatusById(int invitationId, Invitation.Status newStatus) {
        invitationDAO.updateInvitationStatusById(invitationId, newStatus);
    }

    @Override
    public UUID uploadFile(int senderId, Integer receiverId, Integer groupId,
                           String fileName, String fileType, byte[] fileData) throws RemoteException {
        // Generate a new file ID and create a unique stored file name
        UUID fileId = UUID.randomUUID();
        String fileExtension = getFileExtension(fileName);
        String storedFileName = fileId.toString() + fileExtension;
        java.nio.file.Path filePath = storagePath.resolve(storedFileName);

        // Save file to disk
        try {
            Files.write(filePath, fileData);
        } catch (IOException e) {
            throw new RemoteException("File write failed", e);
        }

        // Insert file metadata into the database using try-with-resources
        String sql = "INSERT INTO file_transfer (file_id, sender_id, receiver_id, group_id, file_name, file_type, file_path, timestamp) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, fileId.toString());
            ps.setInt(2, senderId);
            if (receiverId != null) {
                ps.setInt(3, receiverId);
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            if (groupId != null) {
                ps.setInt(4, groupId);
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setString(5, fileName);
            ps.setString(6, fileType);
            ps.setString(7, storedFileName);
            ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Database insert failed", e);
        }

        return fileId;
    }

    @Override
    public byte[] downloadFile(UUID fileId, int requesterId) throws RemoteException {
        FileTransfer transfer = null;
        String sql = "SELECT sender_id, receiver_id, group_id, file_path FROM file_transfer WHERE file_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, fileId.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int senderId = rs.getInt("sender_id");
                    int receiverId = rs.getInt("receiver_id");
                    // Use getObject to properly check for NULL in groupId
                    Integer groupId = rs.getObject("group_id") != null ? rs.getInt("group_id") : null;
                    String storedFileName = rs.getString("file_path");


                    // Create a FileTransfer object. (The original file name and file type are not needed for download.)
                    transfer = new FileTransfer(fileId, senderId, receiverId, groupId, null, null, storedFileName,
                            new Timestamp(System.currentTimeMillis()));
                } else {
                    throw new RemoteException("File not found");
                }
            }
        } catch (SQLException e) {
            throw new RemoteException("Database retrieval failed", e);
        }

        // Validate permissions: if the file belongs to a group, ensure the requester is a member;
        // otherwise, check that the requester is either the sender or receiver.
        if (transfer.getGroupId() != null) {
            try {
                if (!userGroupsDAO.isUserInGroup(requesterId, transfer.getGroupId())) {
                    throw new RemoteException("User not in group");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (requesterId != transfer.getSenderId() && requesterId != transfer.getReceiverId()) {
                throw new RemoteException("Unauthorized access");
            }
        }

        // Read the file from disk and return its bytes
        Path filePath = storagePath.resolve(transfer.getFilePath());
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RemoteException("Download failed: " + e.getMessage(), e);
        }
    }

    @Override
    public String getFileName(UUID fileId) throws RemoteException {
        return fileTransferDAO.getFileName(fileId);

    }


    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }

    @Override
    public List<ServerAnnouncement> getAllServerAnnouncementsBasedOnCreatedTime(int userID) throws RemoteException {
        try {
            return serverAnnouncementDAO.getAllServerAnnouncementsBasedOnCreatedTime(userID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void reloadContactListwithAnnouncement() throws RemoteException {

    }


}
