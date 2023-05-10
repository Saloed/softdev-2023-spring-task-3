package GameEngine;

import Levels.LevelManager;
import Scenes.GamePanel;
import Scenes.GameWindow;
import Sprites.MainCharacter;

import java.awt.*;

public class Game implements Runnable {
    private GameWindow gameWindow;
    private final GamePanel gamePanel;

    public final static int DefaultSizeTile = 32;
    public final static float Scale = 1.5f;
    public final static int TilesInWidth = 26;
    public final static int TilesInHeight = 14;
    public final static int TilesSizes = (int) (DefaultSizeTile * Scale);
    public final static int GameWidth = TilesInWidth * TilesSizes;
    public final static int GameHeight = TilesInHeight * TilesSizes;

    private MainCharacter mainCharacter;
    private LevelManager levelManager;


    public Game() {
        init();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        startGame();
    }

    private void startGame() {
        Thread loopThread = new Thread(this);
        loopThread.start();
    }

    private void init() {
        levelManager = new LevelManager(this);
        mainCharacter = new MainCharacter(200, 200, (int) (Scale * 48), (int) (Scale * 48));
        mainCharacter.lvlData(levelManager.getLevel().getLevelData());
        //System.out.println(GameWidth + " " + GameHeight);
    }

    public void update() {
        levelManager.update();
        mainCharacter.update();
    }

    public void render(Graphics g) {
        levelManager.draw(g);
        mainCharacter.render(g);
    }

    public MainCharacter getMainCharacter() {
        return mainCharacter;
    }

    public void lostFocus() {
        mainCharacter.resetBool();
    }

    @Override
    public void run() {
        int FPS = 120;
        int UPS = 200;
        double deltaUPS = 0;
        double deltaFPS = 0;
        int frames = 0;
        int updates = 0;
        double timeOfLastFrame = 1000000000.0 / FPS;
        double timeOfLastUpdate = 1000000000.0 / UPS;
        long thisTime = System.nanoTime();
        long lastFrame = System.nanoTime();
        long lastCheck = System.currentTimeMillis();
        long lastTime = System.nanoTime();

        while (true) {
            long currentTime = System.nanoTime();

            deltaUPS += (currentTime - lastTime) / timeOfLastUpdate;
            deltaFPS += (currentTime - lastTime) / timeOfLastFrame;
            lastTime = currentTime;

            if (deltaUPS >= 1) {
                update();
                updates++;
                deltaUPS--;
            }
            if (deltaFPS >= 1) {
                gamePanel.repaint();
                frames++;
                deltaFPS--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                frames = 0;
                updates = 0;
            }
        }
    }
}