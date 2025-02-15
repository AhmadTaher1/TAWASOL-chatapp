package gov.iti.jets.client.controller;

import gov.iti.jets.client.ClientMain;
import gov.iti.jets.client.model.ChatbotService;
import gov.iti.jets.client.model.ClientImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Worker;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import javafx.scene.paint.ImagePattern;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.web.HTMLEditor;
import netscape.javascript.JSObject;
import shared.dto.*;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;
import shared.utils.SecureStorage;

import javax.imageio.ImageIO;


public class HomeScreenController implements Initializable {

    private UserInt userInt;
    private AdminInt adminInt;
    ClientImpl c;
    List<Card> listOfContactCards;
    private ObservableList<Card> cardObservableList = javafx.collections.FXCollections.observableArrayList();
    static User currentUser = null;
    String Target_Type;
    int Target_ID;


    public void setCurrentUser(User currentUser) {
        System.out.println("setting the current user in the home page");
        System.out.println(currentUser);
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setUserInt(UserInt userInt) {
        this.userInt = userInt;
    }

    public void setAdminInt(AdminInt adminInt) {
        this.adminInt = adminInt;
    }


    @FXML
    private BorderPane MainBorderPane;
    @FXML
    private Button notificationbtn;
    @FXML
    private Button groupbtn;
    @FXML
    private Button addContactbtn;
    @FXML
    private Button botbtn;
    @FXML
    private Button blockListbtn;
    @FXML
    private Button logOutbtn;
    @FXML
    Circle userProfileImage;
    @FXML
    private Text userNameText;
    @FXML
    private Button EditProfilebtn;
    @FXML
    private TextField searchTextField;
    @FXML
    private ListView<Card> ContactList;
    @FXML
    private Circle friendImage;
    @FXML
    private Text friendName;
    @FXML
    private Button blockbtn;
    @FXML
    private ScrollPane chatMessageScrollPane;
    @FXML
    private HTMLEditor messageField;
    @FXML
    private ImageView friendProfileImage;
    @FXML
    private Label friendNameProfile;
    @FXML
    private Label friendPhone;
    @FXML
    private Button PDFbtn;
    @FXML
    private Button Vediobtn;
    @FXML
    private Button sendButton;
    @FXML
    private Button musicbtn;
    @FXML
    private Button Imagebtn;
    @FXML
    private Button attachmentButton;
    @FXML
    private ListView<BaseMessage> chatListView;
    @FXML
    private VBox vBox;
    @FXML
    private ImageView defaultPhoto;

    @FXML
    private StackPane stackPane;

    //    private ObservableList<BaseMessage> observableMessages = javafx.collections.FXCollections.observableArrayList();
    private ObservableList<BaseMessage> observableMessages = FXCollections.observableArrayList();
    boolean isFile = false;
    FileTransfer fileMsg;

    Image friImage = null;

    public void handleSendButton(ActionEvent actionEvent) {

        try {
            if (adminInt.getServerStatus() == true) {


                if (Target_Type == null) {
                    showErrorAlert("Choose a chat", "Please choose a user or a group to chat with.");
                    return;
                }

                if (isFile) {
                    //update the ui to show the file
                    //attachedFile.setTimestamp(new Timestamp(System.currentTimeMillis()));

                    observableMessages.add(fileMsg);
                    chatListView.refresh();
                    chatListView.scrollTo(observableMessages.size() + 3);
                    messageField.setHtmlText("");

                    try {
                        //send a file to a user
                        if (Target_Type.equals("user")) {
                            userInt.reload(userInt.getUserById(Target_ID).getPhoneNumber(), fileMsg, "user", currentUser.getUserId());
                        }

                        //send a file to a group
                        else {
                            try {
                                List<Integer> l = userInt.getUsersByGroupId(Target_ID);
                                for (Integer id : l) {
                                    if (id != currentUser.getUserId()) {
                                        userInt.reload(userInt.getUserById(id).getPhoneNumber(), fileMsg, "group", Target_ID);
                                        userInt.pushSound(userInt.getUserById(id).getPhoneNumber());
                                    }
                                }
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        }


                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }


                    isFile = false;
                    fileMsg = null;
                } else {
                    String htmlString = messageField.getHtmlText();
                    String message = htmlString.replaceAll("\\<.*?\\>", "").trim();
                    if (message.isEmpty() && !isFile) {
                        showErrorAlert("Empty message", "Please write a message to send.");
                        return;
                    } else {
                        if (Target_Type.equals("user")) {

                            UserBlockedConnection userBlockedConnection = null;
                            try {
                                userBlockedConnection = userInt.getBlockedConnection(currentUser.getUserId(), Target_ID);

                            } catch (RemoteException e) {
                                e.printStackTrace();

                            }
                            if (userBlockedConnection == null) {
                                DirectMessage directMessage = new DirectMessage();
                                directMessage.setMessageContent(htmlString);
                                directMessage.setSenderId(currentUser.getUserId());
                                directMessage.setReceiverId(Target_ID);
                                directMessage.setFontStyle("Arial");
                                directMessage.setFontColor("Black");
                                directMessage.setTextBackground("White");
                                directMessage.setFontSize(14);
                                directMessage.setBold(false);
                                directMessage.setItalic(false);
                                directMessage.setUnderlined(false);
                                directMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
                                try {
                                    userInt.insertDirectMessage(directMessage);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                                observableMessages.add(directMessage);
                                chatListView.refresh();
                                chatListView.scrollTo(observableMessages.size() + 3);
                                messageField.setHtmlText("");

                                try {

                                    System.out.println("message Target ID" + Target_ID);
                                    userInt.reload(userInt.getUserById(Target_ID).getPhoneNumber(), directMessage, "user", currentUser.getUserId());
                                    userInt.pushSound(userInt.getUserById(Target_ID).getPhoneNumber());

                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }

                                //I will put here the chatbot service work***********************************************************

                                boolean isChatbotEnabledForReciever = false;
                                try {
                                    isChatbotEnabledForReciever = userInt.isChatbotEnabled(Target_ID);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }

                                if (isChatbotEnabledForReciever) {

                                    Runnable r1 = new Runnable() {
                                        @Override
                                        public void run() {


                                            String messageContent = htmlString.replaceAll("\\<.*?\\>", "").trim();
                                            String response = ChatbotService.getChatbotResponse(messageContent);
                                            String htmlResponse = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">" + response + "</span></p></body></html>";
                                            DirectMessage botMessage = new DirectMessage();
                                            botMessage.setMessageContent(htmlResponse);
                                            botMessage.setSenderId(Target_ID);
                                            botMessage.setReceiverId(HomeScreenController.currentUser.getUserId());
                                            botMessage.setFontStyle("Arial");
                                            botMessage.setFontColor("Black");
                                            botMessage.setTextBackground("White");
                                            botMessage.setFontSize(14);
                                            botMessage.setBold(false);
                                            botMessage.setItalic(false);
                                            botMessage.setUnderlined(false);
                                            botMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
                                            try {
                                                userInt.insertDirectMessage(botMessage);
                                            } catch (RemoteException e) {
                                                e.printStackTrace();
                                            }
                                            Platform.runLater(new Runnable() {
                                                @Override
                                                public void run() {


                                                    observableMessages.add(botMessage);
                                                    chatListView.refresh();
                                                    chatListView.scrollTo(observableMessages.size());
                                                    try {
                                                        userInt.reload(userInt.getUserById(Target_ID).getPhoneNumber(), botMessage, "user", currentUser.getUserId());
                                                        userInt.pushSound(userInt.getUserById(Target_ID).getPhoneNumber());
                                                    } catch (RemoteException e) {
                                                        e.printStackTrace();
                                                    }


                                                }
                                            });

                                        }


                                    };
                                    Thread t1 = new Thread(r1);
                                    t1.start();


                                }
                            } else {
                                showErrorAlert("Error", "You can't send message to this user");
                                messageField.setHtmlText("");

                            }


                        } else if (Target_Type.equals("group")) {

                            GroupMessage groupMessage = new GroupMessage();
                            groupMessage.setMessageContent(htmlString);
                            groupMessage.setSenderId(currentUser.getUserId());
                            groupMessage.setGroupId(Target_ID);
                            groupMessage.setFontStyle("Arial");
                            groupMessage.setFontColor("Black");
                            groupMessage.setTextBackground("White");
                            groupMessage.setFontSize(14);
                            groupMessage.setBold(false);
                            groupMessage.setItalic(false);
                            groupMessage.setUnderlined(false);
                            groupMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
                            try {
                                userInt.addGroupMessage(groupMessage);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            observableMessages.add(groupMessage);
                            chatListView.refresh();
                            chatListView.scrollTo(observableMessages.size());
                            messageField.setHtmlText("");

                            try {
                                List<Integer> l = userInt.getUsersByGroupId(Target_ID);
                                for (Integer id : l) {
                                    if (id != currentUser.getUserId()) {
                                        userInt.reload(userInt.getUserById(id).getPhoneNumber(), groupMessage, "group", Target_ID);
                                        userInt.pushSound(userInt.getUserById(id).getPhoneNumber());
                                    }
                                }
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }


                        } else if (Target_Type.equals("announcement")) {
                            messageField.setHtmlText("");
                        } else {
                            System.out.println("Error in type");
                        }
                    }


                }
                Platform.runLater(() -> {
                    try {

                        listOfContactCards.clear();
                        listOfContactCards = userInt.getCards(HomeScreenController.currentUser);
                        cardObservableList.clear();
                        cardObservableList.addAll(listOfContactCards);
                        ContactList.refresh();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                System.out.println("server is off");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ServerUnavailable.fxml"));

                Parent root = loader.load();
                ServerUnavailableController serverUnavailableController = loader.getController();
                serverUnavailableController.setAdminInt(ClientMain.adminInt);
                serverUnavailableController.setUserInt(ClientMain.userInt);
                serverUnavailableController.setCurrentUser(currentUser);

                Stage stage = this.getStage();

                // Set the scene with the admin login page
                Scene scene = new Scene(root);
                stage.setScene(scene);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleEnterPress(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            handleSendButton(new ActionEvent());
        }
    }

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


    private ImageView SetImage(String imagePath) {
        ImageView imageView = new ImageView();


        userNameText.setText(currentUser.getDisplayName());

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


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Image defaultImage = new Image(getClass().getResourceAsStream("/img/tawasolLogoBlue.png"));
        defaultPhoto.setImage(defaultImage);

        vBox.setVisible(false);

        // Load the image
        Image image = new Image(getClass().getResourceAsStream("/img/backGround3.png"));


        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        // Set the background
        Background background = new Background(backgroundImage);
        chatListView.setBackground(background);


        Platform.runLater(() -> {

            // Get the current stage
            Stage stage = this.getStage();

            stage.setOnCloseRequest(event -> {
                try {
                    if (currentUser != null) { // If a user is logged in
                        userInt.unregister(c);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });


            c = ClientImpl.getInstance();
            c.homeScreenController = this;
            c.setPhoneNumber(currentUser.getPhoneNumber());
            try {
                userInt.register(c);
            } catch (RemoteException e) {
                e.printStackTrace();
            }


            //adding chatBot to the user
            try {
                Chatbot chatbot = userInt.getChatbotById(currentUser.getUserId());
                if (chatbot == null) {
                    userInt.addChatbotByUserID(currentUser.getUserId());

                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }


            messageField.lookupAll(".tool-bar").forEach(node -> {
                if (node instanceof ToolBar toolBar) {
                    List<Node> items = toolBar.getItems();
                    items.removeIf(item ->
                            item.toString().contains("indent") ||
                                    item.toString().contains("outdent") ||
                                    item.toString().contains("align") ||
                                    item.toString().contains("bullets") ||
                                    item.toString().contains("numbers") ||
                                    item.toString().contains("hr") ||
                                    item.toString().toLowerCase().contains("background")

                    );
                }
            });


            userNameText.setText(currentUser.getDisplayName());

            ImageView imageView = SetImage(currentUser.getProfilePicturePath());
            userProfileImage.setFill(new ImagePattern(imageView.getImage()));

        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    listOfContactCards = userInt.getCards(HomeScreenController.currentUser);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                cardObservableList.setAll(listOfContactCards);
                ContactList.setItems(cardObservableList);

                FilteredList<Card> cardFilteredList = new FilteredList<Card>(cardObservableList, p -> true);
                searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    cardFilteredList.setPredicate(item -> {
                        if (item == null || newValue.isEmpty())
                            return true;

                        String filter = newValue.toLowerCase();
                        String name = item.getSenderName().toLowerCase();
                        return name.contains(filter);
                    });
                });
                SortedList<Card> sortedList = new SortedList<>(cardFilteredList);
                ContactList.setItems(sortedList);
                ContactList.refresh();
            }
        });

        //*******************************************Cardlistview************************************************

        ContactList.setCellFactory((param) -> {

            ListCell<Card> cell = new ListCell<>() {
                @Override
                protected void updateItem(Card item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        if (item != null) {
                            try {
                                FXMLLoader Cardloader = new FXMLLoader(getClass().getResource("/fxml/Card.fxml"));
                                Node n = Cardloader.load(); // must load before getController
                                CardController card = Cardloader.getController();
                                card.setCard(item);
                                setGraphic(n);

                            } catch (IOException ex) {
                                Logger.getLogger(HomeScreenController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        setGraphic(null);
                    }
                }
            };
            return cell;
        });


        //********************************************chatlistview*************************************************

        chatListView.setCellFactory(listView -> new ListCell<BaseMessage>() {

            @Override
            protected void updateItem(BaseMessage msg, boolean empty) {
                super.updateItem(msg, empty);
                if (empty || msg == null) {
                    setGraphic(null);
                    setBackground(Background.EMPTY);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM");
                    String formattedTime = msg.getTimeStamp2().toLocalDateTime().format(formatter);

                    HBox container = new HBox();
                    VBox bubble = new VBox();


                    String senderName = "";

                    if (msg.getSenderName2().equals("DM") || msg.getSenderName2().equals("File")) {
                        //get sender name
                        User user = null;
                        try {
                            user = userInt.getUserById(msg.getSenderID2());
                            senderName = user.getDisplayName();

                        } catch (RemoteException e) {
                            System.out.println("Error in getting user name from base message incase of the type is user");
                            throw new RuntimeException(e);
                        }

                    } else if (msg.getSenderName2().equals("GM") || msg.getSenderName2().equals("File")) {
                        User user = null;
                        try {
                            user = userInt.getUserById(msg.getSenderID2());
                            senderName = user.getDisplayName();

                        } catch (RemoteException e) {
                            System.out.println("Error in getting user name from base message incase of the type is group");
                            throw new RuntimeException(e);
                        }

                    } else if (msg.getSenderName2().equals("TAWASOL")) {
                        senderName = "TAWASOL";
                    } else {
                        System.out.println("Error in getting sender name");
                    }


                    Text username = new Text();
                    if (msg.getSenderID2() != currentUser.getUserId()) {
                        try {
                            if (!Target_Type.equals("announcement")) {
                                username = new Text(userInt.getUserById(msg.getSenderID2()).getDisplayName());
                                username.setStyle("-fx-font-weight: bold;");
                            } else {
                                username = new Text("TAWASOL");
                                username.setStyle("-fx-font-weight: bold;");
                            }

                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    if (!Target_Type.equals("announcement")) {
                        //File Transfer handling
                        if (msg instanceof FileTransfer) {
                            FileTransfer cellFileMsg = (FileTransfer) msg;

                            // Create a horizontal box for the file message
                            HBox fileMessageBox = new HBox(10);

                            // Display an icon (using your getFileIcon method)
                            ImageView icon = new ImageView(getFileIcon(cellFileMsg.getFileName()));
                            icon.setFitWidth(32);
                            icon.setFitHeight(32);

                            // Display the file name
                            Label fileNameLabel = new Label(cellFileMsg.getFileName());

                            // Create a download button
                            Button viewButton = new Button("View");
                            viewButton.setMinWidth(Region.USE_PREF_SIZE);
                            viewButton.setMaxWidth(Region.USE_PREF_SIZE);
                            HBox.setHgrow(viewButton, Priority.NEVER);

                            viewButton.setOnAction(e -> {
                                handleFileDownload(cellFileMsg.getFileId());
                            });

                            fileMessageBox.getChildren().addAll(icon, fileNameLabel, viewButton);

                            // Add a timestamp below the file message
                            Text timestampText = new Text(formattedTime);
                            timestampText.setStyle("-fx-font-size: 10px; -fx-fill: gray;");

                            bubble.getChildren().addAll(username, fileMessageBox, timestampText);

                        } else {
                            // Regular text message whether is a direct or group message

                            // Convert HTML content into JavaFX TextFlow
                            TextFlow messageText = createStyledTextFlow(msg.getMessageContent2());

                            Text timestampText = new Text(formattedTime);
                            timestampText.setStyle("-fx-font-size: 10px; -fx-fill: gray;");

                            if (username.getText().isEmpty()) {
                                bubble.getChildren().addAll(messageText, timestampText);
                            } else {
                                bubble.getChildren().addAll(username, messageText, timestampText);
                            }
                        }

                    }
                    //if it is an announcement
                    else {
                        String htmlResponse = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">" + msg.getMessageContent2() + "</span></p></body></html>";
                        TextFlow messageTextFlow = createStyledTextFlow(htmlResponse);

                        Text timestampText = new Text(formattedTime);
                        timestampText.setStyle("-fx-font-size: 10px; -fx-fill: gray;");

                        bubble.getChildren().addAll(username, messageTextFlow, timestampText);
                    }


                    bubble.setPadding(new Insets(8));
                    bubble.setMaxWidth(250);
                    bubble.setStyle("-fx-background-radius: 15px; -fx-padding: 10px;");

                    if (msg.getSenderID2() == currentUser.getUserId()) {
                        bubble.setStyle("-fx-background-color: #61baff; -fx-background-radius: 15px;");
                        container.setAlignment(Pos.CENTER_RIGHT);
                    } else {
                        bubble.setStyle("-fx-background-color: lightgray; -fx-background-radius: 15px;");
                        container.setAlignment(Pos.CENTER_LEFT);
                    }

                    container.getChildren().add(bubble);
                    setGraphic(container);
                }
            }
        });

    }
//***************************************************************************************************************************


    private String extractPlainText(String html) {
        Document doc = Jsoup.parse(html);
        return doc.text();
    }

    private TextFlow createStyledTextFlow(String html) {
        Document doc = Jsoup.parse(html);
        TextFlow textFlow = new TextFlow();

        for (Element element : doc.select("body *")) {
            String textContent = element.ownText();
            if (!textContent.isEmpty()) {
                Text textNode = new Text(textContent);

                // Extract styles from the 'style' attribute
                String style = element.attr("style");

                StringBuilder fxStyle = new StringBuilder();

                // Apply bold
                if (style.contains("font-weight: bold")) {
                    fxStyle.append("-fx-font-weight: bold;");
                }

                // Apply italic
                if (style.contains("font-style: italic")) {
                    fxStyle.append("-fx-font-style: italic;");
                }

                // Extract text decorations
                if (style.contains("text-decoration:")) {
                    String textDecoration = extractStyleValue(style, "text-decoration");
                    if (textDecoration.contains("underline")) {
                        textNode.setUnderline(true);
                    }
                    if (textDecoration.contains("line-through")) {
                        textNode.setStrikethrough(true);
                    }
                }

                // Extract and apply font size
                if (style.contains("font-size:")) {
                    String fontSize = extractStyleValue(style, "font-size");
                    fxStyle.append("-fx-font-size: ").append(fontSize).append(";");
                }

                // Extract and apply font color
                if (style.contains("color:")) {
                    String fontColor = extractStyleValue(style, "color");
                    fxStyle.append("-fx-fill: ").append(fontColor).append(";");
                }

                // Extract and apply background color
                if (style.contains("background-color:")) {
                    String bgColor = extractStyleValue(style, "background-color");
                    fxStyle.append("-fx-background-color: ").append(bgColor).append(";");
                }

                // Extract and apply font family
                if (style.contains("font-family:")) {
                    String fontFamily = extractStyleValue(style, "font-family");
                    fxStyle.append("-fx-font-family: ").append(fontFamily).append(";");
                }

                // Apply final styles
                textNode.setStyle(fxStyle.toString());

                textFlow.getChildren().add(textNode);
            }
        }
        return textFlow;
    }


    private String extractStyleValue(String style, String property) {
        int startIndex = style.indexOf(property);
        if (startIndex == -1) return "";
        startIndex += property.length() + 2; // Skip property name and colon
        int endIndex = style.indexOf(";", startIndex);
        return (endIndex == -1) ? style.substring(startIndex).trim() : style.substring(startIndex, endIndex).trim();
    }


    public void updateUI() {
        userNameText.setText(currentUser.getDisplayName());
        //userProfileImage.setImage(new Image(getClass().getResource(currentUser.getProfilePicturePath()).toString()));
        File file = new File(currentUser.getProfilePicturePath());
        if (file.exists()) {
            Image defaultPhoto = new Image(file.toURI().toString());
            userProfileImage.setFill(new ImagePattern(defaultPhoto));
        } else {
            // Handle case where the file doesn't exist
            System.out.println("Image file not found: " + currentUser.getProfilePicturePath());
        }
    }


    void addCardtoListView(Card card, String phoneName) {
        try {
            // Done
            if (card.getType().equals("group")) {
                //send for all
                System.out.println("card id" + card.getId() + "current id" + currentUser.getUserId() + "Target Id" + Target_ID);
                List<Integer> l = userInt.getUsersByGroupId(card.getId());
                System.out.println(l);
                for (Integer id : l) {
                    userInt.reloadContactList(userInt.getUserById(id).getPhoneNumber(), card);
                }
            } else if (card.getType().equals("user")) {
                //list<Card> newCard -> {,,,}
                if (card.getId() != currentUser.getUserId()) {
                    listOfContactCards.addFirst(card);
                    cardObservableList.clear();
                    cardObservableList.setAll(listOfContactCards);
                    ContactList.refresh();
                } else {
                    userInt.reloadContactList(phoneName, card);
                }
            }


        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    public String GetCard(HBox item) {

        Group group1 = (Group) item.getChildren().get(0);
        ImageView image = (ImageView) group1.getChildren().get(0);
        Circle status = (Circle) group1.getChildren().get(1);

        VBox vbox1 = (VBox) item.getChildren().get(1);
        Text name = (Text) vbox1.getChildren().get(0);
        Text message = (Text) vbox1.getChildren().get(1);

        return name.toString();
    }

    @FXML
    void handleSelectedCard() {

        defaultPhoto.setVisible(false);
        vBox.setVisible(true);


        Card card = ContactList.getSelectionModel().getSelectedItem();

        if (card == null) {
            System.out.println("I'm a null card");
        } else {
            System.out.println(" Name " + card.getSenderName() + " Type " + card.getType() + " id " + card.getId());
            ImageView imageView = SetImage(card.getImagePath());
            friImage = imageView.getImage();
            friendImage.setFill(new ImagePattern(imageView.getImage()));
            friendName.setText(card.getSenderName());


            Platform.runLater(() -> {
                try {
                    Target_ID = card.getId();
                    Target_Type = card.getType();

                    populateChatListView(Target_Type, Target_ID);
                    // fillChatListView();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });


            if (card.getType().equals("user")) {
                System.out.println("user");
                AnchorPane anchorPane = null;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FriendProfile.fxml"));
                    anchorPane = loader.load();
                    FriendProfileController controller = loader.getController();
                    User user = userInt.getUserById(card.getId());
                    System.out.println(user.getDisplayName());
                    controller.setInfo(friImage, user.getDisplayName(), user.getPhoneNumber(), user.getBio());
                } catch (IOException e) {
                    System.out.println("failed");
                    e.printStackTrace();
                }
                MainBorderPane.setRight(anchorPane);


            } else if (card.getType().equals("group")) {

                System.out.println("group");

                AnchorPane anchorPane = null;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GroupProfile.fxml"));
                    anchorPane = loader.load();
                    GroupProfileController controller = loader.getController();
                    System.out.println(card.getId());
                    String createdGroup = userInt.getCreatedGroupName(card.getId());
                    List<Integer> membersId = userInt.getUsersByGroupId(card.getId());
                    List<String> members = new ArrayList<>(membersId.size() - 1);
                    for (int id : membersId) {
                        members.add(userInt.getUserById(id).getDisplayName() + "\n");
                    }
                    System.out.println(createdGroup);
                    controller.setInfo(friImage, card.getSenderName(), createdGroup, members);


                } catch (IOException e) {
                    System.out.println("failed");
                    e.printStackTrace();
                }
                MainBorderPane.setRight(anchorPane);


            } else if (card.getType().equals("announcement")) {
                System.out.println("announcement");
                AnchorPane anchorPane = null;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AnnouncementProfile.fxml"));
                    anchorPane = loader.load();

                } catch (IOException e) {
                    System.out.println("failed");
                    e.printStackTrace();
                }
                MainBorderPane.setRight(anchorPane);
            }
        }
    }

    @FXML
    void handleAddContact(ActionEvent event) {
        System.out.println("Add Contact button clicked!");
        // Add your logic here

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddContactWindow.fxml"));

            Parent root = loader.load();
            AddContactWindowController addContactWindowController = loader.getController();
            addContactWindowController.setAdminInt(ClientMain.adminInt);
            addContactWindowController.setUserInt(ClientMain.userInt);
            addContactWindowController.setCurrentUser(currentUser);
            addContactWindowController.setHomeScreenController(this);

            Stage addContactStage = new Stage();
            addContactStage.setTitle("Add Contact");

            // Set the scene for the small window
            addContactStage.setScene(new Scene(root));

            // Optional: Set modality to block the main window
            addContactStage.initModality(Modality.APPLICATION_MODAL);
            addContactStage.setResizable(false);

            // Show the small window
            addContactStage.showAndWait(); // Use show() for a non-blocking window

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleCreateGroup(ActionEvent event) {
        System.out.println("Create Group button clicked!");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CreateGroupWindow.fxml"));

            Parent root = loader.load();
            CreateGroupController createGroupController = loader.getController();
            createGroupController.setAdminInt(ClientMain.adminInt);
            createGroupController.setUserInt(ClientMain.userInt);
            createGroupController.setCurrentUser(currentUser);
            createGroupController.setHomeScreenController(this);

            Stage createGroupStage = new Stage();
            createGroupStage.setTitle("Create Group");

            // Set the scene for the small window
            createGroupStage.setScene(new Scene(root));

            // Optional: Set modality to block the main window
            createGroupStage.initModality(Modality.APPLICATION_MODAL);
            createGroupStage.setResizable(false);

            // Show the small window
            createGroupStage.showAndWait(); // Use show() for a non-blocking window

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleLogoutButton() {
        System.out.println("log out button pressed");
        SecureStorage.clearCredentials();
        try {
            userInt.unregister(c);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


        try {
            // Load the UserLoginPage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserLoginPage.fxml"));
            Parent userLoginRoot = loader.load();
            UserLoginController controller = loader.getController();
            controller.setUserInt(userInt);
            controller.setAdminInt(adminInt);


            Stage stage = getStage();
            double width = stage.getWidth();
            double height = stage.getHeight();

            Scene scene = new Scene(userLoginRoot);
            stage.setScene(scene);
            stage.setWidth(width);
            stage.setHeight(height);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleInvitationListButton() {
        // Add your logic here

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InvitationWindow.fxml"));
            Parent root = loader.load();
            if (ClientMain.userInt == null) {
                System.out.println("null");
            }
            InvitationListWindowController invitationListWindowController = loader.getController();
            invitationListWindowController.setAdminInt(ClientMain.adminInt);
            invitationListWindowController.setUserInt(ClientMain.userInt);
            invitationListWindowController.setCurrentUser(currentUser);
            invitationListWindowController.setHomeScreenController(this);


            Stage addContactStage = new Stage();
            addContactStage.setTitle("Invitation List");

            // Set the scene for the small window
            addContactStage.setScene(new Scene(root));

            // Optional: Set modality to block the main window
            addContactStage.initModality(Modality.APPLICATION_MODAL);
            addContactStage.setResizable(false);


            // Show the small window
            addContactStage.showAndWait(); // Use show() for a non-blocking window


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void handleEditProfileButton() {
        System.out.println("Edit button clicked!");
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditWindow.fxml"));
            Parent root = loader.load();
            EditWindowController editWindowController = loader.getController();
            editWindowController.setAdminInt(adminInt);
            editWindowController.setUserInt(userInt);
            editWindowController.setCurrentUser(currentUser);
            editWindowController.setHomeScreenController(this);

            Stage editStage = new Stage();
            editStage.setTitle("Edit Info");

            // Set the scene for the small window
            editStage.setScene(new Scene(root));

            // Optional: Set modality to block the main window
            editStage.initModality(Modality.APPLICATION_MODAL);
            editStage.setResizable(false);


            // Show the small window
            editStage.showAndWait(); // Use show() for a non-blocking window
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleNotificationButton() {
        System.out.println("notification window pressed");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NotificationWindow.fxml"));
            Parent root = loader.load();
            if (ClientMain.userInt == null) {
                System.out.println("null");
            }
            NotificationWindowController notificationWindowController = loader.getController();
            notificationWindowController.setAdminInt(ClientMain.adminInt);
            notificationWindowController.setUserInt(ClientMain.userInt);
            notificationWindowController.setCurrentUser(currentUser);
            notificationWindowController.setHomeScreenController(this);


            Stage notificationStage = new Stage();
            notificationStage.setTitle("Notifications");

            // Set the scene for the small window
            notificationStage.setScene(new Scene(root));

            // Optional: Set modality to block the main window
            notificationStage.initModality(Modality.APPLICATION_MODAL);
            notificationStage.setResizable(false);


            // Show the small window
            notificationStage.showAndWait(); // Use show() for a non-blocking window


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleBotButton(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChatbotWindow.fxml"));
            Parent root = loader.load();
            ChatbotWindowController chatbotWindowController = loader.getController();
            chatbotWindowController.setUserInt(ClientMain.userInt);
            chatbotWindowController.setAdminInt(ClientMain.adminInt);
            chatbotWindowController.setCurrentUser(currentUser);
            chatbotWindowController.setHomeScreenController(this);
            if (root == null) {
                System.out.println("null");
            }

            Stage chatBotStage = new Stage();
            chatBotStage.setTitle("Chat bot");

            // Set the scene for the small window
            chatBotStage.setScene(new Scene(root));

            // Optional: Set modality to block the main window
            chatBotStage.initModality(Modality.APPLICATION_MODAL);
            chatBotStage.setResizable(false);


            // Show the small window
            chatBotStage.showAndWait(); // Use show() for a non-blocking window


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


    //chat work*****************************************************
    public void populateChatListView(String type, int id) throws RemoteException {

        if (type.equals("group")) {

            List<BaseMessage> list = userInt.getGroupMessages(id);
            observableMessages.clear();
            observableMessages.addAll(list);

        } else if (type.equals("user")) {

            List<BaseMessage> list = userInt.getMessagesBetweenTwo(currentUser.getUserId(), id);
            observableMessages.clear();
            observableMessages.addAll(list);

        } else if (type.equals("announcement")) {
            observableMessages.clear();
            List<ServerAnnouncement> list = userInt.getAllServerAnnouncementsBasedOnCreatedTime(currentUser.getUserId());
            observableMessages.addAll(list);
        } else {

            System.out.println("Error in type");
        }

        chatListView.setItems(observableMessages);
        chatListView.refresh();
        chatListView.scrollTo(observableMessages.size());
    }


    public void refreshChatList(BaseMessage message, String Type, int ID) {
        Platform.runLater(() -> {
            try {
                System.out.println("Targer id " + Target_ID); // from where
                System.out.println("id " + ID); //from where
                listOfContactCards.clear();
                listOfContactCards = userInt.getCards(HomeScreenController.currentUser);
                cardObservableList.clear();
                cardObservableList.addAll(listOfContactCards);
                ContactList.refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        if (chatListView == null) {
            System.out.println("Chat list is null!");
            return;
        }

        if (observableMessages == null) {
            System.out.println("Observable messages list is null!");
            return;
        }

        if (Target_Type == null || Type == null) {
            System.out.println("Target_Type or Type is null!");
            return;
        }

        if (ContactList == null) {
            System.out.println("ContactList is null!");
            return;
        }


        if (Objects.equals(Target_Type, Type) && Target_ID == ID) {
            Platform.runLater(() -> {
                // if (Target_Type.equals(Type) && Target_ID == ID) {
                try {
                    observableMessages.add(message);
                    chatListView.refresh();
                    chatListView.scrollTo(observableMessages.size());


                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }

    public void refreshContactList(Card c) {
        Platform.runLater(() -> {
            try {
                listOfContactCards.addFirst(c);
                cardObservableList.clear();
                cardObservableList.addAll(listOfContactCards);
                ContactList.refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }


    public Stage getStage() {
        return (Stage) groupbtn.getScene().getWindow();
    }


    @FXML
    private void handleAttachmentButton(ActionEvent event) {
        if (Target_Type == null) {
            showErrorAlert("Choose a chat", "Please choose a user or a group to chat with.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Attach");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("Documents", "*.pdf", "*.doc", "*.docx", "*.txt"),
                new FileChooser.ExtensionFilter("Videos", "*.mp4", "*.avi", "*.mov", "*.mkv"),
                new FileChooser.ExtensionFilter("Audios", "*.mp3", "*.wav", "*.aac")
        );

        File selectedFile = fileChooser.showOpenDialog(attachmentButton.getScene().getWindow());
        if (selectedFile != null) {
            new Thread(() -> {
                try {
                    String fileName = selectedFile.getName();
                    String fileType = Files.probeContentType(selectedFile.toPath());

                    // Read the entire file into a byte array
                    byte[] fileData = Files.readAllBytes(selectedFile.toPath());

                    UUID fileId;
                    if (Target_Type.equals("user")) {
                        fileId = userInt.uploadFile(
                                currentUser.getUserId(),
                                Target_ID,
                                null,
                                fileName,
                                fileType,
                                fileData
                        );

                        fileMsg = new FileTransfer(fileId, currentUser.getUserId(), Target_ID, null, fileName, fileType, fileData, new Timestamp(System.currentTimeMillis()));
                        isFile = true;
                    } else if (Target_Type.equals("group")) {
                        fileId = userInt.uploadFile(
                                currentUser.getUserId(),
                                null,
                                Target_ID,    // group ID for group chat
                                fileName,
                                fileType,
                                fileData
                        );
                        fileMsg = new FileTransfer(fileId, currentUser.getUserId(), null, Target_ID, fileName, fileType, fileData, new Timestamp(System.currentTimeMillis()));
                        isFile = true;
                    } else {
                        fileId = null;
                    }


                    showSuccessAlert("File uploaded successfully!");

                } catch (Exception e) {
                    Platform.runLater(() ->
                            showErrorAlert("Error uploading file: " + e.getMessage()));
                }
            }).start();

        }
    }


    private Image getFileIcon(String fileName) {
        String imagePath = "/img/pdf.png"; // default icon
        if (fileName.toLowerCase().endsWith(".pdf")) {
            imagePath = "/img/pdf.png";
        } else if (fileName.toLowerCase().matches(".*\\.(png|jpg|jpeg|gif)$")) {
            imagePath = "/img/insert-picture-icon.png";
        } else if (fileName.toLowerCase().matches(".*\\.(mp4|avi|mov|mkv)$")) {
            imagePath = "/img/clapperboard.png";
        } else if (fileName.toLowerCase().matches(".*\\.(mp3|wav|aac)$")) {
            imagePath = "/img/music.png";
        }
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }


    private void handleFileDownload(UUID fileId) {
        new Thread(() -> {
            try {
                // Call the RMI service to download the file (returns file bytes)
                byte[] fileData = userInt.downloadFile(fileId, currentUser.getUserId());

                // Retrieve the original file name from the server
                String originalFileName = userInt.getFileName(fileId);

                // Create a Downloads directory in the user's home directory
                Path downloadDir = Paths.get(System.getProperty("user.home"), "/Downloads/Server_Downloads");
                Files.createDirectories(downloadDir);

                // Save the downloaded file using the original file name
                Path outputPath = downloadDir.resolve(originalFileName);
                Files.write(outputPath, fileData);

                Platform.runLater(() -> {
                    showSuccessAlert("File saved to: " + outputPath);
                    try {
                        Desktop.getDesktop().open(outputPath.toFile());
                    } catch (IOException e) {
                        showErrorAlert("Couldn't open file automatically");
                    }
                });
            } catch (RemoteException e) {
                Platform.runLater(() -> showErrorAlert("Download failed: " + e.getMessage()));
            } catch (IOException e) {
                Platform.runLater(() -> showErrorAlert("File handling error: " + e.getMessage()));
            }
        }).start();
    }

    // Dummy alert methods
    private void showSuccessAlert(String message) {
        System.out.println("SUCCESS: " + message);
    }

    private void showErrorAlert(String message) {
        System.err.println("ERROR: " + message);
    }

}

