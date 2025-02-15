package gov.iti.jets.client.controller;

import gov.iti.jets.client.model.ClientImpl;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.BorderPane;
import shared.dto.Admin;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

public class GenderPieController implements Initializable {

    private UserInt userInt;
    private AdminInt adminInt;
    Registry registry;
    ClientImpl c;

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
    private BorderPane grandchildborderpanethree;

    @FXML
    private PieChart genderpie;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            registry = LocateRegistry.getRegistry("localhost", 8554);
            adminInt = (AdminInt) registry.lookup("AdminServices");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        try {
            int femaleCount = adminInt.getNumberOfUsersBasedOnGender("female");
            int maleCount = adminInt.getNumberOfUsersBasedOnGender("male");


            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            if (femaleCount > 0) {
                pieChartData.add(new PieChart.Data("Female", femaleCount));
            }
            if (maleCount > 0) {
                pieChartData.add(new PieChart.Data("male", maleCount));
            }

            pieChartData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(), " number ", data.pieValueProperty())));
            genderpie.getData().addAll(pieChartData);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        c = ClientImpl.getInstance();
        c.setGenderPieController(this);


    }
}

