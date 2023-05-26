package UI;

import GameEngine.Game;
import GameStates.GameState;
import GameStates.Playing;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOver {
    private final Playing playing;

    public GameOver(Playing playing) {
        this.playing = playing;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, Game.GameWidth, Game.GameHeight);

        g.setColor(Color.WHITE);
        g.drawString("Game Over", Game.GameWidth / 2, 150);
        g.drawString("press esc", Game.GameWidth / 2, 300);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            GameState.gameState = GameState.MENU;
        }
    }
}
