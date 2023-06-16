package com.example.demo.Controllers.SpriteAnimations;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

import javafx.util.Duration;

public class SpriteAnimationHero extends Transition {
    public ImageView imageView;
    public int count;
    public int columns;
    public int offsetX;
    public int offsetY;
    public int width;
    public int height;

    public SpriteAnimationHero(
            ImageView imageView, Duration duration,
            int count, int columns, int offsetX,
            int offsetY, int width, int height) {
        this.imageView = imageView;
        this.count = count;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        setCycleDuration(duration);
        setCycleCount(INDEFINITE);
        setInterpolator(Interpolator.LINEAR);
        this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
    }

    public void setOffsetXhero(int x) {
        this.offsetX = x;
    }
    public void setOffsetYhero(int y) {
        this.offsetY = y;
    }

    protected void interpolate(double k) {
        int index;
        if (k <= 0.25) index = 0;
        else if (k <= 0.5) index = 1;
        else if (k <= 0.75) index = 2;
        else index = 3;
        final int x = (index % columns) * width + offsetX;
        final int y = (index / columns) * height + offsetY;
        imageView.setViewport(new Rectangle2D(x, y, width, height));
    }
}
