package gov.iti.jets.client.controller;

import gov.iti.jets.client.ClientMain;
import gov.iti.jets.client.model.ClientImpl;
import shared.utils.SecureStorage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import shared.dto.User;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;

import java.rmi.RemoteException;

enum Window {
    HOME_PAGE,
    LOGIN_PAGE,
    SIGNUP_PAGE
}

public class UserLoginController {


    private UserInt userInt;
    private AdminInt adminInt;
    private User currentUser = null;
    ClientImpl c;

    public void setUserInt(UserInt userInt) {
        this.userInt = userInt;
    }

    public void setAdminInt(AdminInt adminInt) {
        this.adminInt = adminInt;
    }


    @FXML
    private Label signupLabel;

    @FXML
    private Button loginAsAdminButton;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> handleLogInButton());
        signupLabel.setOnMouseClicked(event -> navigateToSignup());
        loginAsAdminButton.setOnAction(event -> navigateToAdminLogin());
        c = ClientImpl.getInstance();
        c.setUserLoginController(this);

        phoneNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {

            // Skip validation if user is auto-logging in
            if (newValue.isEmpty()) {
                return;
            }


            if (!newValue.matches("\\d*")) {
                showAlert("Invalid Input", "Phone number must contain only digits.");
                phoneNumberTextField.setText(oldValue);
            } else if (newValue.length() > 11) {
                showAlert("Invalid Input", "Phone number must be exactly 11 digits.");
                phoneNumberTextField.setText(oldValue);
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleLogInButton() {
        try {
            if (adminInt.getServerStatus() == true) {

                String phoneNumber = phoneNumberTextField.getText();
                String password = passwordTextField.getText();

                String savedPhoneNumber = SecureStorage.getPhoneNumber();
                String savedToken = SecureStorage.getToken();

                System.out.println(savedPhoneNumber + "" + savedToken + "this is");
                boolean isValid = false;
                try {
                    isValid = userInt.validateToken(savedPhoneNumber, savedToken);

                    System.out.println("is valid" + isValid);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    currentUser = userInt.isValidUser(phoneNumber, password);

                    if (currentUser == null && isValid == false) {
                        SecureStorage.clearCredentials();
                        showErrorAlert("Invalid User", "Phone number or password are not correct");
                        return;
                    } else {
                        if (currentUser == null) {
                            currentUser = userInt.getUserByPhoneNumber(savedPhoneNumber);
                        }
                        String token = userInt.getSessionToken(currentUser.getPhoneNumber()); // Get session token from server
                        SecureStorage.saveCredentials(currentUser.getPhoneNumber(), token); // Save locally
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }


                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/homeScreen.fxml"));
                    Parent homeRoot = loader.load();
                    HomeScreenController homeScreenController = loader.getController();
                    homeScreenController.setUserInt(ClientMain.userInt);
                    homeScreenController.setAdminInt(ClientMain.adminInt);
                    System.out.println("Current User");
                    homeScreenController.setCurrentUser(currentUser);


                    // Get the current stage
                    Stage stage = (Stage) signupLabel.getScene().getWindow();
                    double width = stage.getWidth();
                    double height = stage.getHeight();

                    // Set the scene with the admin login page
                    Scene scene = new Scene(homeRoot);
                    stage.setScene(scene);
                    stage.setWidth(width);
                    stage.setHeight(height);


                } catch (Exception e) {
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
                serverUnavailableController.setNavigatedWindow(gov.iti.jets.client.controller.ServerUnavailableController.Window.LOGIN_PAGE);

                Stage stage = this.getStage();

                // Set the scene with the admin login page
                Scene scene = new Scene(root);
                stage.setScene(scene);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void navigateToSignup() {
        try {
            // Load the UserSignupPage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserSignupPage.fxml"));
            Parent signupRoot = loader.load();
            UserSignupController userSignupController = loader.getController();
            userSignupController.setUserInt(ClientMain.userInt);
            userSignupController.setAdminInt(ClientMain.adminInt);


            // Get the current stage
            Stage stage = (Stage) signupLabel.getScene().getWindow();

            double width = stage.getWidth();
            double height = stage.getHeight();

            // Set the scene with the admin login page
            Scene scene = new Scene(signupRoot);
            stage.setScene(scene);
            stage.setWidth(width);
            stage.setHeight(height);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void navigateToAdminLogin() {
        try {
            // Load the AdminLoginPage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminLoginPage.fxml"));
            Parent adminLoginRoot = loader.load();
            AdminLoginController adminLoginController = loader.getController();
            adminLoginController.setUserInt(ClientMain.userInt);
            adminLoginController.setAdminInt(ClientMain.adminInt);

            // Get the current stage
            Stage stage = (Stage) loginAsAdminButton.getScene().getWindow();


            double width = stage.getWidth();
            double height = stage.getHeight();

            // Set the scene with the admin login page
            Scene scene = new Scene(adminLoginRoot);
            stage.setScene(scene);
            stage.setWidth(width);
            stage.setHeight(height);


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

    // Method to get the Stage
    public Stage getStage() {
        return (Stage) loginButton.getScene().getWindow();
    }
}
