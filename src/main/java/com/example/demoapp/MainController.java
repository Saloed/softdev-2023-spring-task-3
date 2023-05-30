package com.example.demoapp;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import static com.example.demoapp.PlannerController.observableList1;
import static com.example.demoapp.PlannerController.observableList2;

public class MainController implements Initializable {
    public Menu settingsButton;
    public Button settings2;
    public Text taskName;
    public Button start;
    public Button stop;
    public Button pause;

    public Button plannerButton;
    public Circle circle;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stop.setDisable(true);
        time.setText(String.format("%02d:%02d", this.min, this.sec));
        setTask();
    }

    void setTask() {
        if (!observableList1.isEmpty()) {
            taskName.setText(observableList1.get(0).getTaskDesc());
        } else taskName.setText("");
    }

    @FXML
    public void switchToPlanner() throws IOException {
        Stage stage = (Stage) settings2.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("planner.fxml"));
        Parent root = fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Planner");
        stage.setScene(new Scene(root, 700, 600));
        stage.show();
    }

    @FXML
    public void switchToSettings() throws IOException {
        Stage stage = (Stage) settings2.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings.fxml"));
        Parent root = fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Settings");
        stage.setScene(new Scene(root, 700, 600));
        stage.show();
    }

    public static int workTime = 1;
    public static int restTime = 1;

    boolean isGreen = false;
    int sec = 0;
    int min = workTime;

    @FXML
    private Text time;
    public Timeline timeline;

    void backToStart() {
        timeline.stop();
        pause.setDisable(true);
        start.setDisable(false);
        repaintToRed();
        this.min = workTime;
        this.sec = 0;
        time.setText(String.format("%02d:%02d", this.min, 0));
        stop.setDisable(true);
        start.setText("start");
        stop.setText("stop");
        setTask();
    }

    @FXML
    protected void onStartButtonClick() {
        stop.setDisable(false);
        start.setDisable(true);
        pause.setDisable(false);
        start.setText("pause");
        stop.setText("stop");
        this.timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        e -> {

                            if (this.sec <= 0 && this.min != 0) {
                                this.min--;
                                this.sec = 59;
                            }

                            time.setText(String.format("%02d:%02d", this.min, this.sec));
                            if (this.min == 0 && this.sec <= 0) {
                                playSound(filename);
                                if (!isGreen) {
                                    timeline.stop();
                                    rest(true);
                                } else {
                                    backToStart();
                                }
                            }
                            this.sec--;

                        }
                ));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }



    public void rest(boolean isDone) {
        int work;
        if (isDone) {
            work = workTime;
        } else work = workTime-this.min + 1;
        this.min = restTime;
        this.sec = 0;
        timeline.play();
        time.setText(String.format("%02d:%02d", restTime, 0));
        taskName.setText("Take a short break!");
        repaintToGreen();
        if (observableList1.size() != 0) {
            Plan plan = observableList1.get(0);
            if (plan.getTaskTime() == 1) {
                observableList1.remove(0);
            } else {
                plan.setTaskTime(plan.getTaskTime() - 1);
            }
            observableList2.add(new Plan(plan.getTaskType(), plan.getTaskDesc(), work));
        } else observableList2.add(new Plan("", "(no description)", work));

    }

    @FXML
    protected void onStopButtonClick() {
        if (stop.textProperty().getValue().equals("stop")) {
            if (isGreen) {
                repaintToRed();
                taskName.setText("");
            }
            start.setDisable(false);
            backToStart();
        }
        if (stop.textProperty().getValue().equals("done")) {
            if (isGreen) {
                repaintToRed();
                this.min = workTime;
                this.sec = 0;
                time.setText(String.format("%02d:%02d", workTime, 0));
                stop.setDisable(true);
                start.setText("start");
                stop.setText("stop");
                setTask();

            } else {
                rest(false);
                playSound(filename);
                start.setDisable(true);
                start.setText("pause");
                stop.setText("stop");
            }
        }
    }

    @FXML
    protected void onPauseButtonClick() {
        start.setDisable(false);
        start.setText("continue");
        stop.setText("done");
        timeline.stop();
    }

    void repaintToGreen() {
        circle.setFill(Color.web("#46EA69"));
        isGreen = true;
        start.setStyle("-fx-background-color: #46EA69; -fx-border-color: white;");
        stop.setStyle("-fx-background-color: #46EA69; -fx-border-color: white;");
        pause.setStyle("-fx-background-color: #46EA69; -fx-border-color: white;");
    }

    void repaintToRed() {
        circle.setFill(Color.web("#FF0000"));
        isGreen = false;
        start.setStyle("-fx-background-color: red; -fx-border-color: white;");
        stop.setStyle("-fx-background-color: red; -fx-border-color: white;");
        pause.setStyle("-fx-background-color: red; -fx-border-color: white;");
    }


    static String filename = Paths.get("src", "main",
            "resources", "com", "example", "demoapp", "music",
            "calm.mp3").toString();


    static void playSound(String filename) {
        Media media = new javafx.scene.media.Media(new File(filename).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(1);
        mediaPlayer.play();
    }


}