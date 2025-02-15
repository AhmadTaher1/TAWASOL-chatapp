package gov.iti.jets.client.controller;

import gov.iti.jets.client.ClientMain;
import gov.iti.jets.client.model.ClientImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

public class AdminLoginController {

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
    private TextField username; // Username field

    @FXML
    private PasswordField password; // Password field

    @FXML
    private Button loginButton; // Login button

    @FXML
    private Button loginAsUserButton; // Login as user button

    @FXML
    public void initialize() {

        try {

            reg = LocateRegistry.getRegistry("localhost", 8554);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        loginAsUserButton.setOnAction(event -> navigateToUserLogin());
        loginButton.setOnAction(event -> navigateToServerPage());
        c = ClientImpl.getInstance();
        c.setAdminLoginController(this);
    }


    private void navigateToUserLogin() {
        try {
            // Load the UserLoginPage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserLoginPage.fxml"));
            Parent userLoginRoot = loader.load();
            UserLoginController userLoginController = loader.getController();
            userLoginController.setUserInt(ClientMain.userInt);
            userLoginController.setAdminInt(ClientMain.adminInt);


            // Get the current stage
            Stage stage = (Stage) loginAsUserButton.getScene().getWindow();

            double width = stage.getWidth();
            double height = stage.getHeight();
            // Set the scene with the user login page
            Scene scene = new Scene(userLoginRoot);

            stage.setScene(scene);
            stage.setWidth(width);
            stage.setHeight(height);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToServerPage() {
        if (handleLogin()) {
            try {


                // Load the UserLoginPage.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hello-viewhomepage.fxml"));
                Parent serverPageRoot = loader.load();
                ServerHomePageController serverHomePageController = new ServerHomePageController();
                serverHomePageController.setAdminInt(ClientMain.adminInt);
                serverHomePageController.setUserInt(ClientMain.userInt);

                // Get the current stage
                Stage stage = (Stage) loginButton.getScene().getWindow();

                double width = stage.getWidth();
                double height = stage.getHeight();

                // Set the scene with the signup page
                Scene scene = new Scene(serverPageRoot);
                stage.setScene(scene);
                stage.setWidth(width);
                stage.setHeight(height);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            showErrorAlert("Login Error", "Incorrect username or password.");

        }

    }

    private boolean handleLogin() {

        try {
            //Look up the remote object
            adminInt = (AdminInt) reg.lookup("AdminServices");

            if (adminInt.Login(username.getText().trim(), password.getText())) {

                return true;
            } else {
                System.out.println("user or password are not correct");
                return false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper method to show an error alert
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}