package gov.iti.jets.client.controller;

import gov.iti.jets.client.ClientMain;
import gov.iti.jets.client.model.ClientImpl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.*;

import java.io.*;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

//io

//stage
import javafx.stage.*;
import shared.dto.SocialNetwork;
import shared.dto.User;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class EditWindowController {

    private static final String USER_IMAGE_DIR = "/user_data/users/";

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

    public User getCurrentUser() {
        return currentUser;
    }

    public void setUserInt(UserInt userInt) {
        this.userInt = userInt;
    }

    public void setAdminInt(AdminInt adminInt) {
        this.adminInt = adminInt;
    }

    @FXML
    Pane pane;

    @FXML
    private Circle photoCircle;
    @FXML
    private ImageView cameraIcon;
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField bioTextField;

    @FXML
    private ComboBox<User.Status> statusComboBox;
    @FXML
    private Button applyButton;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (currentUser != null) {
                loadUserDetails();
            }
        });

        c = ClientImpl.getInstance();
        c.setEditWindowController(this);


        statusComboBox.getItems().addAll(
                User.Status.values());


        statusComboBox.setCellFactory(lv -> {
            return new ListCell<User.Status>() {
                @Override
                protected void updateItem(User.Status item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item.name());
                        setStyle("-fx-background-color: #4399FF;");
                    }
                }
            };
        });

    }

    private void loadUserDetails() {
        if (currentUser == null)
            return;

        nameTextField.setText(currentUser.getDisplayName());
        bioTextField.setText(currentUser.getBio());
        statusComboBox.setValue(currentUser.getStatus());


        String profilePicturePath = currentUser.getProfilePicturePath();
        if (profilePicturePath != null && !profilePicturePath.isEmpty()) {
            try {
                Image profileImage;

                // Check if the path is absolute
                if (Paths.get(profilePicturePath).isAbsolute()) {
                    File file = new File(profilePicturePath);
                    if (file.exists() && file.canRead()) {
                        profileImage = new Image(file.toURI().toString());
                    } else {
                        System.out.println("Error: File does not exist or cannot be read.");
                        return;
                    }
                } else {
                    profileImage = new Image(getClass().getResource(profilePicturePath).toExternalForm());
                }

                photoCircle.setFill(new ImagePattern(profileImage));

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error loading profile image: " + e.getMessage());
            }
        }

    }

    @FXML
    public void changePhotoEvent(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an Image");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        Stage stage = (Stage) applyButton.getScene().getWindow();

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            String newImagePath = saveProfileImage(selectedFile, currentUser.getUserId());
            if (newImagePath != null) {
                currentUser.setProfilePicturePath(newImagePath);
                Image newImage = new Image(new File(newImagePath).toURI().toString());
                photoCircle.setFill(new ImagePattern(newImage));
            }
        }
    }

    private String saveProfileImage(File sourceFile, int userId) {
        File userDir = new File(USER_IMAGE_DIR + "user_" + userId);
        if (!userDir.exists())
            userDir.mkdirs();

        File destFile = new File(userDir, "profile.jpg");
        try {
            Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return destFile.getAbsolutePath(); // Return relative path
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    public void handleApplyButton() {
        try {
            if (adminInt.getServerStatus() == true) {

                String newPicPath = currentUser.getProfilePicturePath();
                String newDisplayName = nameTextField.getText();
                String newBio = bioTextField.getText();
                User.Status newStatus = statusComboBox.getValue();

                boolean isUpdated = userInt.editUserShownInfo(currentUser.getUserId(), newDisplayName, newStatus,
                        newPicPath, newBio);

                if (!isUpdated) {
                    System.out.println("Failed to update user information.");
                    return;
                }

                currentUser.setDisplayName(newDisplayName);
                currentUser.setStatus(newStatus);
                currentUser.setBio(newBio);

                if (homeScreenController != null) {
                    homeScreenController.setCurrentUser(currentUser);
                    homeScreenController.updateUI();
                }

                Stage stage = (Stage) applyButton.getScene().getWindow();
                stage.close();

            } else {
                System.out.println("server is off");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ServerUnavailable.fxml"));

                Parent root = loader.load();
                ServerUnavailableController serverUnavailableController = loader.getController();
                serverUnavailableController.setAdminInt(ClientMain.adminInt);
                serverUnavailableController.setUserInt(ClientMain.userInt);
                serverUnavailableController.setCurrentUser(currentUser);

                Stage stage = homeScreenController.getStage();
                Stage editWindowStage = (Stage) applyButton.getScene().getWindow();


                editWindowStage.close();


                // Set the scene with the admin login page
                Scene scene = new Scene(root);
                stage.setScene(scene);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
