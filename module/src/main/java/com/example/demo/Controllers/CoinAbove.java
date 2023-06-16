package com.example.demo.Controllers;

import com.example.demo.Controllers.SpriteAnimations.SpriteAnimationCoin;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class CoinAbove extends Pane {
    final ImageView imageView;
    final int columns = 8;
    public int offsetX = 0;
    final int width = 60;
    final int height = 60;
    public SpriteAnimationCoin spriteAnimation;
    public LevelController levelController;

    public CoinAbove(LevelController levelController, ImageView imageView) {
        this.levelController = levelController;
        this.imageView = imageView;
        this.imageView.setViewport(new Rectangle2D(offsetX, 0, width, height));
        spriteAnimation = new SpriteAnimationCoin(
                imageView, Duration.millis(1100), columns, offsetX, width, height);
        getChildren().add(imageView);
        this.setTranslateX(2920);
        this.setTranslateY(770);
    }
}
