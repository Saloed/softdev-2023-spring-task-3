package com.example.demoapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import static com.example.demoapp.MainController.playSound;
import static com.example.demoapp.MainController.workTime;
import static com.example.demoapp.PlannerController.observableList1;

public class SettingsController implements Initializable {

    public Button applyPomodoro;
    public Button applyBreak;
    public Button backToWork;
    public TextField inputRestTime;
    public TextField inputWorkTime;
    public Text warning1;
    public Text warning2;
    public ToggleGroup music;
    public RadioButton alert;
    public RadioButton calm;
    public RadioButton cuckoo;
    public RadioButton classmates;
    public RadioButton triangle;
    public RadioButton victory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputWorkTime.setText(String.valueOf(workTime));
        inputRestTime.setText(String.valueOf(MainController.restTime));

    }

    @FXML
    public void switchToMainScene(ActionEvent event) throws IOException {
        Stage stage = (Stage) backToWork.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainScene.fxml"));
        Parent root = fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Pomodoro Tracker");
        stage.setScene(new Scene(root, 700, 600));
        stage.show();

    }

    @FXML
    protected void changeWorkTime() {
        String getText = inputWorkTime.getText();
        if (getText.matches("[0-9]+")) {
            int input = Integer.parseInt(inputWorkTime.getText());
            if (input > 99 || input < 1) {
                warning1.setText("Please, enter correct pomodoro duration");
            } else {
                warning1.setText("Saved!");
                recountTasks(input);
                workTime = input;
            }
        } else warning1.setText("Please, enter correct pomodoro duration");
    }

    private void recountTasks(int newWorkTime) {
        for (Plan plan : observableList1) {
                int time = plan.getTaskTime() * workTime;
                if (time % newWorkTime == 0) {
                    time = time / newWorkTime;
                } else time = time / newWorkTime + 1;
                plan.setTaskTime(time);
        }
    }

    @FXML
    protected void changeRestTime() {
        String getText = inputRestTime.getText();
        if (!getText.isBlank()) {
            int input = Integer.parseInt(getText);
            if (input > 99 || input < 1) {
                warning2.setText("Please, enter correct break duration");
            } else {
                warning2.setText("Saved!");
                MainController.restTime = input;
            }
        }
    }
    @FXML
    protected void changeSound(ActionEvent event){
        RadioButton btn = (RadioButton) event.getSource();
        String id = btn.getId()+".mp3";
        System.out.println(id);
        String filename = Paths.get("src", "main",
                "resources", "com", "example", "demoapp", "music",
                id).toString();
        playSound(filename);
    }

}
