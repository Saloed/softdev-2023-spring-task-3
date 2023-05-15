package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.states.GameStateManager;
import com.mygdx.game.states.MenuState;

public class IndieGame extends ApplicationAdapter {
    public static final int WIDTH = 1460;
    public static final int HEIGHT = 800;
    public static final String TITLE = "Indiegame";
    private GameStateManager stateManager;
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        stateManager = new GameStateManager();
        Gdx.gl.glClearColor(1,0,0,1);
        stateManager.push(new MenuState(stateManager));
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateManager.update(Gdx.graphics.getDeltaTime());
        stateManager.render(batch);
    }
}
