package UI;

import GameStates.GameState;
import GameStates.Playing;
import Scenes.DataProcessing;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static GameEngine.Game.*;

public class Pause {
    private final Playing playing;
    private BufferedImage backgroundImg;
    private int imgX, imgY, imgW, imgH;
    private UrmButtons unpause, replay, menu;


    public Pause(Playing playing) {
        this.playing = playing;
        loadBackground();
        createUrmButtons();
    }

    private void createUrmButtons() {
        int menuX = (int) (230 * Scale);
        int replayX = (int) (365 * Scale);
        int unpauseX = (int) (500 * Scale);
        int urmY = (int) (190 * Scale);

        menu = new UrmButtons(menuX, urmY, 2);
        replay = new UrmButtons(replayX, urmY, 1);
        unpause = new UrmButtons(unpauseX, urmY, 0);
    }

    private void loadBackground() {
        backgroundImg = DataProcessing.GetSprite(DataProcessing.PauseBackground);
        imgW = (int) (backgroundImg.getWidth() * Scale);
        imgH = (int) (backgroundImg.getHeight() * Scale);
        imgX = GameWidth / 2 - imgW / 2;
        imgY = (int) (100 * Scale);
    }

    public void update() {
        menu.update();
        replay.update();
        unpause.update();
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GameWidth, GameHeight);
        g.drawImage(backgroundImg, imgX, imgY, imgW, imgH, null);

        menu.draw(g);
        replay.draw(g);
        unpause.draw(g);
    }

    public void mouseMoved(MouseEvent e) {
        menu.setMouseOver(false);
        replay.setMouseOver(false);
        unpause.setMouseOver(false);

        if (isInUrmButton(e, menu))
            menu.setMouseOver(true);
        else if (isInUrmButton(e, replay))
            replay.setMouseOver(true);
        else if (isInUrmButton(e, unpause))
            unpause.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isInUrmButton(e, menu)) {
            if (menu.isMousePressed())
                GameState.gameState = GameState.MENU;
        } else if (isInUrmButton(e, replay)) {
            if (replay.isMousePressed()) {
                playing.resetAll();
                playing.unpauseGame();
            }
        } else if (isInUrmButton(e, unpause)) {
            if (unpause.isMousePressed())
                playing.unpauseGame();
        }
        menu.resetBool();
        replay.resetBool();
        unpause.resetBool();
    }

    public void mousePressed(MouseEvent e) {
        if (isInUrmButton(e, menu))
            menu.setMousePressed(true);
        else if (isInUrmButton(e, replay))
            replay.setMousePressed(true);
        else if (isInUrmButton(e, unpause))
            unpause.setMousePressed(true);
    }

    private boolean isInUrmButton(MouseEvent e, UrmButtons pauseButton) {
        return pauseButton.getBorders().contains(e.getX(), e.getY());
    }

}
