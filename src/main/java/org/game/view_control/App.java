package org.game.view_control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.game.game.MainLogic;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game_scene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getRoot().requestFocus();
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("16384");
        primaryStage.show();
        MainLogic.init();
    }

    public static void main(String[] args) {
        launch(args);
    }

}