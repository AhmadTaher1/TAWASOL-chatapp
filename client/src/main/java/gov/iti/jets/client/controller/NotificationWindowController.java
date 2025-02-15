package gov.iti.jets.client.controller;

import gov.iti.jets.client.ClientMain;
import gov.iti.jets.client.model.ClientImpl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.paint.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;

import javafx.scene.image.Image;
import javafx.geometry.Insets;
import shared.dto.*;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;


public class NotificationWindowController {

    private UserInt userInt;
    private AdminInt adminInt;
    private User currentUser = null;
    ClientImpl c;
    Registry registry = null;
    List<Invitation> invitationsList = null;

    private HomeScreenController homeScreenController;

    public void setHomeScreenController(HomeScreenController homeScreenController) {
        this.homeScreenController = homeScreenController;
    }

    public void setCurrentUser(User currentUser) {

        this.currentUser = currentUser;
    }

    public void setUserInt(UserInt userInt) {
        this.userInt = userInt;
    }

    public void setAdminInt(AdminInt adminInt) {
        this.adminInt = adminInt;
    }

    @FXML
    public ScrollPane pane;

    @FXML
    private VBox vBox;


    @FXML
    public void initialize() {

        Platform.runLater(() -> {

            // Initialize any necessary data or settings here
            System.out.println("initialized!");
            pane.getStylesheets().add(getClass().getResource("/cssStyles/InvitationList.css").toExternalForm());

            vBox.setSpacing(2);


            try {
                registry = LocateRegistry.getRegistry("localhost", 8554);
                userInt = (UserInt) registry.lookup("UserServices");
                if (userInt == null) {
                    System.out.println("null");
                }
                try {

                    invitationsList = userInt.getAllAcceptedInvitationsBySenderId(currentUser.getUserId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            Collections.reverse(invitationsList);


            for (Invitation invitation : invitationsList) {
                try {
                    User user = userInt.getUserById(invitation.getSenderId());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NotificationCard.fxml"));
                    Parent card = loader.load();

                    NotificationCardController notificationCardController = loader.getController();
                    notificationCardController.setAdminInt(ClientMain.adminInt);
                    notificationCardController.setUserInt(ClientMain.userInt);
                    notificationCardController.setCurrentUser(currentUser);
                    notificationCardController.setNotificationData(invitation);
                    // notificationCardController.setNotificationController(this);
                    notificationCardController.setHomeScreenController(homeScreenController);
                    System.out.println("hello");
                    vBox.getChildren().add(card);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        });

    }


    public void updateUI(HBox clearedHbox) {
        Platform.runLater(() -> {
            vBox.getChildren().remove(clearedHbox);

        });


    }

}
