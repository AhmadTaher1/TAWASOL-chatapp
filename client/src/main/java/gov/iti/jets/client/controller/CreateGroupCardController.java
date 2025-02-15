package gov.iti.jets.client.controller;

import java.io.File;
import java.nio.file.Paths;
import java.rmi.RemoteException;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class CreateGroupCardController {

    @FXML
    private ImageView frinedImage;

    @FXML
    private Text friendNameText;

    @FXML
    private Circle personalImg;

    public void setContactData(String name, String imgPath) {


        friendNameText.setText(name);

        if (imgPath != null && !imgPath.isEmpty()) {
            try {
                Image profileImage;

                if (Paths.get(imgPath).isAbsolute()) {
                    File file = new File(imgPath);
                    if (file.exists() && file.canRead()) {
                        profileImage = new Image(file.toURI().toString());
                    } else {
                        System.out.println("Error: File does not exist or cannot be read.");
                        return;
                    }
                } else {
                    profileImage = new Image(getClass().getResource(imgPath).toExternalForm());
                }

                personalImg.setFill(new ImagePattern(profileImage));


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error loading profile image: " + e.getMessage());
            }
        }
    }


}
