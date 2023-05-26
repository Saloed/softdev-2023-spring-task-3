package GameEngine;

import GameStates.Menu;
import GameStates.Playing;
import Scenes.GamePanel;
import Scenes.GameWindow;

import java.awt.*;

import static GameStates.GameState.PLAYING;
import static GameStates.GameState.gameState;

public class Game implements Runnable {
    private final GamePanel gamePanel;
    private Playing playing;
    private Menu menu;

    public final static int DefaultSizeTile = 32;
    public final static float Scale = 1.5f;
    public final static int TilesInWidth = 26;
    public final static int TilesInHeight = 14;
    public final static int TileSize = (int) (DefaultSizeTile * Scale);
    public final static int GameWidth = TilesInWidth * TileSize;
    public final static int GameHeight = TilesInHeight * TileSize;

    public Game() {
        init();

        gamePanel = new GamePanel(this);
        GameWindow gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        startGame();
        System.out.println(GameWidth + " " + GameHeight);
    }

    private void startGame() {
        Thread loopThread = new Thread(this);
        loopThread.start();
    }

    private void init() {
        menu = new Menu(this);
        playing = new Playing(this);
    }

    public void update() {
        switch (gameState) {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g) {
        switch (gameState) {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            default:
                break;
        }
    }

    public void lostFocus() {
        if (gameState == PLAYING) playing.getMainCharacter().resetBool();
    }

    @Override
    public void run() {
        int FPS = 120;
        int UPS = 200;
        double deltaUPS = 0;
        double deltaFPS = 0;
        double timeOfLastFrame = 1000000000.0 / FPS;
        double timeOfLastUpdate = 1000000000.0 / UPS;
        long lastCheck = System.currentTimeMillis();
        long lastTime = System.nanoTime();

        while (true) {
            long currentTime = System.nanoTime();

            deltaUPS += (currentTime - lastTime) / timeOfLastUpdate;
            deltaFPS += (currentTime - lastTime) / timeOfLastFrame;
            lastTime = currentTime;

            if (deltaUPS >= 1) {
                update();
                deltaUPS--;
            }
            if (deltaFPS >= 1) {
                gamePanel.repaint();
                deltaFPS--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
            }
        }
    }

    public Playing getPlaying() {
        return playing;
    }

    public Menu getMenu() {
        return menu;
    }
}