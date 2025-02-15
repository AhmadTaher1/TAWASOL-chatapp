package gov.iti.jets.client.controller;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import gov.iti.jets.client.ClientMain;
import gov.iti.jets.client.model.ClientImpl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shared.dto.User;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;


public class ChatbotWindowController {

    private UserInt userInt;
    private AdminInt adminInt;
    private User currentUser = null;
    ClientImpl c;
    Stage stage;

    private HomeScreenController homeScreenController;

    public void setHomeScreenController(HomeScreenController homeScreenController) {
        this.homeScreenController = homeScreenController;
    }

    public void setCurrentUser(User currentUser) {
        // System.out.println("setting the current user in the createGroup page");
        // System.out.println(currentUser);
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
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ScrollPane pane;

    @FXML
    private VBox vBox;

    @FXML
    private HBox hBox;

    @FXML
    private ToggleButton chatbotEnableButton;

    @FXML
    void chatbotEnableMethod(ActionEvent event) {

        try {
            if (adminInt.getServerStatus() == true) {

                try {
                    if (chatbotEnableButton.isSelected()) {
                        userInt.enableChatBot(HomeScreenController.currentUser.getUserId());
                        chatbotEnableButton.setStyle("-fx-background-color: #00FF00; -fx-text-fill: black;");
                    } else {
                        userInt.disableChatBot(HomeScreenController.currentUser.getUserId());
                        chatbotEnableButton.setStyle("-fx-background-color: red; -fx-text-fill: black;");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
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
                Stage createGroupWindowStage = (Stage) chatbotEnableButton.getScene().getWindow();


                createGroupWindowStage.close();


                // Set the scene with the admin login page
                Scene scene = new Scene(root);
                stage.setScene(scene);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @FXML
    private void initialize() {

        Platform.runLater(() -> {
            try {
                if (userInt.isChatbotEnabled(HomeScreenController.currentUser.getUserId())) {
                    chatbotEnableButton.setSelected(true);
                    chatbotEnableButton.setStyle("-fx-background-color: #00FF00; -fx-text-fill: black;");
                } else {
                    chatbotEnableButton.setSelected(false);
                    chatbotEnableButton.setStyle("-fx-background-color: red; -fx-text-fill: black;");


                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

    }


}
