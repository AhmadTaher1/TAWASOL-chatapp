package gov.iti.jets.client.controller;

import gov.iti.jets.client.model.ClientImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import shared.dto.ServerAnnouncement;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.sql.Timestamp;

public class AnnouncementController {
    private UserInt userInt;
    private AdminInt adminInt;
    private Registry registry;

    private static int announcementId = 1;
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
    private BorderPane childBorderpanetwo;

    @FXML
    private TextArea txtarea;


    @FXML
    private Button sendBut;

    @FXML
    void sendmethod(ActionEvent event) {

        try {
            System.out.println(txtarea.getText());
            registry = LocateRegistry.getRegistry("localhost", 8554);
            adminInt = (AdminInt) registry.lookup("AdminServices");

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());


            ServerAnnouncement serverAnnouncement = new ServerAnnouncement(announcementId, txtarea.getText(), timestamp);
            adminInt.sendAnnouncement(serverAnnouncement);

        } catch (Exception e) {
            e.printStackTrace();
        }

        txtarea.clear();
    }

    @FXML
    void initialize() {


        c = ClientImpl.getInstance();
        c.setAnnouncementController(this);
        assert childBorderpanetwo != null : "fx:id=\"childBorderpane\" was not injected: check your FXML file 'hello-view -Announcement.fxml'.";
        assert txtarea != null : "fx:id=\"txtarea\" was not injected: check your FXML file 'hello-view -Announcement.fxml'.";
        assert sendBut != null : "fx:id=\"sendBut\" was not injected: check your FXML file 'hello-view -Announcement.fxml'.";

    }


}

