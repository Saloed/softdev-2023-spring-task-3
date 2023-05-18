package org.game.view_control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game_scene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getRoot().requestFocus();
        primaryStage.setScene(scene);
        primaryStage.setTitle("16384");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.jpg"))));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}