package org.scenes;

import org.main.Game;
import org.ui.MyButton;

import java.awt.*;

import static org.main.GameStates.*;
import static org.main.LangTexts.*;


public class GameOver extends GameScene implements SceneMethods {

    private MyButton bReplay, bMenu;

    public GameOver(Game game) {
        super(game);

        initButtons();
    }

    private void initButtons() {
        int w = 150;
        int h = w / 2;
        int x = 640 / 2 - w / 2;
        int y = 300;
        int yOffset = 100;

        bMenu = new MyButton(bMenuStr.toString(), x, y, w, h);
        bReplay = new MyButton(bReplayStr.toString(), x, y + yOffset, w, h);
        buttonsSetText();
    }

    public void buttonsSetText() {
        bMenu.setText(bMenuStr.toString());
        bReplay.setText(bReplayStr.toString());
    }

    @Override
    public void render(Graphics g) {
        g.setFont(new Font("LucidaSans", Font.PLAIN, 20));
        bMenu.draw(g);
        bReplay.draw(g);

        g.setColor(Color.RED);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
        int w = g.getFontMetrics().stringWidth(gameOverStr.toString());
        g.drawString(gameOverStr.toString(), 320 - w / 2, 80);
    }

    private void replayGame() {
        resetAll();

        SetGameState(PLAYING);
    }

    private void goToMenu() {
        resetAll();

        SetGameState(MENU);
    }

    private void resetAll() {
        getGame().getPlaying().resetAll();
    }


    @Override
    public void mouseClicked(int x, int y) {
        if (bMenu.getBounds().contains(x, y))
            goToMenu();
        else if (bReplay.getBounds().contains(x, y))
            replayGame();
    }

    @Override
    public void mouseMoved(int x, int y) {
        bMenu.setMouseOver(false);
        bReplay.setMouseOver(false);
        if (bMenu.getBounds().contains(x, y))
            bMenu.setMouseOver(true);
        else if (bReplay.getBounds().contains(x, y))
            bReplay.setMouseOver(true);
    }

    @Override
    public void mousePressed(int x, int y) {
        if (bMenu.getBounds().contains(x, y))
            bMenu.setMousePressed(true);
        else if (bReplay.getBounds().contains(x, y))
            bReplay.setMousePressed(true);
    }

    @Override
    public void mouseReleased(int x, int y) {
        bMenu.resetBooleans();
        bReplay.resetBooleans();
    }
}
