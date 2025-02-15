package gov.iti.jets.client.controller;

import gov.iti.jets.client.ClientMain;
import gov.iti.jets.client.model.ClientImpl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import shared.dto.Card;
import shared.dto.User;
import shared.dto.UserConnection;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CreateGroupController {

    private UserInt userInt;
    private AdminInt adminInt;
    private User currentUser = null;
    ClientImpl c;
    Registry registry = null;

    private HomeScreenController homeScreenController;

    public void setHomeScreenController(HomeScreenController homeScreenController) {
        this.homeScreenController = homeScreenController;
    }


    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setUserInt(UserInt userInt) {
        this.userInt = userInt;
    }

    public void setAdminInt(AdminInt adminInt) {
        this.adminInt = adminInt;
    }

    // FXML components
    @FXML
    private VBox contactsContainer;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button createButton; // Button to create the group

    private List<Integer> selectedContacts = new ArrayList<>();

    List<UserConnection> userConnections;

    // Initialize method, called after the FXML elements have been injected
    @FXML
    public void initialize() {

        Platform.runLater(() -> {

            try {
                registry = LocateRegistry.getRegistry("localhost", 8554);
                userInt = (UserInt) registry.lookup("UserServices");
                if (userInt == null) {
                    System.out.println("null");
                }
                userConnections = userInt.getUserConncectionById(currentUser.getUserId());

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }

            for (UserConnection u : userConnections) {
                try {
                    int id = u.getConnectedUserId();

                    User user = userInt.getUserById(id);
                    System.out.println(user.getDisplayName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (UserConnection userFriend : userConnections) {

                try {

                    int friendId = userFriend.getConnectedUserId();

                    User user = userInt.getUserById(friendId);
                    String contact = user.getDisplayName();
                    String imgPath = user.getProfilePicturePath();
                    System.out.println(imgPath);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CreateGroupCard.fxml"));
                    Parent card = loader.load();
                    CreateGroupCardController controller = loader.getController();
                    controller.setContactData(contact, imgPath);

                    // Add a checkbox to select the contact
                    CheckBox checkBox = new CheckBox();
                    checkBox.setOnAction(event -> {
                        if (checkBox.isSelected()) {
                            selectedContacts.add(friendId);
                        } else {
                            selectedContacts.remove(friendId);
                        }
                    });

                    HBox cardWithCheckbox = new HBox(checkBox, card);
                    cardWithCheckbox.setSpacing(5);
                    cardWithCheckbox.setAlignment(Pos.CENTER_LEFT); // Align items to the left
                    contactsContainer.getChildren().add(cardWithCheckbox);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

        c = ClientImpl.getInstance();
        c.setCreateGroupController(this);

    }

    @FXML
    private void handleCreateButton() {
        //set timestamp of card
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);


        // Implement logic for creating the group

        try {
            if (adminInt.getServerStatus() == true) {
                if (validContacts()) {

                    if (validGroupName()) {
                        System.out.println("Create button clicked");
                        System.out.println("Creating group with selected contacts: " + selectedContacts);

                        System.out.println("group name: " + nameTextField.getText().trim());
                        try {
                            // get id of the current user
                            int groupId = userInt.createGroup(nameTextField.getText().trim(), currentUser.getUserId());
                            userInt.addUserToGroup(currentUser.getUserId(), groupId);
                            System.out.println("group id" + groupId);
                            // add contactId to the group
                            for (Integer i : selectedContacts) {
                                userInt.addUserToGroup(i, groupId);
                            }

                            Card card = new Card(groupId, "group", nameTextField.getText().trim(), "", Timestamp.valueOf(formattedNow), User.Status.AVAILABLE, "/img/people.png");
                            homeScreenController.addCardtoListView(card, currentUser.getPhoneNumber());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Stage stage = (Stage) createButton.getScene().getWindow();
                        stage.close();
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
                Stage createGroupWindowStage = (Stage) createButton.getScene().getWindow();


                createGroupWindowStage.close();


                // Set the scene with the admin login page
                Scene scene = new Scene(root);
                stage.setScene(scene);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean validGroupName() {
        if (nameTextField.getText().trim().length() == 0 || nameTextField.getText().trim().length() > 15) {
            showErrorAlert("Invalid Group Name", "Please enter a valid group name");
            return false;
        }
        return true;
    }

    private boolean validContacts() {
        if (selectedContacts.size() < 2) {
            showErrorAlert("No Contacts Selected", "Please select at least two contact to create a group.");
            return false;
        }
        return true;
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}