module gov.iti.jets.client {
    //requires gov.iti.jets.shared;
    requires javafx.fxml;
    requires java.rmi;
    requires java.sql;
    requires javafx.controls;
    //requires gov.iti.jets.shared;
    requires javafx.web;

    //chatbot requires
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.json;
    requires jdk.jsobject;
    requires org.jsoup;
    requires java.desktop;
    requires java.prefs;
    //**********************************


    //requires javafx.web;

    opens gov.iti.jets.client to javafx.fxml;
    opens gov.iti.jets.client.controller to javafx.fxml;
    exports gov.iti.jets.client;
    exports gov.iti.jets.client.model;

    //call back
    exports shared.interfaces to java.rmi;


    opens gov.iti.jets.client.model to javafx.fxml;
    exports shared.utils;
    opens shared.utils to javafx.fxml,java.rmi;
    requires jakarta.mail;

    //exports shared.utils;
}
