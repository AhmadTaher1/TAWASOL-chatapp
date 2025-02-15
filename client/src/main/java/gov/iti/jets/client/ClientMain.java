package gov.iti.jets.client;

import gov.iti.jets.client.controller.UserLoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain extends Application {

    public static UserInt userInt;
    public static AdminInt adminInt;


    static {
        try {


            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 8554);
            userInt = (UserInt) registry.lookup("UserServices");
            adminInt = (AdminInt) registry.lookup("AdminServices");
            System.out.println("Connected to RMI Server!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {


        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserLoginPage.fxml"));

            // FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ServerUnavailable.fxml"));


            root = loader.load();
            UserLoginController userLoginController = loader.getController();

            if (userLoginController == null) {
                System.out.println("Controller is NULL! Check FXML setup.");
            } else {
                userLoginController.setUserInt(userInt);
                userLoginController.setAdminInt(adminInt);

            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not load the UserLoginPage.fxml file.");
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Chat Application");
        stage.setResizable(true);
        stage.setMinWidth(1200);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }


}


//package gov.iti.jets.client.controller;
//
//import javafx.application.*;
//import javafx.fxml.*;
//import javafx.scene.*;
//import javafx.stage.*;
//import java.io.*;
//
//
//public class App extends Application {
//
//    private static Scene scene;
//
//    public static String userName;
//
//    static boolean isOnPressed;
//
//    static boolean isOffPressed;
//
//
//
//
//    @Override
//    public void start(Stage stage) throws IOException {
//        //scene = new Scene(loadFXML("fxml/homeScreen"));
//        scene = new Scene(loadFXML("fxml/UserLoginPage"));
//        stage.setScene(scene);
//        stage.show();
//
//    }
//
//    static void setRoot(String fxml) throws IOException {
//        scene.setRoot(loadFXML(fxml));
//    }
//
//    private static Parent loadFXML(String fxml) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
//        return fxmlLoader.load();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//
//}
