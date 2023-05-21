package com.brickgame;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.brickgame.Games.Arcanoid.ArcanoidGameScreen;
import com.badlogic.gdx.Game;
import com.brickgame.Games.EndGameSceen;
import com.brickgame.Games.Race.RaceGameScreen;
import com.brickgame.Games.Snake.SnakeGameScreen;
import com.brickgame.Games.Shoot.ShootGameScreen;
import com.brickgame.Games.Tetris.TetrisGameScreen;

public class BrickGame  extends Game {

    public final static int MENU = 0;
    public final static int ARCANOID = 1;
    public final static int SPACEINVANDERS = 2;
    public final static int RACE = 3;
    public final static int SNAKE = 4;
    public final static int TETRIS = 5;
    public final static int ENDGAME = 6;

    public MainMenuScreen mainMenuScreen;
    public ArcanoidGameScreen arcanoidGameScreen;
    public ShootGameScreen shootGameScreen;
    public RaceGameScreen raceGameScreen;
    public SnakeGameScreen snakeGameScreen;
    public TetrisGameScreen tetrisGameScreen;


    public EndGameSceen endGameSceen;
    public AssetsManager assetsManager;
    public Sound hit, broke, achives;
    public Music music;
    public Skin skin;


    @Override
    public void create() {
        mainMenuScreen = new MainMenuScreen(this);
        assetsManager = new AssetsManager();
        setScreen(mainMenuScreen);
        assetsManager.queueAddSounds();
        assetsManager.queueAddMusic();
        assetsManager.queueAddSkin();
        assetsManager.manager.finishLoading();
        skin = assetsManager.manager.get("skin/uiskin.json");
        hit = assetsManager.manager.get("Sounds/hit.mp3", Sound.class);
        broke = assetsManager.manager.get("Sounds/brickIsbroke.mp3", Sound.class);
        achives = assetsManager.manager.get("Sounds/achives.mp3", Sound.class);
        music = assetsManager.manager.get("Sounds/backgroundMusic.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }
    @Override
    public void render(){
        super.render();
    }

    public void changeScreen(int screen) {
        switch (screen) {
            case MENU:
                if (mainMenuScreen == null) mainMenuScreen = new MainMenuScreen(this);
                this.setScreen(mainMenuScreen);
                break;
            case ARCANOID:
                if (arcanoidGameScreen == null) arcanoidGameScreen = new ArcanoidGameScreen(this);
                this.setScreen(arcanoidGameScreen);
                break;
            case SPACEINVANDERS:
                if ( shootGameScreen == null) shootGameScreen = new ShootGameScreen(this);
                this.setScreen(shootGameScreen);
                break;
            case RACE:
                if ( raceGameScreen== null) raceGameScreen = new RaceGameScreen(this);
                this.setScreen(raceGameScreen);
                break;
            case SNAKE:
                if ( snakeGameScreen == null) snakeGameScreen = new SnakeGameScreen(this);
                this.setScreen(snakeGameScreen);
                break;
            case TETRIS:
                if ( tetrisGameScreen == null) tetrisGameScreen = new TetrisGameScreen(this);
                this.setScreen(tetrisGameScreen);
                break;
            case ENDGAME:
                if ( endGameSceen== null) endGameSceen = new EndGameSceen(this,-1);
                this.setScreen(endGameSceen);
                break;
        }
    }
    @Override
    public void dispose(){
        music.dispose();
       assetsManager.manager.dispose();
    }
}
