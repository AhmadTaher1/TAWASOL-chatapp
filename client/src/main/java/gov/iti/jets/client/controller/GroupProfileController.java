package gov.iti.jets.client.controller;

import gov.iti.jets.client.model.ClientImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GroupProfileController implements Initializable {
    private UserInt userInt;
    private AdminInt adminInt;
    ClientImpl c;

    @FXML
    private ImageView groupProfileImage;
    @FXML
    private Text groupProfileName;
    @FXML
    private Text createdGroupName;
    @FXML
    private TextFlow MembersName;

    public void setUserInt(UserInt userInt) {
        this.userInt = userInt;
    }

    public void setAdminInt(AdminInt adminInt) {
        this.adminInt = adminInt;
    }


    public void setInfo(Image image, String name, String createdGroup, List<String> members) {
        groupProfileImage.setImage(image);
        groupProfileName.setText(name);
        createdGroupName.setText("Created by: " + createdGroup);
        Text text;
        for (String m : members) {
            text = new Text(m);
            text.setStyle("-fx-font-size: 16px;");
            MembersName.getChildren().add(text);
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        c = ClientImpl.getInstance();
        c.setGroupProfileController(this);
    }
}
