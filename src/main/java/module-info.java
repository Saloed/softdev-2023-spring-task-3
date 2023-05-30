module com.example.demoapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.gson;
    requires com.google.api.services.calendar;
    requires javafx.media;
    requires jdk.httpserver;
    opens com.example.demoapp to javafx.fxml;
    exports com.example.demoapp;
}