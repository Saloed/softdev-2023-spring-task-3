package UI;

import GameStates.GameState;
import GameStates.Playing;
import Scenes.DataProcessing;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static GameEngine.Game.*;

public class LevelCompleted {
    private final Playing playing;
    private UrmButtons menu, nextLvl;
    private BufferedImage backgroundImg;
    private int imgX, imgY, imgW, imgH;

    public LevelCompleted(Playing playing) {
        this.playing = playing;
        loadBackground();
        createButtons();
    }

    private void loadBackground() {
        backgroundImg = DataProcessing.GetSprite(DataProcessing.LevelCompletedBackground);
        imgW = (int) (backgroundImg.getWidth() * Scale);
        imgH = (int) (backgroundImg.getHeight() * Scale);
        imgX = GameWidth / 2 - imgW / 2;
        imgY = (int) (50 * Scale);
    }

    private void createButtons() {
        int menuX = (int) (275 * Scale);
        int nextLvlX = (int) (450 * Scale);
        int y = (int) (220 * Scale);

        menu = new UrmButtons(menuX, y, 2);
        nextLvl = new UrmButtons(nextLvlX, y, 0);
    }

    public void update() {
        menu.update();
        nextLvl.update();
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GameWidth, GameHeight);
        g.drawImage(backgroundImg, imgX, imgY, imgW, imgH, null);

        menu.draw(g);
        nextLvl.draw(g);
    }

    public void mouseMoved(MouseEvent e) {
        menu.setMouseOver(false);
        nextLvl.setMouseOver(false);

        if (isInUrmButton(e, menu))
            menu.setMouseOver(true);
        else if (isInUrmButton(e, nextLvl))
            nextLvl.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isInUrmButton(e, menu)) {
            if (menu.isMousePressed())
                GameState.gameState = GameState.MENU;
        } else if (isInUrmButton(e, nextLvl)) {
            if (nextLvl.isMousePressed()) {
                playing.loadNextLvl();
            }
        }
        menu.resetBool();
        nextLvl.resetBool();
    }

    public void mousePressed(MouseEvent e) {
        if (isInUrmButton(e, menu))
            menu.setMousePressed(true);
        else if (isInUrmButton(e, nextLvl))
            nextLvl.setMousePressed(true);
    }

    private boolean isInUrmButton(MouseEvent e, UrmButtons pauseButton) {
        return pauseButton.getBorders().contains(e.getX(), e.getY());
    }
}
