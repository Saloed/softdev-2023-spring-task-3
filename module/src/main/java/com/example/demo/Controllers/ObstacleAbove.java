package com.example.demo.Controllers;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class ObstacleAbove extends Pane {
    Rectangle rectangle;
    public int length;
    public int width;
    public Image image;
    public ObstacleAbove(int width, int length, Image image) {
        this.width = width;
        this.length = length;
        this.image = image;
        rectangle = new Rectangle(width, length);
        rectangle.setFill(new ImagePattern(image));
        getChildren().add(rectangle);
    }
}
