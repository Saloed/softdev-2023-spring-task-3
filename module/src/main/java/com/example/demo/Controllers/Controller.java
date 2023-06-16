package com.example.demo.Controllers;

import com.example.demo.Controllers.Levels.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    private Button YesButton;
    @FXML
    private Pane PlayPane;
    @FXML
    private Pane SettingsPane;
    @FXML
    private Pane InformationPane;
    @FXML
    private Pane HallOfFamePane;
    @FXML
    private Pane QuitPane;
    @FXML
    private Button Level0Button;
    @FXML
    private Button Level1Button;
    @FXML
    private Button Level2Button;
    @FXML
    private Button Level3Button;
    @FXML
    private Button Level4Button;
    @FXML
    private Label Record0;
    @FXML
    private Label Record1;
    @FXML
    private Label Record2;
    @FXML
    private Label Record3;
    @FXML
    private Label Record4;
    @FXML
    protected void Play() {
        PlayPane.setVisible(true);
    }
    @FXML
    protected void Settings() {
        SettingsPane.setVisible(true);
    }
    @FXML
    protected void Information() {
        InformationPane.setVisible(true);
    }
    @FXML
    protected void HallOfFame() throws IOException {
        HallOfFamePane.setVisible(true);
        List<String> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader(
                new FileReader("module/src/main/resources/completedLevels.txt"));
        String just;
        while ((just = reader.readLine()) != null) list.add(just);
        int count = 0;
        for (String string : list) {
            int seconds = Integer.parseInt(string);
            if (seconds != 0) {
                String watch = watch(seconds);
                if (count == 0) Record0.setText(watch);
                if (count == 1) Record1.setText(watch);
                if (count == 2) Record2.setText(watch);
                if (count == 3) Record3.setText(watch);
                if (count == 4) Record4.setText(watch);
            }
            count++;
        }
    }
    @FXML
    protected void Quit() {
        QuitPane.setVisible(true);
    }
    @FXML
    protected void Yes() {
        YesButton.getScene().getWindow().hide();
    }
    @FXML
    protected void BackPlay() {
        PlayPane.setVisible(false);
    }
    @FXML
    protected void BackSettings() {
        SettingsPane.setVisible(false);
    }
    @FXML
    protected void BackInformation() {
        InformationPane.setVisible(false);
    }
    @FXML
    protected void BackHallOfFame() {
        HallOfFamePane.setVisible(false);
    }
    @FXML
    protected void No() {
        QuitPane.setVisible(false);
    }
    Level0 level0;
    Level1 level1;
    Level2 level2;
    Level3 level3;
    Level4 level4;
    @FXML
    protected void PlayLevel0() {
        Level0Button.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.getIcons().add(new Image("Images/Icon.png"));
        stage.setFullScreen(true);
        level0 = new Level0();
        level0.start(stage);
    }
    @FXML
    protected void PlayLevel1() {
        Level1Button.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.getIcons().add(new Image("Images/Icon.png"));
        stage.setFullScreen(true);
        level1 = new Level1();
        level1.start(stage);
    }
    @FXML
    protected void PlayLevel2() {
        Level2Button.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.getIcons().add(new Image("Images/Icon.png"));
        stage.setFullScreen(true);
        level2 = new Level2();
        level2.start(stage);
    }
    @FXML
    protected void PlayLevel3() {
        Level3Button.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.getIcons().add(new Image("Images/Icon.png"));
        stage.setFullScreen(true);
        level3 = new Level3();
        level3.start(stage);
    }
    @FXML
    protected void PlayLevel4() {
        Level4Button.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.getIcons().add(new Image("Images/Icon.png"));
        stage.setFullScreen(true);
        stage.show();
        level4 = new Level4();
        level4.start(stage);
    }
    protected String watch(Integer time) {
        int minutes = time / 60;
        int seconds = time % 60;
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(minutes) + ":" + decimalFormat.format(seconds);
    }
}