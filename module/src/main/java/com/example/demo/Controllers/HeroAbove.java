package com.example.demo.Controllers;

import com.example.demo.Controllers.SpriteAnimations.SpriteAnimationHeroAbove;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class HeroAbove extends Pane {
    public Point2D velocity = new Point2D(0, 0);
    final ImageView imageView;
    final int columns = 4;
    final int count = 4;
    public int offsetX = 0;
    public int offsetY = 0;
    final int WIDTH = 58;
    final int HEIGHT = 71;
    final int XMAX = 3000;
    final int YMAX = 700;
    final int MIN = 0;
    public SpriteAnimationHeroAbove spriteAnimation;
    public LevelController levelController;

    public HeroAbove(LevelController levelController, ImageView imageView) {
        this.levelController = levelController;
        this.imageView = imageView;
        this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, WIDTH, HEIGHT));
        spriteAnimation = new SpriteAnimationHeroAbove(
                imageView, Duration.millis(500), count, columns, offsetX, offsetY, WIDTH, HEIGHT);
        getChildren().add(imageView);
        this.setTranslateX(10);
        this.setTranslateY(YMAX);
    }

    public void moveX(int value) {
        boolean rightMove = value > 0;
        for (int i = 0; i < Math.abs(value); i++) {
            for (ObstacleAbove obstacle : levelController.listOfObstaclesAbove) {
                if (this.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                    if (getTranslateX() + WIDTH >= obstacle.getTranslateX() && rightMove) {
                        setTranslateX(getTranslateX() - 1);
                    } else if (getTranslateX() == obstacle.getTranslateX() + obstacle.width && !rightMove) {
                        setTranslateX(getTranslateX() + 1);
                    }
                    return;
                }
            }
            if (getTranslateX() < MIN) setTranslateX(MIN);
            if (getTranslateX() > XMAX - WIDTH) setTranslateX(XMAX - WIDTH);
            this.setTranslateX(getTranslateX() + (rightMove ? 1 : -1));
        }
    }
    public void moveY(int value){
        boolean forwardMove = value > 0;
        for (int i = 0; i < Math.abs(value); i++) {
            for (ObstacleAbove obstacle : levelController.listOfObstaclesAbove) {
                if (this.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                    if (getTranslateY() + HEIGHT == obstacle.getTranslateY() && forwardMove) {
                        setTranslateY(getTranslateY() - 1);
                    } else if (getTranslateY() == obstacle.getTranslateY() + obstacle.length && !forwardMove) {
                        setTranslateY(getTranslateY() + 1);
                    }
                    return;
                }
            }
            if (getTranslateY() < MIN) setTranslateY(MIN);
            if (getTranslateY() > YMAX + 100) setTranslateY(YMAX + 100);
            this.setTranslateY(getTranslateY() + (forwardMove ? 1 : -1));
        }
    }
}