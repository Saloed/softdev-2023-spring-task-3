package UI;

import Util.Resource;
import object.*;
import org.jetbrains.annotations.NotNull;


import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;


public class Screen extends JPanel implements KeyListener {
    public static final int GAME_FIRST_STATE = 0;
    public static final int GAME_PLAY_STATE = 1;
    public static final int GAME_OVER_STATE = 2;
    public static final float GRAVITY = 0.5f;
    public static final float GROUDNY = 110;
    public static final int SCREEN_WIDTH = 600;
    private float score;
    private int highScore;

    private MainCharacter mainCharacter;
    private final Land land;
    private final Clouds clouds;
    private final EnemiesManager enemiesManager;

    private int gameState = GAME_FIRST_STATE;
    private boolean isKeyPressed;

    private final BufferedImage imageGameOver;
    private final BufferedImage imageReplay;


    public Screen() {
        mainCharacter = new MainCharacter();
        mainCharacter.setX(50);
        mainCharacter.setY(60);
        land = new Land(mainCharacter);
        mainCharacter.setSpeedX(5);
        clouds = new Clouds();
        enemiesManager = new EnemiesManager(mainCharacter);
        imageGameOver = Resource.getImage("files/gameover_text.png");
        imageReplay = Resource.getImage("files/replay_button.png");
    }

    public void update() {
        if (gameState == GAME_PLAY_STATE) {
            mainCharacter.update();
            land.update();
            clouds.update();
            enemiesManager.update();
            plusScore();
            if (!mainCharacter.getAlive()) {
                gameState = GAME_OVER_STATE;
            }
        }
    }

    public void plusScore() {
        score += 0.3f;
        Clip scoreUpSound = Resource.getAudio("files/1score_up.wav");
        if ((int) score % 100 == 0 && ((int) (score - 0.3f) != (int) score)) {
            scoreUpSound.start();
        }
    }

    @Override
    public void paint(@NotNull Graphics g) {
        g.setColor(Color.decode("#f7f7f7"));
        g.fillRect(0, 0, getWidth(), getHeight());
        switch (gameState) {
            case GAME_FIRST_STATE -> {
                mainCharacter.draw(g);
                g.drawString("To start the game, press Space.", 200, 30);
            }
            case GAME_PLAY_STATE -> {
                clouds.draw(g);
                land.draw(g);
                enemiesManager.draw(g);
                mainCharacter.draw(g);
                g.drawString("HI " + highScore + " " + (int) score, 500, 20);
            }
            case GAME_OVER_STATE -> {
                clouds.draw(g);
                land.draw(g);
                enemiesManager.draw(g);
                mainCharacter.draw(g);
                g.drawImage(imageGameOver, 200, 30, null);
                g.drawImage(imageReplay, 283, 50, null);
            }
        }

    }

    public void resetGame() {
        highScore = (int) Math.max(highScore, score);
        score = 0;
        enemiesManager.reset();
        mainCharacter.setAlive(true);
        mainCharacter.setY(60);
        mainCharacter.setSpeedX(5);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!isKeyPressed) {
            isKeyPressed = true;
            switch (gameState) {
                case GAME_FIRST_STATE:
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        gameState = GAME_PLAY_STATE;
                    }
                    break;
                case GAME_PLAY_STATE:
                    if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
                        mainCharacter.jump();
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        mainCharacter.down(true);
                    }
                    break;
                case GAME_OVER_STATE:
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        gameState = GAME_PLAY_STATE;
                        resetGame();
                    }
                    break;

            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        isKeyPressed = false;
        if (gameState == GAME_PLAY_STATE) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                mainCharacter.down(false);
            }
        }
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public MainCharacter getMainCharacter() {
        return mainCharacter;
    }

    public void setMainCharacter(MainCharacter mainCharacter) {
        this.mainCharacter = mainCharacter;
    }

    public int getGameState() {
        return gameState;
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }
}
