module gov.iti.jets.server {
    requires java.sql;
    requires java.rmi;
    requires javafx.controls;
    requires javafx.fxml;
    //requires gov.iti.jets.shared;
    opens gov.iti.jets.server to javafx.fxml, java.rmi;
    exports gov.iti.jets.server;
    exports shared.interfaces;
    requires jakarta.mail;


    //chatbot requires
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.apache.httpcomponents.httpcore;
    requires org.json;
    requires org.apache.httpcomponents.httpclient;
    requires java.prefs;
}
