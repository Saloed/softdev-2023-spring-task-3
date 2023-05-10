package Inputs;

import Scenes.GamePanel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyPadInputs extends KeyAdapter {
    private GamePanel gamePanel;

    public KeyPadInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    /*@Override
    public void keyTyped(KeyEvent e) {

    }*/

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                gamePanel.getGame().getMainCharacter().setLeft(true);
                break;
            case KeyEvent.VK_S:
                gamePanel.getGame().getMainCharacter().setDown(true);
                break;
            case KeyEvent.VK_D:
                gamePanel.getGame().getMainCharacter().setRight(true);
                break;
            case KeyEvent.VK_W:
                gamePanel.getGame().getMainCharacter().setUp(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                gamePanel.getGame().getMainCharacter().setLeft(false);
                break;
            case KeyEvent.VK_S:
                gamePanel.getGame().getMainCharacter().setDown(false);
                break;
            case KeyEvent.VK_D:
                gamePanel.getGame().getMainCharacter().setRight(false);
                break;
            case KeyEvent.VK_W:
                gamePanel.getGame().getMainCharacter().setUp(false);
                break;
        }
    }
}
