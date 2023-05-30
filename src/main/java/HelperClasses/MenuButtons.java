package HelperClasses;

import GameStates.GameState;

import java.awt.*;
import java.awt.image.BufferedImage;

import static HelperClasses.Constants.UI.Buttons.*;

public class MenuButtons {
    private final int x, y, line;
    private int index;
    private final int xOffset = ButtonWidth / 2;
    private final GameState gameState;
    private BufferedImage[] images;
    private boolean mouseOver, mousePressed;
    private Rectangle borders;

    public MenuButtons(int x, int y, int line, GameState gameState) {
        this.x = x;
        this.y = y;
        this.line = line;
        this.gameState = gameState;
        loadImages();
        initBorders();
    }

    private void initBorders() {
        borders = new Rectangle( x - xOffset, y, ButtonWidthScaled, ButtonHeightScaled);
    }

    private void loadImages() {
        images = new BufferedImage[3];
        BufferedImage temp = DataProcessing.GetSprite(DataProcessing.ButtonsForMenu);
        for (int i = 0; i < images.length; i++) {
            images[i] = temp.getSubimage(i * ButtonWidth, line * ButtonHeight, ButtonWidth, ButtonHeight);
        }
    }

    public void draw(Graphics g){
        g.drawImage(images[index], x - xOffset, y, ButtonWidthScaled, ButtonHeightScaled, null);
    }

    public void update(){
        index = 0;
        if (mouseOver) index = 1;
        if (mousePressed) index = 2;
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

    public Rectangle getBorders(){
        return borders;
    }

    public void applyGameState(){
        GameState.gameState = gameState;
    }

    public void resetBool(){
        mouseOver = false;
        mousePressed = false;
    }
}
