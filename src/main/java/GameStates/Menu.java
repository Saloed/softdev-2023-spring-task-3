package GameStates;

import GameEngine.Game;
import Scenes.DataProcessing;
import UI.MenuButtons;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static GameEngine.Game.GameWidth;
import static GameEngine.Game.Scale;

public class Menu extends State implements StateMethods {
    private final MenuButtons[] buttons = new MenuButtons[2];

    private BufferedImage backgroundImage;
    private int menuW, menuH;

    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
    }

    private void loadBackground() {
        backgroundImage = DataProcessing.GetSprite(DataProcessing.MenuBackground);
        menuW = (int) (backgroundImage.getWidth() * Scale);
        menuH = (int) (backgroundImage.getHeight() * Scale);
    }

    private void loadButtons() {
        buttons[0] = new MenuButtons(GameWidth / 2, (int) (100 * Scale), 0, GameState.PLAYING);
        buttons[1] = new MenuButtons(GameWidth / 2, (int) (200 * Scale), 1, GameState.QUIT);
    }

    @Override
    public void update() {
        for (MenuButtons button: buttons) button.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, menuW, menuH, null);
        for (MenuButtons button: buttons) button.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButtons button : buttons)
            button.setMouseOver(false);

        for (MenuButtons button : buttons)
            if (IsInButton(e, button)) {
                button.setMouseOver(true);
                break;
            }
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButtons button: buttons) {
            if (IsInButton(e, button)) {
                if (button.isMousePressed()) button.applyGameState();
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for (MenuButtons button: buttons) {
            button.resetBool();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButtons button: buttons) {
            if (IsInButton(e, button)) {
                button.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) GameState.gameState = GameState.PLAYING;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
