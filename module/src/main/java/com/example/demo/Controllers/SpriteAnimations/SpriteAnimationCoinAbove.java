package com.example.demo.Controllers.SpriteAnimations;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimationCoinAbove extends Transition {
    public ImageView imageView;
    public int columns;
    public int offsetX;
    public int width;
    public int height;

    public SpriteAnimationCoinAbove(
            ImageView imageView, Duration duration,
            int columns, int offsetX,
            int width, int height) {
        this.imageView = imageView;
        this.columns = columns;
        this.offsetX = offsetX;
        this.width = width;
        this.height = height;
        setCycleDuration(duration);
        setCycleCount(INDEFINITE);
        setInterpolator(Interpolator.LINEAR);
        this.imageView.setViewport(new Rectangle2D(offsetX, 0, width, height));
    }

    protected void interpolate(double k) {
        int index;
        if (k <= 0.125) index = 0;
        else if (k <= 0.250) index = 1;
        else if (k <= 0.375) index = 2;
        else if (k <= 0.5) index = 3;
        else if (k <= 0.625) index = 4;
        else if (k <= 0.750) index = 5;
        else if (k <= 0.875) index = 6;
        else index = 7;
        final int x = (index % columns) * width + offsetX;
        imageView.setViewport(new Rectangle2D(x, 0, width, height));
    }
}
