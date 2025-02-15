package gov.iti.jets.client.controller;

import gov.iti.jets.client.model.ClientImpl;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;

import java.net.URL;
import java.util.ResourceBundle;

public class BlockContactsController implements Initializable {
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
    private ListView<String> listView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        c = ClientImpl.getInstance();
        c.setBlockContactsController(this);
    }


}
