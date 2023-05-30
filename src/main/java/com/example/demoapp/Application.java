package com.example.demoapp;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.security.GeneralSecurityException;
import java.io.IOException;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        stage.setTitle("Pomodoro Tracker");
        stage.setScene(scene);
        stage.show();
        stage.setMaxHeight(700);
        stage.setMaxWidth(700);

    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        ImportCalendar.passTodayEvents();
        launch();
        }
    }
