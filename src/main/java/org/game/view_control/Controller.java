package org.game.view_control;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.game.game.Constants;
import org.game.game.MainLogic;

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
    private Label[][] gameField;
    @FXML
    private TextField sideLength;
    @FXML
    private Pane fieldPane;
    @FXML
    private Label warningLabel;
    private Boolean wasTheFirstMove;


    public void getSide() {
        Constants.SIDE_LENGTH = Integer.parseInt(sideLength.getText());
        Constants.ARRAY_SIDE = 2 * Constants.SIDE_LENGTH - 1;
        Constants.DIAMETER = 700.0 / Constants.ARRAY_SIDE * 0.7;
    }

    @FXML
    public void drawTheField() {
        getSide();
        if (Constants.SIDE_LENGTH > 10 || Constants.SIDE_LENGTH < 3) {
            warningLabel.setVisible(true);
            return;
        }
        start();
        gameField = new Label[Constants.ARRAY_SIDE][Constants.ARRAY_SIDE];

        double x = Constants.DIAMETER * (Constants.ARRAY_SIDE / 2);
        double y = 230.0 - Constants.DIAMETER * Math.sin(Math.PI/3) * (Constants.ARRAY_SIDE / 2);
        for (int q = 0; q < Constants.ARRAY_SIDE; q++) {
            int c = 0;
            for (int r = 0; r < Constants.ARRAY_SIDE; r++) {
                if(MainLogic.getGrid().getState(q, r) != -1) {
                    Label tile = new Label();
                    tile.setPrefSize(Constants.DIAMETER, Constants.DIAMETER);
                    tile.setLayoutX(x);
                    tile.setLayoutY(y);
                    tile.setStyle("-fx-background-color: #ccb69f; -fx-background-radius: 50%; -fx-border-size: "
                                    + Constants.DIAMETER * 0.02 + "; -fx-border-color:  #331b09; -fx-border-radius: 50%");
                    tile.setAlignment(Pos.CENTER);
                    fieldPane.getChildren().add(tile);
                    x = x + Constants.DIAMETER;
                    gameField[q][r] = tile;
                    c++;
                }
            }
            y = y + Math.sin(Math.PI/3) * Constants.DIAMETER;
            if(q < Constants.ARRAY_SIDE / 2) x = x - c * Constants.DIAMETER - Constants.DIAMETER / 2;
            else x = x - c * Constants.DIAMETER + Constants.DIAMETER / 2;
        }
        updateField();
        papa.setOnKeyPressed(this::keyPressed);
        papa.requestFocus();
    }

    @FXML
    public void keyPressed(KeyEvent key) {
        if (!winPane.isVisible() & !failPane.isVisible()) {
            MainLogic.input(key.getCode().toString());
            if (MainLogic.move()) {
                wasTheFirstMove = true;
                updateField();
                scoreLabel.setText(String.valueOf(MainLogic.getScore()));
                if (win) {
                    winPane.setVisible(true);
                }
            } else {
                if (MainLogic.isItEnd()) {
                    failPane.setVisible(true);
                }
            }
        } else if (winPane.isVisible()) {
            restart();
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
                    String style = "-fx-background-color: " + Colors.valueOf("TILE" + valOfTile).getColor() +
                            "; -fx-background-radius: 50%; -fx-text-fill: #fafafa; " +
                            "-fx-font-family: Harpseal; -fx-font-size: " + Constants.DIAMETER * 0.27 +
                            "; -fx-border-size: " + Constants.DIAMETER * 0.02 + "; -fx-border-color:  #331b09; -fx-border-radius: 50%";
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
        wasTheFirstMove = false;
    }

    @FXML
    public void restart() {
        wasTheFirstMove = false;
        failPane.setVisible(false);
        scoreLabel.setText("0");
        drawTheField();
    }

    @FXML
    public void returnToMenu() {
        wasTheFirstMove = false;
        fieldPane.getChildren().clear();
        startPane.setVisible(true);
        sideLength.requestFocus();
        scoreLabel.setText("0");
        warningLabel.setVisible(false);
    }

    @FXML
    public void returnPrevious() {
        if(!wasTheFirstMove) return;
        MainLogic.returnPrevious();
        scoreLabel.setText(String.valueOf(MainLogic.getScore()));
        updateField();
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
