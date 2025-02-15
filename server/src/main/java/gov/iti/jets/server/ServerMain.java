package gov.iti.jets.server;

import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {

    public ServerMain() {
        try {
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");

            UserInt userInt = new UserImpl();
            AdminInt adminInt = new AdminImpl();
            Registry registry = LocateRegistry.createRegistry(8554, null, null);
            registry.rebind("UserServices", userInt);
            registry.rebind("AdminServices", adminInt);
            System.out.println("server is running .....");
            while (true) ;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new ServerMain();
    }
}
