package org.game.view_control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.game.game.Constants;
import org.game.game.MainLogic;

import java.util.Objects;

public class Controller {

    @FXML
    public Button restartButton;
    @FXML
    private StackPane papa;
    @FXML
    private Pane winPane;
    @FXML
    public Pane failPane;
    @FXML
    public Pane startPane;
    @FXML
    private Label scoreLabel;
    public static Boolean win = false;
    public TextField[][] gameField;
    @FXML
    public TextField sideLength;
    @FXML
    public Pane fieldPane;


    public void getSide() {
        Constants.SIDE_LENGTH = Integer.parseInt(sideLength.getText());
        Constants.ARRAY_SIDE = 2 * Constants.SIDE_LENGTH - 1;
    }

    @FXML
    public void drawTheField() {
        getSide();
        start();
        gameField = new TextField[Constants.ARRAY_SIDE][Constants.ARRAY_SIDE];

        double x = 150.0;
        double y = 0.0;
        for (int q = 0; q < Constants.ARRAY_SIDE; q++) {
            int c = 0;
            for (int r = 0; r < Constants.ARRAY_SIDE; r++) {
                if(MainLogic.getGrid().getState(q, r) != -1) {
                    TextField tile = new TextField();
                    tile.setPrefSize(100., 100.);
                    tile.setEditable(false);
                    tile.setLayoutX(x);
                    tile.setLayoutY(y);
                    tile.setStyle("-fx-background-color: #ccb69f; -fx-background-radius: 50%; -fx-border-color: black; -fx-border-size: 2; -fx-border-radius: 50%;");
                    fieldPane.getChildren().add(tile);
                    x += 100.;
                    gameField[q][r] = tile;
                    c++;
                }
            }
            y += Math.sin(Math.PI/3) * 100.0;
            if(q < Constants.ARRAY_SIDE/2) x = x - c * 100 - 50;
            else x = x - c * 100 + 50;
        }
        updateField();
        papa.setOnKeyPressed(this::keyPressed);
    }

    @FXML
    public void keyPressed(KeyEvent key) {
        if (!winPane.isVisible() & !failPane.isVisible()) {
            MainLogic.input(key.getCode().toString());
            if (MainLogic.move()) {
                updateField();
                scoreLabel.setText(String.valueOf(MainLogic.score));
                if (win) {
                    winPane.setVisible(true);
                }
            } else {
                if (MainLogic.isItEnd()) {
                    failPane.setVisible(true);
                }
            }
        } else if (winPane.isVisible()) {
            winPane.setVisible(false);
            win = false;
        } else if(failPane.isVisible()) {
            restart();
        }
    }

    public void updateField() {
        for (int q = 0; q < Constants.ARRAY_SIDE; q++) {
            for (int r = 0; r < Constants.ARRAY_SIDE; r++) {
                if(gameField[q][r] != null) {
                    int valOfTile = MainLogic.getGrid().getState(q, r);
                    String style = "-fx-background-color: " + Colors.colors.get(valOfTile) +
                            "; -fx-background-radius: 50%; -fx-padding: 4; -fx-text-fill: #fafafa; " +
                            "-fx-font-family: Harpseal; -fx-font-size: 27.0";
                    gameField[q][r].setStyle(style);
                    if(valOfTile != 0) {
                        gameField[q][r].setText(String.valueOf(valOfTile));
                    }
                    else gameField[q][r].setText("");
                }
            }
        }
    }

    public void start() {
        MainLogic.init();
        for (int i = 0; i < Constants.COUNT_INITIAL_TILES; i++) {
            MainLogic.generateNewTile();
        }
        startPane.setVisible(false);
    }

    @FXML
    public void restart() {
        failPane.setVisible(false);
        drawTheField();
    }

//    public void spawnAnimation(int q, int r) {
//        ScaleTransition anim = new ScaleTransition(Duration.millis(300), gameField[q][r]);
//        anim.setFromX(.1);
//        anim.setToX(1.0);
//        anim.setFromY(.1);
//        anim.setToY(1.0);
//        anim.play();
//    }

//    public void mergeAnimation(int q, int r) {
//        ScaleTransition anim = new ScaleTransition(Duration.millis(300), gameField[q][r]);
//        anim.setFromX(1.2);
//        anim.setToX(1.0);
//        anim.setFromY(1.2);
//        anim.setToY(1.0);
//        anim.play();
//    }

}
