package com.example.demo.Controllers;

import com.example.demo.Controllers.SpriteAnimations.SpriteAnimationHero;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;



public class Hero extends Pane {
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
    public boolean canJump = true;
    public SpriteAnimationHero spriteAnimation;
    public LevelController levelController;

    public Hero(LevelController levelController, ImageView imageView) {
            this.imageView = imageView;
            this.levelController = levelController;
            this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, WIDTH, HEIGHT));
            spriteAnimation = new SpriteAnimationHero(
                    imageView, Duration.millis(500), count, columns, offsetX, offsetY, WIDTH, HEIGHT);
            getChildren().add(imageView);
            this.setTranslateX(10);
            this.setTranslateY(480);
    }
    public void moveY (int value) {
        boolean moveDown = value > 0;
        for (int i = 0; i < Math.abs(value); i++) {
            for (Obstacle obstacle : levelController.listOfObstacles) {
                if (this.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                    if (moveDown) {
                        setTranslateY(getTranslateY() - 1);
                        canJump = true;
                    } else {
                        setTranslateY(getTranslateY() + 2);
                    }
                    return;
                } else canJump = false;
            }
            if (getTranslateY() < MIN) setTranslateY(MIN);
            if (getTranslateX() > XMAX - WIDTH) setTranslateX(XMAX - WIDTH);
            if (getTranslateY() > YMAX) setTranslateY(YMAX);
            this.setTranslateY(getTranslateY() + (moveDown ? 1 : -1));
        }
    }

        public void moveX (int value) {
        boolean rightMove = value > 0;
        for (int i = 0; i < Math.abs(value); i++) {
            for (Obstacle obstacle : levelController.listOfObstacles) {
                if (this.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                    if (getTranslateX() + WIDTH == obstacle.getTranslateX() && rightMove) {
                        setTranslateX(getTranslateX() - 1);
                    } else if (getTranslateX() == obstacle.getTranslateX() + obstacle.width && !rightMove) {
                        setTranslateX(getTranslateX() + 1);
                    }
                    return;
                }
            }
            if (getTranslateX() < MIN) setTranslateX(MIN);
            this.setTranslateX(getTranslateX() + (rightMove ? 1 : -1));
        }
    }
        public void jump () {
        if (canJump) {
            levelController.hero.velocity = levelController.hero.velocity.add(0, -20);
            canJump = false;
        }
    }
}
