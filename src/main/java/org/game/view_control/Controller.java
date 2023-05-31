package org.game.view_control;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.game.game.Constants;
import org.game.game.FieldOptions;
import org.game.game.MainLogic;

public class Controller {

    @FXML
    public Button restartButton;
    @FXML
    private Pane winPane;
    @FXML
    public Pane failPane;
    @FXML
    public Pane startPane;
    @FXML
    private Label scoreLabel;
    public Boolean win = false;
    private Label[][] gameField;
    @FXML
    private TextField sideLength;
    @FXML
    private Pane fieldPane;
    @FXML
    private Label warningLabel;
    private Boolean wasTheFirstMove;
    private MainLogic logic;
    private int[][] anim;
    private final FieldOptions fieldOptions = new FieldOptions();

    public void setAnim(int i, int j, int value) {
        anim[i][j] = value;
    }

    @FXML
    public void enterText(KeyEvent key) {
        if(key.getCode().toString().equals("ENTER")) {
            drawTheField();
        }
    }

    public void getSide() {
        if(sideLength.getText().equals("")) {
            fieldOptions.setSideLength(Integer.parseInt(sideLength.getPromptText()));
            fieldOptions.setArraySide(2 * fieldOptions.getSideLength() - 1);
            fieldOptions.setDiameter(700.0 / fieldOptions.getArraySide() * 0.7);
        } else {
            if (!sideLength.getText().matches("[0-9]")) {
                warningAnimation();
            } else {
                fieldOptions.setSideLength(Integer.parseInt(sideLength.getText()));
                fieldOptions.setArraySide(2 * fieldOptions.getSideLength() - 1);
                fieldOptions.setDiameter(700.0 / fieldOptions.getArraySide() * 0.7);
            }
        }
    }

    @FXML
    public void drawTheField() {
        getSide();
        if (fieldOptions.getSideLength() > 9 || fieldOptions.getSideLength() < 3) {
            warningAnimation();
            return;
        }
        start();
        gameField = new Label[fieldOptions.getArraySide()][fieldOptions.getArraySide()];

        double x = fieldOptions.getDiameter() * (double)(fieldOptions.getArraySide() / 2);
        double y = 230.0 - fieldOptions.getDiameter() * Math.sin(Math.PI/3) * (double)(fieldOptions.getArraySide() / 2);
        for (int q = 0; q < fieldOptions.getArraySide(); q++) {
            int c = 0;
            for (int r = 0; r < fieldOptions.getArraySide(); r++) {
                if(logic.getGrid().getState(q, r) != -1) {
                    Label tile = new Label();
                    Pane tileCell = new StackPane();
                    tileCell.setPrefSize(fieldOptions.getDiameter(), fieldOptions.getDiameter());
                    tileCell.setLayoutX(x);
                    tileCell.setLayoutY(y);
                    tileCell.setStyle("-fx-background-color: #ccb69f; -fx-background-radius: 50%; -fx-border-width: " +
                            fieldOptions.getDiameter() * 0.05 + "; -fx-border-color: #331b09; -fx-border-radius: 50%;");
                    tile.setPrefSize(fieldOptions.getDiameter(), fieldOptions.getDiameter());
                    tile.setStyle("-fx-background-color: transparent; -fx-background-radius: 50%;");
                    tile.setAlignment(Pos.CENTER);
                    tileCell.getChildren().add(tile);
                    fieldPane.getChildren().add(tileCell);
                    x = x + fieldOptions.getDiameter();
                    gameField[q][r] = tile;
                    c++;
                }
            }
            y = y + Math.sin(Math.PI/3) * fieldOptions.getDiameter();
            if(q < fieldOptions.getArraySide() / 2) x = x - c * fieldOptions.getDiameter() - fieldOptions.getDiameter() / 2;
            else x = x - c * fieldOptions.getDiameter() + fieldOptions.getDiameter() / 2;
        }
        updateField();
        fieldPane.setOnKeyPressed(this::keyPressed);
        fieldPane.requestFocus();
    }

    @FXML
    public void keyPressed(KeyEvent key) {
        if (!winPane.isVisible() & !failPane.isVisible()) {
            logic.input(key.getCode().toString());
            if (logic.move()) {
                wasTheFirstMove = true;
                updateField();
                scoreLabel.setText(String.valueOf(logic.getScore()));
                if (win) {
                    winPane.setVisible(true);
                }
            } else {
                if (logic.isItEnd()) {
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
        for (int q = 0; q < fieldOptions.getArraySide(); q++) {
            for (int r = 0; r < fieldOptions.getArraySide(); r++) {
                if(gameField[q][r] != null) {
                    int valOfTile = logic.getGrid().getState(q, r);
                    String style = "-fx-background-color: " + Colors.valueOf("TILE" + valOfTile).getColor() +
                            "; -fx-background-radius: 50%; -fx-text-fill: #fafafa; " +
                            "-fx-font-family: Harpseal; -fx-font-size: " + fieldOptions.getDiameter() * 0.27;
                    gameField[q][r].setStyle(style);
                    if(valOfTile != 0) {
                        gameField[q][r].setText(String.valueOf(valOfTile));
                        if(anim[q][r] == 1) {
                            spawnAnimation(q, r);
                        }
                    }
                    else gameField[q][r].setText("");
                }
            }
        }
        anim = new int[fieldOptions.getArraySide()][fieldOptions.getArraySide()];
    }

    public void start() {
        anim = new int[fieldOptions.getArraySide()][fieldOptions.getArraySide()];
        logic = new MainLogic();
        logic.init(fieldOptions, this);
        for (int i = 0; i < Constants.COUNT_INITIAL_TILES; i++) {
            logic.generateNewTile();
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
        fieldOptions.setSideLength(0);
    }

    @FXML
    public void returnPrevious() {
        if(!wasTheFirstMove) return;
        logic.returnPrevious();
        scoreLabel.setText(String.valueOf(logic.getScore()));
        fieldPane.requestFocus();
        updateField();
    }

    public void warningAnimation() {
        warningLabel.setVisible(true);
        ScaleTransition anim = new ScaleTransition(Duration.millis(150), warningLabel);
        anim.setFromY(.7);
        anim.setFromX(.7);
        anim.setToY(1.0);
        anim.setToX(1.0);
        anim.play();
    }

    public void spawnAnimation(int q, int r) {
        ScaleTransition anim = new ScaleTransition(Duration.millis(200), gameField[q][r]);
        anim.setFromX(.3);
        anim.setToX(1.0);
        anim.setFromY(.3);
        anim.setToY(1.0);
        anim.play();
    }

}
