package org.scenes;

import org.helpers.LoadSave;
import org.main.Game;
import org.ui.MyButton;

import java.awt.*;

import static org.main.GameStates.*;
import static org.main.LangStates.*;
import static org.main.LangTexts.*;

public class Menu extends GameScene implements SceneMethods {
    private MyButton bPlaying, bQuit, bEdit, bEng, bRus;

    public Menu(Game game) {
        super(game);
        initButtons();
    }

    private void initButtons() {
        int w = 150;
        int h = w / 2;
        int x = 640 / 2 - w / 2;
        int y = 150;
        int yOffset = 100;
        bPlaying = new MyButton(bPlayingStr.toString(), x, y, w, h);
        bEdit = new MyButton(bEditStr.toString(), x, y + yOffset, w, h);
        bQuit = new MyButton(bQuitStr.toString(), x, y + yOffset * 2, w, h);
        bEng = new MyButton("", 245, 25, 50, 50);
        bRus = new MyButton("", 345, 25, 50, 50);
    }

    private void buttonsSetText() {
        bPlaying.setText(bPlayingStr.toString());
        bEdit.setText(bEditStr.toString());
        bQuit.setText(bQuitStr.toString());
    }

    @Override
    public void render(Graphics g) {
        drawButtons(g);
    }

    private void drawButtons(Graphics g) {
        bPlaying.draw(g);
        bEdit.draw(g);
        bQuit.draw(g);

        drawFlags(g);

        drawButtonFeedback(g, bEng);
        drawButtonFeedback(g, bRus);
    }

    private void drawFlags(Graphics g) {
        g.drawImage(LoadSave.getSpriteAtlas().getSubimage(9 * 32, 3 * 32, 32, 32), bEng.x, bEng.y, bEng.width, bEng.height, null);
        g.drawImage(LoadSave.getSpriteAtlas().getSubimage(8 * 32, 3 * 32, 32, 32), bRus.x, bRus.y, bRus.width, bRus.height, null);
    }

    protected void drawButtonFeedback(Graphics g, MyButton b) {
        //Рисует границы на кнопке, чтобы показать, что она интерактивная
        if (b.isMouseOver()) {
            g.setColor(Color.white);
        } else {
            g.setColor(Color.black);
        }
        g.drawRect(b.x, b.y, b.width, b.height);
        if (b.isMousePressed()) {
            g.drawRect(b.x + 1, b.y + 1, b.width - 2, b.height - 2);
            g.drawRect(b.x + 2, b.y + 2, b.width - 4, b.height - 4);
        }
    }

    @Override
    public void mouseClicked(int x, int y) {
        if (bPlaying.getBounds().contains(x, y)) {
            getGame().getPlaying().getActionBar().setButtonText();
            getGame().getGameOver().buttonsSetText();
            SetGameState(PLAYING);
        } else if (bEdit.getBounds().contains(x, y)) {
            getGame().getEditor().getToolBar().setButtonText();
            SetGameState(EDIT);
        } else if (bEng.getBounds().contains(x, y)) {
            LoadSave.setLanguage(ENGLISH);
            buttonsSetText();
        } else if (bRus.getBounds().contains(x, y)) {
            LoadSave.setLanguage(RUSSIAN);
            buttonsSetText();
        } else if (bQuit.getBounds().contains(x, y)) {
            System.exit(0);
        }
    }

    @Override
    public void mouseMoved(int x, int y) {
        bPlaying.setMouseOver(false);
        bEdit.setMouseOver(false);
        bQuit.setMouseOver(false);
        bEng.setMouseOver(false);
        bRus.setMouseOver(false);
        if (bPlaying.getBounds().contains(x, y)) {
            bPlaying.setMouseOver(true);
        } else if (bEdit.getBounds().contains(x, y)) {
            bEdit.setMouseOver(true);
        } else if (bEng.getBounds().contains(x, y)) {
            bEng.setMouseOver(true);
        } else if (bRus.getBounds().contains(x, y)) {
            bRus.setMouseOver(true);
        } else if (bQuit.getBounds().contains(x, y)) {
            bQuit.setMouseOver(true);
        }
    }

    @Override
    public void mousePressed(int x, int y) {
        if (bPlaying.getBounds().contains(x, y)) {
            bPlaying.setMousePressed(true);
        } else if (bEdit.getBounds().contains(x, y)) {
            bEdit.setMousePressed(true);
        } else if (bEng.getBounds().contains(x, y)) {
            bEng.setMousePressed(true);
        } else if (bRus.getBounds().contains(x, y)) {
            bRus.setMousePressed(true);
        } else if (bQuit.getBounds().contains(x, y)) {
            bQuit.setMousePressed(true);
        }
    }

    @Override
    public void mouseReleased(int x, int y) {
        resetButtons();
    }

    private void resetButtons() {
        bPlaying.resetBooleans();
        bEdit.resetBooleans();
        bQuit.resetBooleans();
        bEng.resetBooleans();
        bRus.resetBooleans();
    }
}
