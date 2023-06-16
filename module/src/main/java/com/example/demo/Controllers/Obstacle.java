package com.example.demo.Controllers;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Obstacle extends Pane {
    Rectangle rectangle;
    public int height;
    public int width;
    public Image image;
    public Obstacle(int width, int height, Image image) {
        this.width = width;
        this.height = height;
        this.image = image;
        rectangle = new Rectangle(width, height);
        rectangle.setFill(new ImagePattern(image));
        getChildren().add(rectangle);
    }
}
