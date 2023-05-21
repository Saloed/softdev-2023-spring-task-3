package com.brickgame.Games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brickgame.BrickGame;

public class EndGameSceen implements Screen {
    private Stage stage;
    private final BrickGame game;
    public int beforeGameScreen;

    public EndGameSceen(BrickGame game, int beforegame) {
        this.game = game;
        beforeGameScreen = beforegame;
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        stage.addActor(table);
        TextButton resumeGame = new TextButton("Resume Game", game.skin);
        TextButton exitGame = new TextButton("Exit game", game.skin);
        table.add(resumeGame).fillX().uniformX();
        table.add(exitGame).fillX().uniformX();

        resumeGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(beforeGameScreen);
            }
        });

        exitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(0);
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(66f / 255f, 66f / 255f, 231f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
