package gov.iti.jets.client.controller;

import gov.iti.jets.client.model.ClientImpl;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.BorderPane;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;


public class StatisticsPieController implements Initializable {

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
    private BorderPane grandchildborderpaneone;

    @FXML
    private PieChart statisticspie;

    //    @FXML
//    void initialize() {
//        assert grandchildborderpaneone != null : "fx:id=\"grandchildborderpaneone\" was not injected: check your FXML file 'hello-view - statistics.fxml'.";
//        assert statisticspie != null : "fx:id=\"statisticspie\" was not injected: check your FXML file 'hello-view - statistics.fxml'.";
//
//    }
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
            int onlineUsersCount = adminInt.getNumberOfUsersBasedOnStat("online");
            int offlineUsersCount = adminInt.getNumberOfUsersBasedOnStat("offline");


            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();


            pieChartData.add(new PieChart.Data("online", onlineUsersCount));


            pieChartData.add(new PieChart.Data("offline", offlineUsersCount));


            pieChartData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(), " number ", data.pieValueProperty())));
            statisticspie.getData().addAll(pieChartData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        c = ClientImpl.getInstance();
        c.setStatisticsPieController(this);

    }
}

