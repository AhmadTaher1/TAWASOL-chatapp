package gov.iti.jets.client.controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import gov.iti.jets.client.ClientMain;
import gov.iti.jets.client.model.ClientImpl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import shared.dto.SocialNetwork;
import shared.dto.User;
import shared.interfaces.AdminInt;
import shared.interfaces.ClientInt;
import shared.interfaces.UserInt;

import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

public class ServerUnavailableController {

    public static void main(String[] args) {

    }

    enum Window {
        HOME_PAGE,
        LOGIN_PAGE,
        SIGNUP_PAGE
    }

    private Window navigatedWindow = Window.HOME_PAGE; // Default to home page

    private UserInt userInt;
    private AdminInt adminInt;
    private User currentUser = null;
    Registry registry = null;
    ClientImpl c;

    public void setCurrentUser(User currentUser) {
        System.out.println("setting the current user in the server page");
        System.out.println(currentUser);
        this.currentUser = currentUser;
    }

    public void setUserInt(UserInt userInt) {
        this.userInt = userInt;
    }

    public void setAdminInt(AdminInt adminInt) {
        this.adminInt = adminInt;
    }

    public void setNavigatedWindow(Window window) {
        navigatedWindow = window;
    }

    @FXML
    private ImageView reconnetcButton;

    @FXML
    public void initialize() {

        try {

            registry = LocateRegistry.getRegistry("localhost", 8554);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void handleReconnctButton() {
        try {
            if (adminInt.getServerStatus() == true) {
                if (navigatedWindow == Window.HOME_PAGE) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/homeScreen.fxml"));
                    Parent homeRoot = loader.load();
                    HomeScreenController homeScreenController = loader.getController();
                    homeScreenController.setUserInt(ClientMain.userInt);
                    homeScreenController.setAdminInt(ClientMain.adminInt);
                    homeScreenController.setCurrentUser(currentUser);

                    // Get the current stage
                    Stage stage = (Stage) reconnetcButton.getScene().getWindow();
                    double width = stage.getWidth();
                    double height = stage.getHeight();

                    // Set the scene with the admin login page
                    Scene scene = new Scene(homeRoot);
                    scene.getStylesheets().add(getClass().getResource("/cssStyles/message.css").toExternalForm());
                    stage.setScene(scene);
                    stage.setWidth(width);
                    stage.setHeight(height);
                } else if (navigatedWindow == Window.SIGNUP_PAGE) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserSignupPage.fxml"));
                    Parent homeRoot = loader.load();
                    UserSignupController userSignupController = loader.getController();
                    userSignupController.setUserInt(ClientMain.userInt);
                    userSignupController.setAdminInt(ClientMain.adminInt);


                    // Get the current stage
                    Stage stage = (Stage) reconnetcButton.getScene().getWindow();
                    double width = stage.getWidth();
                    double height = stage.getHeight();

                    // Set the scene with the admin login page
                    Scene scene = new Scene(homeRoot);
                    scene.getStylesheets().add(getClass().getResource("/cssStyles/message.css").toExternalForm());
                    stage.setScene(scene);
                    stage.setWidth(width);
                    stage.setHeight(height);
                } else if (navigatedWindow == Window.LOGIN_PAGE) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserLoginPage.fxml"));
                    Parent homeRoot = loader.load();
                    UserLoginController userLoginController = loader.getController();
                    userLoginController.setUserInt(ClientMain.userInt);
                    userLoginController.setAdminInt(ClientMain.adminInt);


                    // Get the current stage
                    Stage stage = (Stage) reconnetcButton.getScene().getWindow();
                    double width = stage.getWidth();
                    double height = stage.getHeight();

                    // Set the scene with the admin login page
                    Scene scene = new Scene(homeRoot);
                    scene.getStylesheets().add(getClass().getResource("/cssStyles/message.css").toExternalForm());
                    stage.setScene(scene);
                    stage.setWidth(width);
                    stage.setHeight(height);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
