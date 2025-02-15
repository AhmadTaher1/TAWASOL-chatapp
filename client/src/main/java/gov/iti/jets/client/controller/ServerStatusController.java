package gov.iti.jets.client.controller;

import gov.iti.jets.client.model.ClientImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;

import java.net.URL;
import java.util.ResourceBundle;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerStatusController implements Initializable {

    private UserInt userInt;
    private AdminInt adminInt;
    private Registry reg;
    ClientImpl c;

    public void setUserInt(UserInt userInt) {
        this.userInt = userInt;
    }

    public void setAdminInt(AdminInt adminInt) {
        this.adminInt = adminInt;
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane childBorderpanethree;

    @FXML
    private Button offbutton;

    @FXML
    private Button onbutton;

    @FXML
    void offmethod(ActionEvent event) {
        offbutton.setStyle("-fx-background-color: red; -fx-text-fill: black;");
        onbutton.setStyle("");

        try {
            adminInt.turnOffServer();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void onmethod(ActionEvent event) {
        onbutton.setStyle("-fx-background-color: #00FF00; -fx-text-fill: black;");
        offbutton.setStyle("");

        try {

            adminInt.turnOnServer();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // App.isOnPressed=true;
        // App.isOffPressed=false;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        boolean isServerOn = true;
        try {

            reg = LocateRegistry.getRegistry("localhost", 8554);
            adminInt = (AdminInt) reg.lookup("AdminServices");

            isServerOn = adminInt.getServerStatus();

        } catch (Exception e) {
            e.printStackTrace();
        }

        c = ClientImpl.getInstance();
        c.setServerStatusController(this);


        if (isServerOn) {
            onbutton.setStyle("-fx-background-color: #00FF00; -fx-text-fill: black;");
            offbutton.setStyle("");
        } else {
            offbutton.setStyle("-fx-background-color: red; -fx-text-fill: black;");
            onbutton.setStyle("");

        }
    }
}
