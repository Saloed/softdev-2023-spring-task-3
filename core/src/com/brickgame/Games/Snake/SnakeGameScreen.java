package com.brickgame.Games.Snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;
import com.brickgame.Games.SidePanel;

public class SnakeGameScreen implements Screen {

    static BrickGame game;
    static SidePanel sidePanel;
    SpriteBatch batch;
    Texture gameGrid;
    Apple apple;

    Snake snake;
    Stage stage;

    public SnakeGameScreen(BrickGame gam) {
        game = gam;
    }


    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();

        gameGrid = new Texture(Gdx.files.internal("background.png"));
        snake = new Snake(batch);
        apple = new Apple(batch, snake);
        sidePanel = new SidePanel(batch, game);

        stage.addActor(sidePanel.musicButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(66f / 255f, 66f / 255f, 231f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        snake.updatePosition(apple);

        // отрисовка элементов игры
        batch.begin();
        batch.draw(gameGrid, 0, 0, 10 * Piece.SIZE, 20 * Piece.SIZE);
        snake.draw();
        apple.draw();
        sidePanel.draw();
        stage.draw();
        batch.end();

        // переход на следующий уровень
        if (sidePanel.isNextLevel()) if (snake.timeUpdatePositionLimit > 0.01f) snake.timeUpdatePositionLimit -= 0.01f;

        // принудительный выход из игры
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) game.changeScreen(0);

        // проигрыш
        if (snake.checkSelfCollision()) {
            game.changeScreen(6);
            game.endGameSceen.beforeGameScreen = 4;
        }
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
        Gdx.input.setInputProcessor(null);
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        gameGrid.dispose();
    }
}
