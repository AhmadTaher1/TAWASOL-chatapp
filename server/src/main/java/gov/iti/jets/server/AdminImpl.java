package gov.iti.jets.server;


import gov.iti.jets.server.model.dao.implementations.*;
import shared.dto.Admin;
import shared.dto.ServerAnnouncement;
import shared.interfaces.AdminInt;
import shared.utils.DB_UtilityClass;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.SQLException;

public class AdminImpl extends UnicastRemoteObject implements AdminInt {


    public static boolean isServerAvailabe = true;
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
    private final UserDAOImpl userDAO;
    private final UserGroupsDAOImpl userGroupsDAO;
    private Connection connection = DB_UtilityClass.getConnection();

    public AdminImpl() throws RemoteException {


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
    }


    @Override
    public boolean Register(Admin admin) throws RemoteException {
        try {
            if (!adminDAO.validateAdminCredentials(admin.getUserName(), admin.getPasswordHash())) {
                adminDAO.addAdmin(admin);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean Login(String userName, String password) throws RemoteException {
        try {
            if (adminDAO.validateAdminCredentials(userName, password)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getNumberOfUsersBasedOnCountry(String country) throws RemoteException {
        return userDAO.countCertainCountryUsers(country);
    }

    @Override
    public int getNumberOfUsersBasedOnGender(String gender) throws RemoteException {
        return userDAO.countCertainGenderUsers(gender);
    }

    @Override
    public int getNumberOfUsersBasedOnStat(String stat) throws RemoteException {


        int onlineusers = UserImpl.getOnlineUsers();

        if (stat.equals("online")) {
            return onlineusers;
        } else {
            return userDAO.countAllUsers() - onlineusers;
        }


    }

    @Override
    public void sendAnnouncement(ServerAnnouncement announcement) throws RemoteException {

        try {
            serverAnnouncementDAO.addServerAnnouncement(announcement);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void turnOnServer() {
        System.out.println("server is turned on");
        isServerAvailabe = true;
    }

    @Override
    public void turnOffServer() {
        System.out.println("server is turned off");
        isServerAvailabe = false;
    }

    @Override
    public boolean getServerStatus() {
        AdminImpl a = null;
        try {
            a = new AdminImpl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return a.isServerAvailabe;
    }


}




