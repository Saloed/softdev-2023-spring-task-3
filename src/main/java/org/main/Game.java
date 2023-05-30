package org.main;

import org.helpers.LoadSave;
import org.managers.TileManager;
import org.scenes.*;
import org.scenes.Menu;

import javax.swing.*;
import java.awt.*;

public class Game implements Runnable {
    private JFrame frame;

    private GameScreen gameScreen;
    private Thread gameThread;

    private final double FPS_SET = 120.0;
    private final double UPS_SET = 60.0;

    //Классы
    private Render render;
    private Menu menu;
    private Playing playing;
    private Editing editing;
    private GameOver gameOver;
    private GameWin gameWin;
    private TileManager tileManager;

    public Game() {
        LoadSave.createFolder();

        //Создание уровня
        createDefaultLevel();
        setLanguage();
        initClasses();

        //Создание самого окна
        if (!GraphicsEnvironment.isHeadless()) {
            frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Tower Defence");
            frame.setResizable(false);
            frame.add(gameScreen);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    private void initClasses() {
        tileManager = new TileManager();
        render = new Render(this);
        gameScreen = new GameScreen(this);
        menu = new Menu(this);
        playing = new Playing(this);
        editing = new Editing(this);
        gameOver = new GameOver(this);
        gameWin = new GameWin(this);
    }

    public static void createDefaultLevel() {
        //Уровень, состоящий только из травы
        int[] arr = new int[400];
        for (int i = 220; i < 240; i++)
            arr[i] = 2;

        LoadSave.createLevel(arr);
    }

    private void setLanguage() {
        LoadSave.createLanguageFile();
    }

    private void start() {
        gameThread = new Thread(this) {
        };
        gameThread.start();
    }


    private void updateGame() {
        //Для обновления важных данных
        switch (GameStates.gameState) {
            case PLAYING -> playing.update();
            case EDIT -> editing.update();
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.gameScreen.initInputs();
        game.start();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long lastFrame = System.nanoTime();
        long lastUpdate = System.nanoTime();

        long now;

        while (true) {
            now = System.nanoTime();
            if (now - lastFrame >= timePerFrame) {
                frame.repaint();
                lastFrame = now;
            }
            if (System.nanoTime() - lastUpdate >= timePerUpdate) {
                updateGame();
                lastUpdate = now;
            }
        }
    }

    public Render getRender() {
        return render;
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public Editing getEditor() {
        return editing;
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    public GameOver getGameOver() {
        return gameOver;
    }

    public GameWin getGameWin() {
        return gameWin;
    }
}

