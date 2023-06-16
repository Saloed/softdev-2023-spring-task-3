package com.example.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("TESSERACT");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.getIcons().add(new Image("Images/Icon.png"));
        stage.show();
    }
    public static void main(String[] args) { launch(); }
}