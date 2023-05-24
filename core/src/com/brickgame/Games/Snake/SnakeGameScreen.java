package com.brickgame.Games.Snake;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;
import com.brickgame.Games.SidePanel;

public class SnakeGameScreen implements Screen {

    private final BrickGame game;
    private SidePanel sidePanel;
    private SpriteBatch batch;
    private Texture gameGrid;
    private Apple apple;
    private Snake snake;
    private Stage stage;

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
        if(snake.isNeedIncreaseScore) sidePanel.score.increaseScore();
        if(snake.isNeedHitPlay) game.hit.play();

        // отрисовка элементов игры
        batch.begin();
        batch.draw(gameGrid, 0, 0, BrickGame.GRID_WIDTH * Piece.SIZE, BrickGame.GRID_HEIGHT * Piece.SIZE);
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
            game.endGameScreen.beforeGameScreen = 4;
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

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
