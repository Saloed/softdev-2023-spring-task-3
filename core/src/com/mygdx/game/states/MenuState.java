package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.IndieGame;
import com.mygdx.game.characters.MainHero;

import java.awt.*;

public class MenuState extends State {
    Music backgroundMusic;
    private Texture currentMusicTexture;
    private Texture musicTrueTexture;
    private Texture musicFalseTexture;
    private Texture backgroundTexture;
    private Texture startButtonTexture;
    private Texture startTexture;
    private Texture gameNameTexture;
    private Texture versionNumberTexture;
    private Rectangle startButton;
    private Rectangle musicButton;

    public MenuState(GameStateManager stateManager) {
        super(stateManager);
        startButton = new Rectangle();
        startButton.x = IndieGame.WIDTH / 2 - 50;
        startButton.y = IndieGame.HEIGHT / 2 - 100;
        startButton.width = 96;
        startButton.height = 32;
        musicButton = new Rectangle();
        musicButton.x = IndieGame.WIDTH - 150;
        musicButton.y = 10;
        musicButton.width = 140;
        musicButton.height = 100;
        musicFalseTexture = new Texture("menu/musicFalse.png");
        musicTrueTexture = new Texture("menu/musicTrue.png");
        currentMusicTexture = musicTrueTexture;
        versionNumberTexture = new Texture("menu/versionNumber.png");
        backgroundTexture = new Texture("menu/menuBackground.png");
        startButtonTexture = new Texture("menu/buttonLarge.png");
        startTexture = new Texture("menu/startString.png");
        gameNameTexture = new Texture("menu/gameName.png");
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("backgroundMusic.mp3"));
        backgroundMusic.play();
        backgroundMusic.setLooping(true);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched() &&
                startButton.contains(new Point(Gdx.input.getX(), IndieGame.HEIGHT - Gdx.input.getY())))
            stateManager.set(new PlayState(stateManager, true, 0,
                    new MainHero(50, 50, 100, 15)));
        if (Gdx.input.justTouched()
                && musicButton.contains(new Point(Gdx.input.getX(), IndieGame.HEIGHT - Gdx.input.getY()))) {
            if (currentMusicTexture == musicTrueTexture) {
                currentMusicTexture = musicFalseTexture;
                backgroundMusic.pause();
            } else {
                currentMusicTexture = musicTrueTexture;
                backgroundMusic.play();
                backgroundMusic.setLooping(true);
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(backgroundTexture, 0, 0);
        sb.draw(startButtonTexture, startButton.x, startButton.y);
        sb.draw(startTexture, startButton.x + 10, startButton.y);
        sb.draw(gameNameTexture, IndieGame.WIDTH / 2 - 170, IndieGame.HEIGHT / 2 - 30);
        sb.draw(versionNumberTexture, 0, IndieGame.HEIGHT - 30);
        sb.draw(currentMusicTexture, musicButton.x, musicButton.y);

        sb.end();
    }

    @Override
    public void dispose() {
        backgroundMusic.dispose();
        musicTrueTexture.dispose();
        musicFalseTexture.dispose();
        backgroundTexture.dispose();
        startButtonTexture.dispose();
        startTexture.dispose();
        gameNameTexture.dispose();
        versionNumberTexture.dispose();
    }
}
