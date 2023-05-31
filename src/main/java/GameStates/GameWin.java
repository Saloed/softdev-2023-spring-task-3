package GameStates;

import GameEngine.Game;
import HelperClasses.DataProcessing;
import HelperClasses.UrmButtons;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static GameEngine.Game.GameWidth;
import static GameEngine.Game.Scale;

public class GameWin {
    private final Playing playing;
    private UrmButtons menu;
    private BufferedImage backgroundImg;
    private int imgX, imgY, imgW, imgH;

    public GameWin(Playing playing) {
        this.playing = playing;
        loadBackground();
        createButtons();
    }

    private void loadBackground() {
        backgroundImg = DataProcessing.GetSprite(DataProcessing.GameWinBackground);
        imgW = (int) (backgroundImg.getWidth() * Scale);
        imgH = (int) (backgroundImg.getHeight() * Scale);
        imgX = GameWidth / 2 - imgW / 2;
        imgY = (int) (50 * Scale);
    }

    private void createButtons() {
        int menuX = (int) (365 * Scale);
        int y = (int) (220 * Scale);

        menu = new UrmButtons(menuX, y, 2);
    }

    public void update() {
        menu.update();
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GameWidth, Game.GameHeight);
        g.drawImage(backgroundImg, imgX, imgY, imgW, imgH, null);

        menu.draw(g);
    }

    public void mouseMoved(MouseEvent e) {
        menu.setMouseOver(false);

        if (isInUrmButton(e, menu))
            menu.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isInUrmButton(e, menu)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                playing.resetGameWin();
                GameState.gameState = GameState.MENU;
            }
        }
        menu.resetBool();
    }

    public void mousePressed(MouseEvent e) {
        if (isInUrmButton(e, menu))
            menu.setMousePressed(true);
    }

    private boolean isInUrmButton(MouseEvent e, UrmButtons gameWinButton) {
        return gameWinButton.getBorders().contains(e.getX(), e.getY());
    }
}
