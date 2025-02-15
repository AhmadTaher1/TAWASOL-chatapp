package shared.interfaces;

import shared.dto.Admin;
import shared.dto.ServerAnnouncement;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AdminInt extends Remote {
    boolean Register(Admin admin) throws RemoteException;

    boolean Login(String userName, String password) throws RemoteException;

    int getNumberOfUsersBasedOnCountry(String country) throws RemoteException;

    int getNumberOfUsersBasedOnGender(String gender) throws RemoteException;

    int getNumberOfUsersBasedOnStat(String stat) throws RemoteException;

    void sendAnnouncement(ServerAnnouncement announcement) throws RemoteException;

    void turnOnServer() throws RemoteException;

    void turnOffServer() throws RemoteException;

    boolean getServerStatus() throws RemoteException;
}
