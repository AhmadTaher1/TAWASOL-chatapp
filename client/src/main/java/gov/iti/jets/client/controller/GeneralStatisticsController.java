package gov.iti.jets.client.controller;

import gov.iti.jets.client.ClientMain;
import gov.iti.jets.client.model.ClientImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GeneralStatisticsController implements Initializable {

    private UserInt userInt;
    private AdminInt adminInt;
    ClientImpl c;

    public void setUserInt(UserInt userInt) {
        this.userInt = userInt;
    }

    public  void setAdminInt(AdminInt adminInt) {
        this.adminInt = adminInt;
    }

    @FXML
    private BorderPane childBorderpane;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button statepiebutton;

    @FXML
    private Button Genderpiebutton;

    @FXML
    private Button countrypiebutton;

    @FXML
    void countrypiemethod(ActionEvent event) {

        countrypiebutton.setStyle("-fx-background-color: #B0B0B0;");
        statepiebutton.setStyle("");
        Genderpiebutton.setStyle("");
        BorderPane borderPane=null;
        try {

            FXMLLoader loader= new  FXMLLoader(getClass().getResource("/fxml/hello-view -country.fxml"));
            borderPane=loader.load();
            CountryPieController countryPieController= loader.getController();
            countryPieController.setAdminInt(ClientMain.adminInt);
            countryPieController.setUserInt(ClientMain.userInt);

        } catch (IOException e) {
            System.out.println("failed to load it");
        }
        childBorderpane.setCenter(borderPane);
    }

    @FXML
    void genderpiemethod(ActionEvent event) {

        statepiebutton.setStyle("");
        Genderpiebutton.setStyle("-fx-background-color: #B0B0B0;");
        countrypiebutton.setStyle("");
        BorderPane borderPane=null;
        try {
            FXMLLoader  loader=  new FXMLLoader(getClass().getResource("/fxml/hello-view -gender.fxml"));
            borderPane=loader.load();
            GenderPieController genderPieController= loader.getController();
            genderPieController.setAdminInt(ClientMain.adminInt);
            genderPieController.setUserInt(ClientMain.userInt);

        } catch (IOException e) {
            System.out.println("failed to load it");
        }
        childBorderpane.setCenter(borderPane);


    }

    @FXML
    void statepiemethod(ActionEvent event) {


        statepiebutton.setStyle("-fx-background-color: #B0B0B0;");
        Genderpiebutton.setStyle("");
        countrypiebutton.setStyle("");
        BorderPane borderPane=null;
        try {
            FXMLLoader loader= new FXMLLoader((getClass().getResource("/fxml/hello-view - statistics.fxml")));
            borderPane=loader.load();
            StatisticsPieController statisticsPieController=loader.getController();
            statisticsPieController.setAdminInt(ClientMain.adminInt);
            statisticsPieController.setUserInt(ClientMain.userInt);

        } catch (IOException e) {
            System.out.println("failed to load it");
        }
        childBorderpane.setCenter(borderPane);


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        c= ClientImpl.getInstance();
        c.setGeneralStatisticsController(this);
    }
}
