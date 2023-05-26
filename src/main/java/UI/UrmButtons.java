package UI;

import Scenes.DataProcessing;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Sprites.Constants.UI.UrmButtons.*;

public class UrmButtons{
    private BufferedImage[] images;
    private final int x, y, line;
    private int index;
    private boolean mouseOver, mousePressed;
    private Rectangle borders;

    public UrmButtons(int x, int y, int line) {
        this.x = x;
        this.y = y;
        this.line = line;
        loadImages();
        initBorders();
    }

    private void loadImages() {
        images = new BufferedImage[3];
        BufferedImage temp = DataProcessing.GetSprite(DataProcessing.ButtonsForPause);
        for (int i = 0; i < images.length; i++) {
            images[i] = temp.getSubimage(i * UrmWidth, line * UrmHeight, UrmWidth, UrmHeight);
        }
    }
    private void initBorders() {
        borders = new Rectangle(x, y, UrmWidthScaled, UrmHeightScaled);
    }

    public void draw(Graphics g) {
        g.drawImage(images[index], x, y, UrmWidthScaled, UrmHeightScaled, null);
    }

    public void update(){
        index = 0;
        if (mouseOver) index = 1;
        if (mousePressed) index = 2;
    }

    public void resetBool(){
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public Rectangle getBorders() {
        return borders;
    }

    public void setBorders(Rectangle borders) {
        this.borders = borders;
    }

}
