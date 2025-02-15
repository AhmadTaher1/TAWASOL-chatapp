package gov.iti.jets.client.controller;

import java.io.File;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import gov.iti.jets.client.ClientMain;
import gov.iti.jets.client.model.ClientImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import shared.dto.*;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;

public class InvitationListCardController {

    private UserInt userInt;
    private AdminInt adminInt;
    private User currentUser = null;
    ClientImpl c;
    Registry registry = null;

    private InvitationListWindowController invitationListWindowController;
    private HomeScreenController homeScreenController;

    public void setHomeScreenController(HomeScreenController homeScreenController) {
        this.homeScreenController = homeScreenController;
    }


    public void setInvitationListController(InvitationListWindowController invitationListController) {
        this.invitationListWindowController = invitationListController;
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
    private ImageView frinedImage;

    @FXML
    private Circle personalImg;

    @FXML
    private Text friendNameText;

    @FXML
    private Circle acceptIcon;

    @FXML
    private Circle deleteIcon;

    @FXML
    private HBox hbox;

    User cardUser;
    Invitation cardInvitation;

    public void setInvitationData(Invitation invitation) {

        try {
            User user = userInt.getUserById(invitation.getSenderId());
            this.cardInvitation = invitation;
            this.cardUser = user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String name = cardUser.getDisplayName();
        String imgPath = cardUser.getProfilePicturePath();

        friendNameText.setText(name);

        if (imgPath != null && !imgPath.isEmpty()) {
            try {
                Image profileImage;

                if (Paths.get(imgPath).isAbsolute()) {
                    File file = new File(imgPath);
                    if (file.exists() && file.canRead()) {
                        profileImage = new Image(file.toURI().toString());
                    } else {
                        System.out.println("Error: File does not exist or cannot be read.");
                        return;
                    }
                } else {
                    profileImage = new Image(getClass().getResource(imgPath).toExternalForm());
                }

                personalImg.setFill(new ImagePattern(profileImage));

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error loading profile image: " + e.getMessage());
            }
        }

    }

    @FXML
    public void initialize() {

        acceptIcon.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/img/accept.png"))));
        deleteIcon.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/img/delete.png"))));

        try {
            registry = LocateRegistry.getRegistry("localhost", 8554);
            userInt = (UserInt) registry.lookup("UserServices");
            if (userInt == null) {
                System.out.println("null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleAcceptIcon() {

        try {
            if (adminInt.getServerStatus() == true) {

                System.out.println("accept");

                try {
                    // Get the current date and time
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedNow = now.format(formatter);

                    // userInt.deleteInvitation(cardInvitation.getInvitationId());
                    userInt.updateInvitationStatusById(cardInvitation.getInvitationId(), Invitation.Status.accepted);
                    UserConnection userConnection1 = new UserConnection(currentUser.getUserId(), cardUser.getUserId(),
                            "friend");
                    UserConnection userConnection2 = new UserConnection(cardUser.getUserId(), currentUser.getUserId(),
                            "friend");

                    Invitation invitation = userInt.getInvitationBySenderAndReciever(currentUser.getUserId(),
                            cardUser.getUserId());
                    if (invitation != null) {
                        // delete this invitation
                        userInt.updateInvitationStatusById(invitation.getInvitationId(), Invitation.Status.accepted);
                        /// userInt.deleteInvitation(invitation.getInvitationId());

                    }
                    userInt.addUserConnection(userConnection1);
                    userInt.addUserConnection(userConnection2);

                    //1
                    Card card = new Card(cardUser.getUserId(), "user", cardUser.getDisplayName(), "", Timestamp.valueOf(formattedNow), cardUser.getStatus(), cardUser.getProfilePicturePath());
                    System.out.println(cardUser.getUserId() + "      " + currentUser.getUserId());
                    homeScreenController.addCardtoListView(card, cardUser.getPhoneNumber());

                    //5
                    Card card1 = new Card(currentUser.getUserId(), "user", currentUser.getDisplayName(), "", Timestamp.valueOf(formattedNow), currentUser.getStatus(), currentUser.getProfilePicturePath());
                    homeScreenController.addCardtoListView(card1, cardUser.getPhoneNumber());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (invitationListWindowController != null) {
                    invitationListWindowController.setCurrentUser(currentUser);
                    invitationListWindowController.updateUI(hbox);
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
                Stage addContactWindowStage = (Stage) acceptIcon.getScene().getWindow();

                addContactWindowStage.close();

                // Set the scene with the admin login page
                Scene scene = new Scene(root);
                stage.setScene(scene);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleDeleteIcon() {

        try {
            if (adminInt.getServerStatus() == true) {
                try {

                    userInt.deleteInvitation(cardInvitation.getInvitationId());
                    System.out.println("deleted");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (invitationListWindowController != null) {
                    invitationListWindowController.setCurrentUser(currentUser);
                    invitationListWindowController.updateUI(hbox);
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
                Stage addContactWindowStage = (Stage) acceptIcon.getScene().getWindow();

                addContactWindowStage.close();

                // Set the scene with the admin login page
                Scene scene = new Scene(root);
                stage.setScene(scene);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
