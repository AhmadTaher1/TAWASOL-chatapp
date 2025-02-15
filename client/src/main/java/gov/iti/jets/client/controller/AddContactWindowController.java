package gov.iti.jets.client.controller;

import gov.iti.jets.client.ClientMain;
import gov.iti.jets.client.model.ClientImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.*;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import shared.dto.Invitation;
import shared.dto.User;
import shared.dto.UserConnection;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;


import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;


public class AddContactWindowController {

    private UserInt userInt;
    private AdminInt adminInt;
    private User currentUser = null;
    ClientImpl c;

    private HomeScreenController homeScreenController;

    public void setHomeScreenController(HomeScreenController homeScreenController) {
        this.homeScreenController = homeScreenController;
    }

    public void setCurrentUser(User currentUser) {
        System.out.println(currentUser);
        this.currentUser = currentUser;
    }


    public void setUserInt(UserInt userInt) {
        this.userInt = userInt;
    }

    public void setAdminInt(AdminInt adminInt) {
        this.adminInt = adminInt;
    }

    @FXML
    private TextField numberTextField;


    @FXML
    public void initialize() {
        c = ClientImpl.getInstance();
        c.setAddContactWindowController(this);

    }


    @FXML
    public void handleAddContactButton(ActionEvent event) throws IOException {
        try {
            if (adminInt.getServerStatus() == true) {

                UserConnection userConnection = null;
                Invitation invitation = null;
                User user = userInt.getUserByPhoneNumber(numberTextField.getText());
                boolean isUserConnection = false;

                if (user != null) {
                    invitation = userInt.getInvitationBySenderAndReciever(HomeScreenController.currentUser.getUserId(), user.getUserId());
                    userConnection = userInt.getUserConnection(HomeScreenController.currentUser.getUserId(), user.getUserId());

                    userInt.isUserConnection(HomeScreenController.currentUser.getUserId(), user.getUserId());
                }

//         if(user!=null && user.getUserId()!=HomeScreenController.currentUser.getUserId() && (invitation==null||invitation.getStatus()!=Invitation.Status.PENDING) && userConnection==null )
                if (user != null && user.getUserId() != HomeScreenController.currentUser.getUserId() && invitation == null && !isUserConnection && userConnection == null) {

                    Invitation new_invitation = new Invitation();
                    new_invitation.setSenderId(HomeScreenController.currentUser.getUserId());
                    new_invitation.setReceiverId(user.getUserId());
                    new_invitation.setStatus(Invitation.Status.pending);

                    if (userInt.addInvitation(new_invitation)) {
                        showInfoMessage("Done!", "Invitation sent successfully");
                        userInt.pushSound(user.getPhoneNumber());
                    }

                } else {
                    if (user == null) {
                        showErrorAlert("Error", "User is not found");
                    } else if (user.getUserId() == HomeScreenController.currentUser.getUserId()) {
                        showErrorAlert("Error", " can't send invitation to yourself");
                    } else if (userConnection != null) {
                        showErrorAlert("Error", "User is already in your contacts");
                    } else if (invitation.getStatus() == Invitation.Status.pending) {
                        showErrorAlert("Error", "Invitation already sent");
                    } else {
                        showErrorAlert("Error", "Unknown error");
                    }

                }

            } else {
                System.out.println("server is off");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ServerUnavailable.fxml"));

                Parent root = loader.load();
                ServerUnavailableController serverUnavailableController = loader.getController();
                serverUnavailableController.setAdminInt(ClientMain.adminInt);
                serverUnavailableController.setUserInt(ClientMain.userInt);
                serverUnavailableController.setCurrentUser(currentUser);

                Stage stage = homeScreenController.getStage();
                Stage addContactWindowStage = (Stage) numberTextField.getScene().getWindow();


                addContactWindowStage.close();


                // Set the scene with the admin login page
                Scene scene = new Scene(root);
                stage.setScene(scene);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
