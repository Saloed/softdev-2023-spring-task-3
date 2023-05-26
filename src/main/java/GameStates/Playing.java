package GameStates;

import GameEngine.Game;
import Levels.LevelManager;
import Scenes.DataProcessing;
import Sprites.EnemyManager;
import Sprites.MainCharacter;
import UI.GameOver;
import UI.LevelCompleted;
import UI.Pause;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static GameEngine.Game.*;
import static Sprites.Constants.Background.*;

public class Playing extends State implements StateMethods {
    private MainCharacter mainCharacter;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private GameOver gameOverScreen;
    private Pause pauseScreen;
    private LevelCompleted levelCompletedScreen;

    private boolean paused = false;
    private boolean lvlCompleted = false;

    private int xLvlOffset;
    private final int leftBorder = (int) (0.2 * GameWidth);
    private final int rightBorder = (int) (0.8 * GameWidth);
    private int maxTilesOffsetX;

    public BufferedImage backgroundImage;
    private final BufferedImage bigCloud;
    private final BufferedImage smallCloud;
    private final int[] smallCloudsPos;

    private boolean gameOver;

    public Playing(Game game) {
        super(game);
        init();
        backgroundImage = DataProcessing.GetSprite(DataProcessing.PlayingBackgroundDay);
        bigCloud = DataProcessing.GetSprite(DataProcessing.BigClouds);
        smallCloud = DataProcessing.GetSprite(DataProcessing.SmallClouds);
        smallCloudsPos = new int[8];
        Random random = new Random();
        for (int i = 0; i < smallCloudsPos.length; i++)
            smallCloudsPos[i] = (int) (75 * Scale) + random.nextInt((int) (100 * Scale));

        lvlOffsets();
        loadStartLvl();
    }
    private void loadStartLvl() {
        enemyManager.loadEnemies(levelManager.getLevel());
    }

    public void loadNextLvl() {
        resetAll();
        levelManager.loadNextLvl();
        mainCharacter.setSpawnPoint(levelManager.getLevel().getSpawnPoint());
    }

    private void lvlOffsets() {
        maxTilesOffsetX = levelManager.getLevel().getLvlOffset();
    }

    public void setMaxTilesOffsetX(int lvlOffset) {
        this.maxTilesOffsetX = lvlOffset;
    }

    public void setLevelCompleted(boolean lvlCompleted) {
        this.lvlCompleted = lvlCompleted;
    }

    private void init() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        mainCharacter = new MainCharacter(100, 150, (int) (Scale * 48), (int) (Scale * 48), this);
        mainCharacter.lvlData(levelManager.getLevel().getLevelData());
        mainCharacter.setSpawnPoint(levelManager.getLevel().getSpawnPoint());
        pauseScreen = new Pause(this);
        levelCompletedScreen = new LevelCompleted(this);
        gameOverScreen = new GameOver(this);
    }

    @Override
    public void update() {
        if (paused) pauseScreen.update();
        else if (lvlCompleted) levelCompletedScreen.update();
        else if (!gameOver) {
            levelManager.update();
            mainCharacter.update();
            enemyManager.update(levelManager.getLevel().getLevelData(), mainCharacter);
            checkCloseToBorder();
        }
    }

    public void resetAll() {
        //reset all: playing, mc, enemy, lvl etc.
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        mainCharacter.resetAll();
        enemyManager.resetAll();
    }

    public void gameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void unpauseGame() {
        paused = false;
    }

    public void checkEnemyHit(Rectangle2D.Float attackHitbox) {
        enemyManager.checkEnemyHit(attackHitbox);
    }

    private void checkCloseToBorder() {
        int mainCharacterX = (int) mainCharacter.getHitbox().x;
        int difference = mainCharacterX - xLvlOffset;

        if (difference > rightBorder) xLvlOffset += difference - rightBorder;
        else if (difference < leftBorder) xLvlOffset += difference - leftBorder;

        if (xLvlOffset > maxTilesOffsetX) xLvlOffset = maxTilesOffsetX;
        else if (xLvlOffset < 0) xLvlOffset = 0;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, GameWidth, GameHeight, null);
        drawClouds(g);
        levelManager.draw(g, xLvlOffset);
        mainCharacter.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        if (paused) pauseScreen.draw(g);
        else if (gameOver) gameOverScreen.draw(g);
        else if (lvlCompleted) levelCompletedScreen.draw(g);
    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < 7; i++)
            g.drawImage(bigCloud, i * BigCloudsWidthScaled - (int) (xLvlOffset * 0.3),
                    (int) (253 * Scale), BigCloudsWidthScaled, BigCloudsHeightScaled, null);

        for (int i = 0; i < smallCloudsPos.length; i++)
            g.drawImage(smallCloud, SmallCloudsWidthScaled * 4 * i - (int) (xLvlOffset * 0.7),
                    smallCloudsPos[i], SmallCloudsWidthScaled, SmallCloudsHeightScaled, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver)
            if (e.getButton() == MouseEvent.BUTTON1) mainCharacter.attack(true);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (paused) pauseScreen.mouseMoved(e);
            else if (lvlCompleted) levelCompletedScreen.mouseMoved(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (paused) pauseScreen.mouseReleased(e);
            else if (lvlCompleted) levelCompletedScreen.mouseReleased(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (paused) pauseScreen.mousePressed(e);
            else if (lvlCompleted) levelCompletedScreen.mousePressed(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) gameOverScreen.keyPressed(e);
        else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    mainCharacter.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    mainCharacter.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    mainCharacter.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    mainCharacter.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    mainCharacter.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    mainCharacter.setJump(false);
            }
    }

    public MainCharacter getMainCharacter() {
        return mainCharacter;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

}
