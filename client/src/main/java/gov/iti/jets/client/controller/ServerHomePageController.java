package gov.iti.jets.client.controller;

import gov.iti.jets.client.ClientMain;
import gov.iti.jets.client.model.ClientImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;


public class ServerHomePageController implements Initializable {

    private UserInt userInt;
    private AdminInt adminInt;
    ClientImpl c;

    public void setUserInt(UserInt userInt) {
        this.userInt = userInt;
    }

    public void setAdminInt(AdminInt adminInt) {
        this.adminInt = adminInt;
    }


    @FXML
    private BorderPane mainBorderpane;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button statbut;

    @FXML
    private Button serverstatbut;

    @FXML
    private Button announcebut;

    @FXML
    private Button outbut;

    @FXML
    private Button registerAdminButton;

    @FXML
    void announcemethod(ActionEvent event) {

        statbut.setStyle("-fx-background-color: #67BCFEF5;");
        announcebut.setStyle("-fx-background-color: #479AC9;");
        serverstatbut.setStyle("-fx-background-color: #67BCFEF5;");
        outbut.setStyle("-fx-background-color: #67BCFEF5;");
        registerAdminButton.setStyle("-fx-background-color: #67BCFEF5;");
        BorderPane borderPane = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hello-view -Announcement.fxml"));
            borderPane = loader.load();
            AnnouncementController announcementController = loader.getController();
            announcementController.setAdminInt(ClientMain.adminInt);
            announcementController.setUserInt(ClientMain.userInt);

        } catch (IOException e) {
            System.out.println("failed to load it");
        }
        mainBorderpane.setCenter(borderPane);

    }

    @FXML
    void outmethod(ActionEvent event) {
        statbut.setStyle("-fx-background-color: #67BCFEF5;");
        announcebut.setStyle("-fx-background-color: #67BCFEF5;");
        serverstatbut.setStyle("-fx-background-color: #67BCFEF5;");
        outbut.setStyle("-fx-background-color: #479AC9;");
        registerAdminButton.setStyle("-fx-background-color: #67BCFEF5;");

        try {
            // Load the UserSignupPage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminLoginPage.fxml"));
            Parent logRoot = loader.load();


            // Get the current stage
            Stage stage = (Stage) outbut.getScene().getWindow();

            double width = stage.getWidth();
            double height = stage.getHeight();

            // Set the scene with the signup page
            Scene scene = new Scene(logRoot);
            stage.setScene(scene);
            stage.setWidth(width);
            stage.setHeight(height);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void serverstatmethod(ActionEvent event) {

        statbut.setStyle("-fx-background-color: #67BCFEF5;");
        announcebut.setStyle("-fx-background-color: #67BCFEF5;");
        serverstatbut.setStyle("-fx-background-color: #479AC9;");
        outbut.setStyle("-fx-background-color: #67BCFEF5;");
        registerAdminButton.setStyle("-fx-background-color: #67BCFEF5;");
        BorderPane borderPane = null;
        try {
            borderPane = FXMLLoader.load(getClass().getResource("/fxml/hello-view -serverstatus.fxml"));
        } catch (IOException e) {
            System.out.println("failed to load it");
        }
        mainBorderpane.setCenter(borderPane);


    }

    @FXML
    void statmethods(ActionEvent event) {
        statbut.setStyle("-fx-background-color: #479AC9;");
        announcebut.setStyle("-fx-background-color: #67BCFEF5;");
        serverstatbut.setStyle("-fx-background-color: #67BCFEF5;");
        outbut.setStyle("-fx-background-color: #67BCFEF5;");
        registerAdminButton.setStyle("-fx-background-color: #67BCFEF5;");
        BorderPane borderPane = null;
        try {
            borderPane = FXMLLoader.load(getClass().getResource("/fxml/hello-view - generalStatistics.fxml"));
        } catch (IOException e) {
            System.out.println("failed to load it");
        }
        mainBorderpane.setCenter(borderPane);

    }

    @FXML
    void registerAdminMethod(ActionEvent event) {
        statbut.setStyle("-fx-background-color: #67BCFEF5;");
        announcebut.setStyle("-fx-background-color: #67BCFEF5;");
        serverstatbut.setStyle("-fx-background-color: #67BCFEF5;");
        outbut.setStyle("-fx-background-color: #67BCFEF5;");
        registerAdminButton.setStyle("-fx-background-color: #479AC9;");

        BorderPane borderPane = null;
        try {

            borderPane = FXMLLoader.load(getClass().getResource("/fxml/AdminSignupPage.fxml"));
            AdminSignupController adminSignupController = new AdminSignupController();
            adminSignupController.setAdminInt(ClientMain.adminInt);
            adminSignupController.setUserInt(ClientMain.userInt);

        } catch (IOException e) {
            System.out.println("failed to load it");
        }
        mainBorderpane.setCenter(borderPane);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        c = ClientImpl.getInstance();
        c.setServerHomePageController(this);
    }
}
