package GameStates;

import GameEngine.Game;
import HelperClasses.DataProcessing;
import HelperClasses.UrmButtons;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static GameEngine.Game.GameWidth;
import static GameEngine.Game.Scale;

public class GameOver {
    private final Playing playing;
    private UrmButtons menu, replay;
    private BufferedImage backgroundImg;
    private int imgX, imgY, imgW, imgH;

    public GameOver(Playing playing) {
        this.playing = playing;
        loadBackground();
        createButtons();
    }

    private void loadBackground() {
        backgroundImg = DataProcessing.GetSprite(DataProcessing.GameOverBackground);
        imgW = (int) (backgroundImg.getWidth() * Scale);
        imgH = (int) (backgroundImg.getHeight() * Scale);
        imgX = GameWidth / 2 - imgW / 2;
        imgY = (int) (50 * Scale);
    }

    private void createButtons() {
        int menuX = (int) (275 * Scale);
        int replayX = (int) (450 * Scale);
        int y = (int) (220 * Scale);

        menu = new UrmButtons(menuX, y, 2);
        replay = new UrmButtons(replayX, y, 1);
    }

    public void update() {
        menu.update();
        replay.update();
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GameWidth, Game.GameHeight);
        g.drawImage(backgroundImg, imgX, imgY, imgW, imgH, null);

        menu.draw(g);
        replay.draw(g);
    }

    public void mouseMoved(MouseEvent e) {
        menu.setMouseOver(false);
        replay.setMouseOver(false);

        if (isInUrmButton(e, menu))
            menu.setMouseOver(true);
        else if (isInUrmButton(e, replay))
            replay.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isInUrmButton(e, menu)) {
            if (menu.isMousePressed()) {
                GameState.gameState = GameState.MENU;
                playing.resetAll();
            }
        } else if (isInUrmButton(e, replay)) {
            if (replay.isMousePressed()) {
                playing.resetAll();
            }
        }
        menu.resetBool();
        replay.resetBool();
    }

    public void mousePressed(MouseEvent e) {
        if (isInUrmButton(e, menu))
            menu.setMousePressed(true);
        else if (isInUrmButton(e, replay))
            replay.setMousePressed(true);
    }

    private boolean isInUrmButton(MouseEvent e, UrmButtons gameOverButton) {
        return gameOverButton.getBorders().contains(e.getX(), e.getY());
    }
}
