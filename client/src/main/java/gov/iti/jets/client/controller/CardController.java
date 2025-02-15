/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gov.iti.jets.client.controller;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.Set;

import gov.iti.jets.client.model.ClientImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import shared.dto.Card;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;

public class CardController implements Initializable {
    private UserInt userInt;
    private AdminInt adminInt;
    ClientImpl c;

    @FXML
    private Text friendNameText;
    @FXML
    private Text friendMessageText;
    @FXML
    private Circle friendStatusCircle;
    @FXML
    private Text friendMessageTimeText;
    @FXML
    private Circle imageCircle;

    public enum colorEnum {
        RED("#d06f65"),
        GREEN("#8dc587"),
        YELLOW("#e7df6d"),
        GRAY("#d0c4c4");

        public String haxColor;

        colorEnum(String haxColor) {
            this.haxColor = haxColor;
        }

        public String getColor() {
            return haxColor;
        }
    }


    public void setUserInt(UserInt userInt) {
        this.userInt = userInt;
    }

    public void setAdminInt(AdminInt adminInt) {
        this.adminInt = adminInt;
    }


    private ImageView SetImage(String imagePath) {
        ImageView imageView = new ImageView();
        String profilePicturePath = imagePath;
        if (profilePicturePath != null && !profilePicturePath.isEmpty()) {
            try {
                Image profileImage;

                if (Paths.get(profilePicturePath).isAbsolute()) {
                    File file = new File(profilePicturePath);
                    if (file.exists() && file.canRead()) {
                        profileImage = new Image(file.toURI().toString());
                        imageView.setImage(profileImage);

                    } else {
                        System.out.println("Error: File does not exist or cannot be read.");
                    }
                } else {
                    profileImage = new Image(getClass().getResource(profilePicturePath).toExternalForm());
                    imageView.setImage(profileImage);
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error loading profile image: " + e.getMessage());
            }
        }
        return imageView;
    }

    private String extractPlainText(String html) {
        if (html == null) {
            return "";
        }
        Document doc = Jsoup.parse(html);
        return doc.text();
    }


    public void setCard(Card item) {
        ImageView imageView = SetImage(item.getImagePath());
        imageCircle.setFill(new ImagePattern(imageView.getImage()));

        if (item.getStatus() != null) {
            switch (item.getStatus().toString()) {
                case "AVAILABLE":
                    friendStatusCircle.setFill(Color.valueOf(colorEnum.GREEN.getColor()));
                    break;
                case "BUSY":
                    friendStatusCircle.setFill(Color.valueOf(colorEnum.RED.getColor()));
                    break;
                case "AWAY":
                    friendStatusCircle.setFill(Color.valueOf(colorEnum.YELLOW.getColor()));
                    break;
                default:
                    friendStatusCircle.setFill(Color.valueOf(colorEnum.GRAY.getColor()));
                    break;
            }
        }

        friendNameText.setText(item.getSenderName() != null ? item.getSenderName() : "Unknown");
        friendMessageText.setText(extractPlainText(item.getMessageContent()));

        if (item.getTimestamp() != null) {
            String messageTime = item.getTimestamp().toString().substring(11, 16);
            friendMessageTimeText.setText(messageTime);
        } else {
            friendMessageTimeText.setText("N/A"); // Handle missing timestamp gracefully
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        c = ClientImpl.getInstance();
        c.setCardController(this);
    }

}
