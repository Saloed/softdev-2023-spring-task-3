package com.example.demo.Controllers;

import com.example.demo.Application;
import com.example.demo.Controllers.Levels.*;
import com.example.demo.Controllers.SpriteAnimations.SpriteAnimationCoin;
import com.example.demo.Controllers.SpriteAnimations.SpriteAnimationHero;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class LevelController {
    public Pane appRoot;
    public Pane gameRoot;
    public Pane extraGameRoot;
    public ImageView backgroundSkyImage;
    public ImageView backgroundGroundImage;
    public List<Obstacle> listOfObstacles;
    public List<ObstacleAbove> listOfObstaclesAbove;
    public List<Integer> list;
    public Level0 level0;
    public Level1 level1;
    public Level2 level2;
    public Level3 level3;
    public Level4 level4;
    public Hero hero;
    public Coin coin;
    public HeroAbove heroAbove;
    public CoinAbove coinAbove;
    public VBox unable = new VBox();
    private VBox vBox;
    public LevelController() {}
    public Parent createContent() {
        backgroundSkyImage.setFitHeight(700);
        backgroundSkyImage.setFitWidth(3000);
        backgroundGroundImage.setFitHeight(865);
        backgroundGroundImage.setFitWidth(3000);
        gameRoot.setPrefSize(700, 700);
        gameRoot.getChildren().addAll(backgroundSkyImage, hero, coin);
        extraGameRoot.setPrefSize(700, 700);
        extraGameRoot.getChildren().addAll(backgroundGroundImage, heroAbove, coinAbove);
        extraGameRoot.setVisible(false);
        ImageView imageViewHero = new ImageView();
        imageViewHero.setViewport(new Rectangle2D(hero.offsetX, hero.offsetY, hero.WIDTH, hero.HEIGHT));
        Animation animationHero = new SpriteAnimationHero(
                imageViewHero, Duration.millis(500),
                hero.count, hero.columns, hero.offsetX,
                hero.offsetY, hero.WIDTH, hero.HEIGHT
        );
        animationHero.setCycleCount(Animation.INDEFINITE);
        animationHero.play();
        ImageView imageViewHeroAbove = new ImageView();
        imageViewHeroAbove.setViewport(new Rectangle2D(hero.offsetX, hero.offsetY, hero.WIDTH, hero.HEIGHT));
        Animation animationHeroAbove = new SpriteAnimationHero(
                imageViewHero, Duration.millis(500),
                hero.count, hero.columns, hero.offsetX,
                hero.offsetY, hero.WIDTH, hero.HEIGHT
        );
        animationHeroAbove.setCycleCount(Animation.INDEFINITE);
        animationHeroAbove.play();
        ImageView imageViewCoin = new ImageView();
        imageViewCoin.setViewport(new Rectangle2D(coin.offsetX, 0, coin.width, coin.height));
        Animation animationCoin = new SpriteAnimationCoin(
                imageViewCoin, Duration.millis(500),
                coin.columns, coin.offsetX, coin.width, coin.height
        );
        animationCoin.setCycleCount(Animation.INDEFINITE);
        animationCoin.play();
        Label text = new Label("You cannot change dimension while the character is in danger of getting stuck " +
                "in the walls!");
        text.setTextFill(Color.WHITE);
        Font font = Font.font("Leelawadee Bold", 32);
        text.setFont(font);
        unable.setAlignment(Pos.CENTER);
        unable.getChildren().add(text);
        unable.setLayoutY(810);
        unable.setOpacity(0);
        gameRoot.getChildren().addAll(listOfObstacles);
        extraGameRoot.getChildren().addAll(listOfObstaclesAbove);
        gameRoot.getChildren().add(unable);
        appRoot.getChildren().addAll(gameRoot, extraGameRoot);
        return appRoot;
    }
    public int currentLevel;
    public int coordinateX;
    public int coordinateY;
    public int coordinateXAbove;
    public int coordinateYAbove;
    public int coordinateCoinX;
    public int coordinateCoinY;
    public int coordinateCoinXAbove;
    public int coordinateCoinYAbove;
    public int countOfTryingToChangeDimension = 0;
    final int COIN_SIDE = 58;
    final int SHIFT_START = 350;
    final int SHIFT_END = 1815;
    public Rectangle blackScreen = new Rectangle(3000, 1000, Color.BLACK);
    public Rectangle darkMode = new Rectangle(3000, 1000, Color.BLACK);
    private boolean isPaused = false;
    public boolean canDimensionBeChanged;
    private boolean canPauseBeReleased = false;
    private boolean isFinished = false;
    public boolean isDimensionChanged = false;
    public boolean stop = false;
    public HashMap<KeyCode, Boolean> map = new HashMap<>();
    private AnimationTimer timer;
    public void update() throws IOException {
        if (!isPaused && !isFinished && !isDimensionChanged && !canPauseBeReleased && !stop) {
            coordinateX = (int) hero.getTranslateX();
            coordinateY = (int) hero.getTranslateY();
            coordinateCoinX = (int) coin.getTranslateX();
            coordinateCoinY = (int) coin.getTranslateY();

            coin.spriteAnimation.play();
            if (hero.velocity.getY() < 5) {
                hero.velocity = hero.velocity.add(0, 1);
            }
            hero.moveX((int) hero.velocity.getX());
            hero.moveY((int) hero.velocity.getY());

            canDimensionBeChanged = true;
            listOfObstacles.forEach(e -> {
                int coordinateXOfObstacles = (int) e.getTranslateX();
                if (e.width != 3000 && coordinateX + hero.WIDTH >= coordinateXOfObstacles
                        && coordinateX <= coordinateXOfObstacles + e.width) canDimensionBeChanged = false;
            });

            if (isPressed(KeyCode.SPACE)) hero.jump();
            if (isPressed(KeyCode.A)) {
                hero.moveX(-5);
                hero.spriteAnimation.setOffsetYhero(141);
                hero.spriteAnimation.play();
            } else if (isPressed(KeyCode.D)) {
                hero.moveX(5);
                hero.spriteAnimation.setOffsetYhero(212);
                hero.spriteAnimation.play();
            } else if (isPressed(KeyCode.R) && canDimensionBeChanged) {
                hero.spriteAnimation.stop();
                coin.spriteAnimation.stop();
                changeDimension();
                extraGameRoot.getChildren().add(unable);
            } else if (isPressed(KeyCode.R) && !canDimensionBeChanged && countOfTryingToChangeDimension <= 15) {
                unableOfChangingDimension();
            } else hero.spriteAnimation.stop();
            if (isPressed(KeyCode.P)) {
                pause();
                hero.spriteAnimation.stop();
                coin.spriteAnimation.stop();
                unable.setVisible(false);
            }
            if (coordinateX + COIN_SIDE >= coordinateCoinX && coordinateX - COIN_SIDE <= coordinateCoinX &&
                    coordinateY + COIN_SIDE >= coordinateCoinY && coordinateY - COIN_SIDE <= coordinateCoinY) {
                finish();
                hero.spriteAnimation.stop();
                coin.spriteAnimation.stop();
                unable.setVisible(false);
            }
        }
        if (!isPaused && !isFinished && isDimensionChanged && !canPauseBeReleased && !stop) {
            coordinateXAbove = (int) heroAbove.getTranslateX();
            coordinateYAbove = (int) heroAbove.getTranslateY();
            coordinateCoinXAbove = (int) coinAbove.getTranslateX();
            coordinateCoinYAbove = (int) coinAbove.getTranslateY();

            coinAbove.spriteAnimation.play();
            heroAbove.moveX((int) heroAbove.velocity.getX());
            heroAbove.moveY((int) heroAbove.velocity.getY());

            canDimensionBeChanged = true;
            listOfObstaclesAbove.forEach(e -> {
                int coordinateXOfObstacleAbove = (int) e.getTranslateX();
                if (coordinateXAbove + hero.WIDTH >= coordinateXOfObstacleAbove
                        && coordinateXAbove <= coordinateXOfObstacleAbove + e.width) {
                    canDimensionBeChanged = false;
                }
            });

            if (isPressed(KeyCode.A)) {
                heroAbove.moveX(-5);
                heroAbove.spriteAnimation.setOffsetYHeroAbove(141);
                heroAbove.spriteAnimation.play();
            } else if (isPressed(KeyCode.D)) {
                heroAbove.moveX(5);
                heroAbove.spriteAnimation.setOffsetYHeroAbove(212);
                heroAbove.spriteAnimation.play();
            } else if (isPressed(KeyCode.W)) {
                heroAbove.moveY(-5);
                heroAbove.spriteAnimation.setOffsetYHeroAbove(71);
                heroAbove.spriteAnimation.play();
            } else if (isPressed(KeyCode.S)) {
                heroAbove.moveY(5);
                heroAbove.spriteAnimation.setOffsetYHeroAbove(0);
                heroAbove.spriteAnimation.play();
            } else if (isPressed(KeyCode.R) && canDimensionBeChanged) {
                heroAbove.spriteAnimation.stop();
                coin.spriteAnimation.stop();
                hero.setTranslateY(550);
                changeDimensionBack();
                gameRoot.getChildren().add(unable);
            } else if (isPressed(KeyCode.R) && !canDimensionBeChanged && countOfTryingToChangeDimension <= 15) {
                unableOfChangingDimension();
            } else heroAbove.spriteAnimation.stop();
            if (isPressed(KeyCode.P)) {
                pause();
                heroAbove.spriteAnimation.stop();
                coin.spriteAnimation.stop();
                unable.setVisible(false);
            }
            if (coordinateXAbove + COIN_SIDE >= coordinateCoinXAbove &&
                coordinateXAbove - COIN_SIDE <= coordinateCoinXAbove &&
                coordinateYAbove + COIN_SIDE >= coordinateCoinYAbove &&
                coordinateYAbove - COIN_SIDE <= coordinateCoinYAbove) {
                finish();
                heroAbove.spriteAnimation.stop();
                coinAbove.spriteAnimation.stop();
                unable.setVisible(false);
            }
        }
        if (isPaused && canPauseBeReleased) {
            if (isPressed(KeyCode.P)) {
                isPaused = false;
                darkMode.setOpacity(0.7);
                if (!isDimensionChanged) gameRoot.getChildren().remove(vBox);
                if (isDimensionChanged) extraGameRoot.getChildren().remove(vBox);
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), darkMode);
                fadeTransition.setFromValue(0.7);
                fadeTransition.setToValue(0);
                fadeTransition.setOnFinished(event -> {
                    if (!isDimensionChanged) gameRoot.getChildren().remove(darkMode);
                    if (isDimensionChanged) extraGameRoot.getChildren().remove(darkMode);
                    canPauseBeReleased = false;
                    startTimer();
                });
                fadeTransition.play();
                unable.setVisible(true);
            }
        }
        if (isDimensionChanged) {
            int offset = coordinateXAbove;
            if (offset > SHIFT_START && offset < SHIFT_END) {
                extraGameRoot.setLayoutX(SHIFT_START - offset);
                unable.setLayoutX(-270 + offset);
            } else if (offset >= SHIFT_END) {
                extraGameRoot.setLayoutX(-1463);
                unable.setLayoutX(1545);
            }
            else {
                extraGameRoot.setLayoutX(0);
                unable.setLayoutX(80);
            }
        } else {
            int offset = coordinateX;
            if (offset > SHIFT_START && offset < SHIFT_END) {
                gameRoot.setLayoutX(SHIFT_START - offset);
                unable.setLayoutX(-270 + offset);
            } else if (offset >= SHIFT_END) {
                gameRoot.setLayoutX(-1463);
                unable.setLayoutX(1545);
            }
            else {
                gameRoot.setLayoutX(0);
                unable.setLayoutX(80);
            }
        }
    }
    public boolean isPressed(KeyCode key) {
        return map.getOrDefault(key, false);
    }
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setOnKeyPressed(event -> map.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> map.put(event.getCode(), false));
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    update();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.start();
        if (currentLevel == 0) beginning();
        else loading();
    }
    public void pause() {
        isPaused = true;
        canPauseBeReleased = false;
        darkMode.setOpacity(0.0);
        stopTimer();
        if (!isDimensionChanged) gameRoot.getChildren().add(darkMode);
        if (isDimensionChanged) extraGameRoot.getChildren().add(darkMode);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), darkMode);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(0.7);
        fadeTransition.setOnFinished(event -> {
            vBox = new VBox(20);
            Button buttonRestart = createButton("RESTART");
            Button buttonContinue = createButton("CONTINUE");
            Button buttonMenu = createButton("MENU");
            Label label = new Label("PAUSE");
            label.setTextFill(Color.GRAY);
            Font font = Font.font("Gill Sans Ultra Bold", 90);
            label.setFont(font);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().addAll(label, buttonContinue, buttonRestart, buttonMenu);
            if (!isDimensionChanged) {
                if (coordinateX < SHIFT_START) {
                    vBox.setLayoutX(580);
                } else if (coordinateX > 1810) {
                    vBox.setLayoutX(2030);
                } else vBox.setLayoutX(coordinateX + 230);
            } else {
                if (coordinateXAbove < SHIFT_START) {
                    vBox.setLayoutX(580);
                } else if (coordinateXAbove > 1810) {
                    vBox.setLayoutX(2030);
                } else vBox.setLayoutX(coordinateXAbove + 230);
            }
            vBox.setLayoutY(190);
            if (!isDimensionChanged) gameRoot.getChildren().add(vBox);
            if (isDimensionChanged) extraGameRoot.getChildren().add(vBox);
            buttonContinue.setOnAction(e -> {
                isPaused = false;
                startTimer();
                darkMode.setOpacity(0.7);
                if (!isDimensionChanged) gameRoot.getChildren().remove(vBox);
                if (isDimensionChanged) extraGameRoot.getChildren().remove(vBox);
                FadeTransition fadeTransition2 = new FadeTransition(Duration.millis(300), darkMode);
                fadeTransition2.setFromValue(0.7);
                fadeTransition2.setToValue(0.0);
                fadeTransition2.setOnFinished(event2 -> {
                    if (!isDimensionChanged) gameRoot.getChildren().remove(darkMode);
                    if (isDimensionChanged) extraGameRoot.getChildren().remove(darkMode);
                    canPauseBeReleased = false;
                });
                fadeTransition2.play();
            });
            buttonRestart.setOnAction(e -> {
                isFinished = true;
                timer.stop();
                Stage stage = newStage();
                if (currentLevel == 0) {
                    level0 = new Level0();
                    level0.start(stage);
                }
                else if (currentLevel == 1) {
                    level1 = new Level1();
                    level1.start(stage);
                }
                else if (currentLevel == 2) {
                    level2 = new Level2();
                    level2.start(stage);
                }
                else if (currentLevel == 3) {
                    level3 = new Level3();
                    level3.start(stage);
                } else if (currentLevel == 4) {
                    level4 = new Level4();
                    level4.start(stage);
                }
                isFinished = false;
            });
            buttonMenu.setOnAction(e -> {
                isFinished = true;
                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainMenu.fxml"));
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load(), 700, 500);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Stage stage = newStage();
                stage.setScene(scene);
                stage.show();
                isPaused = false;
                canPauseBeReleased = false;
                isFinished = false;
            });
            canPauseBeReleased = true;
        });
        fadeTransition.play();
    }
    public void finish() throws IOException {
        isFinished = true;
        timer.stop();
        stopTimer();
        record();
        Button buttonRestart = createButton("RESTART");
        Button buttonNextLevel = createButton("NEXT LEVEL");
        Button buttonMenu = createButton("MENU");
        Label label = new Label("CONGRATS!");
        label.setTextFill(Color.GRAY);
        Font font = Font.font("Gill Sans Ultra Bold", 90);
        label.setFont(font);
        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        if (currentLevel != 4) {
            vBox.getChildren().addAll(label, buttonNextLevel, buttonRestart, buttonMenu);
            vBox.setLayoutY(190);
        } else {
            vBox.getChildren().addAll(label, buttonRestart, buttonMenu);
            vBox.setLayoutY(250);
        }
        vBox.setLayoutX(1900);
        blackScreen.setOpacity(0.7);
        if (!isDimensionChanged) {
            gameRoot.getChildren().addAll(blackScreen, vBox);
        } else {
            extraGameRoot.getChildren().addAll(blackScreen, vBox);
        }
        buttonMenu.setOnAction(e -> {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainMenu.fxml"));
            Scene scene;
            try {
                scene = new Scene(fxmlLoader.load(), 700, 500);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Stage stage = newStage();
            stage.setScene(scene);
            stage.show();
            isFinished = false;
        });
        buttonRestart.setOnAction(e -> {
            Stage stage = newStage();
            if (currentLevel == 0) {
                level0 = new Level0();
                level0.start(stage);
            }
            else if (currentLevel == 1) {
                level1 = new Level1();
                level1.start(stage);
            }
            else if (currentLevel == 2) {
                level2 = new Level2();
                level2.start(stage);
            }
            else if (currentLevel == 3) {
                level3 = new Level3();
                level3.start(stage);
            } else if (currentLevel == 4) {
                level4 = new Level4();
                level4.start(stage);
            }
            isFinished = false;
        });
        buttonNextLevel.setOnAction(e -> {
            Stage stage = newStage();
            if (currentLevel == 0) {
                level1 = new Level1();
                level1.start(stage);
            }
            else if (currentLevel == 1) {
                level2 = new Level2();
                level2.start(stage);
            }
            else if (currentLevel == 2) {
                level3 = new Level3();
                level3.start(stage);
            }
            else if (currentLevel == 3) {
                level4 = new Level4();
                level4.start(stage);
            }
            isFinished = false;
        });
    }
    public Button createButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(300);
        button.setPrefHeight(100);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: gray;" +
                " -fx-border-WIDTH: 2px; -fx-border-radius: 25;");
        button.setTextFill(Color.GRAY);
        Font font = Font.font("Gill Sans Ultra Bold", 32);
        button.setFont(font);
        return button;
    }
    public void changeDimension() {
        stop = true;
        darkMode.setOpacity(0);
        gameRoot.getChildren().remove(darkMode);
        gameRoot.getChildren().add(darkMode);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), darkMode);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        heroAbove.setTranslateX(coordinateX);
        fadeTransition.setOnFinished(event -> {
            gameRoot.setVisible(false);
            extraGameRoot.setVisible(true);
            stop = false;
            isDimensionChanged = true;
        });
        fadeTransition.play();
    }
    public void changeDimensionBack() {
        stop = true;
        darkMode.setOpacity(0);
        extraGameRoot.getChildren().remove(darkMode);
        extraGameRoot.getChildren().add(darkMode);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), darkMode);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        hero.setTranslateX(coordinateXAbove);
        fadeTransition.setOnFinished(event -> {
            extraGameRoot.setVisible(false);
            gameRoot.setVisible(true);
            stop = false;
            isDimensionChanged = false;
        });
        fadeTransition.play();
    }
    public Stage newStage() {
        timer.stop();
        gameRoot.getChildren().clear();
        extraGameRoot.getChildren().clear();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.setFullScreen(true);
        stage.getIcons().add(new Image("Images/Icon.png"));
        isDimensionChanged = false;
        list.clear();
        listOfObstacles.clear();
        listOfObstaclesAbove.clear();
        appRoot.getChildren().clear();
        appRoot.getScene().getWindow().hide();
        return stage;
    }
    public void unableOfChangingDimension() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), unable);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(0.7);
        fadeTransition.setOnFinished(event -> {
            FadeTransition fadeTransition2 = new FadeTransition(Duration.millis(1500), unable);
            fadeTransition2.setFromValue(0.7);
            fadeTransition2.setToValue(0.7);
            fadeTransition2.setOnFinished(event2 -> {
                FadeTransition fadeTransition3 = new FadeTransition(Duration.millis(500), unable);
                fadeTransition3.setFromValue(0.7);
                fadeTransition3.setToValue(0);
                fadeTransition3.setOnFinished(event3 -> countOfTryingToChangeDimension++);
                fadeTransition3.play();
            });
            fadeTransition2.play();
        });
        fadeTransition.play();
    }
    public void beginning() {
        timer.stop();
        isPaused = true;
        darkMode.setOpacity(0.0);
        gameRoot.getChildren().add(darkMode);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), darkMode);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(0.7);
        fadeTransition.setOnFinished(event -> {
            vBox = new VBox(20);
            Button buttonOK = createButton("OK");
            Label label = new Label("CONTROLS:");
            Label text = new Label("""
                                            To begin you need to know some basic control keys:
                    To move right - press D; to move left - A, to move forward - W, to move back - S.
                    If you want your character to jump - press SPACE. P is responsible for the pause.
                      Secret key which will help you to finish the game is R. It changes dimensions.
                                                          Now you know everything. Good luck!""");
            label.setTextFill(Color.GRAY);
            text.setTextFill(Color.GRAY);
            Font font = Font.font("Gill Sans Ultra Bold", 90);
            Font font2 = Font.font("Leelawadee Bold", 32);
            label.setFont(font);
            text.setFont(font2);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().addAll(label, text, buttonOK);
            vBox.setLayoutY(230);
            vBox.setLayoutX(140);
            gameRoot.getChildren().add(vBox);
            buttonOK.setOnAction(e -> {
                darkMode.setOpacity(0.7);
                gameRoot.getChildren().remove(vBox);
                FadeTransition fadeTransition2 = new FadeTransition(Duration.millis(300), darkMode);
                fadeTransition2.setFromValue(0.7);
                fadeTransition2.setToValue(0.0);
                fadeTransition2.setOnFinished(event2 -> {
                    gameRoot.getChildren().remove(darkMode);
                    timer.start();
                    loading();
                    isPaused = false;
                });
                fadeTransition2.play();
            });
        });
        fadeTransition.play();
    }
    private Timer screenTimer;
    private int secondsPassed;
    public Label timerLabel;
    private DecimalFormat decimalFormat;
    public void updateTimer() {
        int minutes = secondsPassed / 60;
        int seconds = secondsPassed % 60;
        appRoot.getChildren().remove(timerLabel);
        timerLabel = new Label();
        timerLabel.setTextFill(Color.GRAY);
        Font font = Font.font("Gill Sans Ultra Bold", 40);
        timerLabel.setFont(font);
        timerLabel.setTranslateX(20);
        timerLabel.setTranslateY(20);
        timerLabel.setText(decimalFormat.format(minutes) + ":" + decimalFormat.format(seconds));
        appRoot.getChildren().add(timerLabel);
    }
    public void startTimer() {
        appRoot.getChildren().remove(timerLabel);
        timerLabel = new Label();
        screenTimer = new Timer();
        screenTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsPassed++;
                Platform.runLater(() -> updateTimer());
            }
        }, 0, 1000);
    }
    public void stopTimer() {
        screenTimer.cancel();
    }
    public void loading() {
        secondsPassed = -1;
        decimalFormat = new DecimalFormat("00");
        startTimer();
        timerLabel.setTranslateX(20);
        timerLabel.setTranslateY(20);
    }
    public void record() throws IOException {
        boolean wasListChanged = false;
        List<String> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader(
                new FileReader("module/src/main/resources/completedLevels.txt"));
        String just;
        while ((just = reader.readLine()) != null) list.add(just);
        for (String line : list) {
            int number = Integer.parseInt(line);
            if (list.indexOf(line) == currentLevel && (number == 0 || secondsPassed < number)) {
                list.set(currentLevel, Integer.toString(secondsPassed));
                wasListChanged = true;
            }
        }
        if (wasListChanged) {
            int count = 1;
            int size = list.size();
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("module/src/main/resources/completedLevels.txt"));
            for (String line : list) {
                writer.write(line);
                if (count != size) writer.write("\n");
                count++;
            }
            writer.close();
        }
        reader.close();
    }
}