package com.brickgame.Games.Arcanoid;

import com.badlogic.gdx.graphics.Texture;
import com.brickgame.Games.Piece;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brickgame.BrickGame;
import com.brickgame.Games.SidePanel;

public class ArcanoidGameScreen implements Screen {
    static BrickGame game;
    SpriteBatch batch;
    static SidePanel sidePanel;
    Stage stage;
    Ball ball;
    Platform platform;
    Blocks blocks;
    Texture gameGrid;

    public ArcanoidGameScreen(BrickGame gam) {
        game = gam;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        gameGrid = new Texture(Gdx.files.internal("background.png"));
        blocks = new Blocks(batch);
        platform = new Platform(batch, 4, 0, 3);
        ball = new Ball(batch, platform.platform[platform.platform.length / 2].getX(), 1, 1, 1);
        sidePanel = new SidePanel(batch, game);

        stage.addActor(sidePanel.musicButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(66f / 255f, 66f / 255f, 231f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // обновление элементов игры
        platform.updatePosition();
        ball.updatePosition(platform);
        ball.collidesWithBlocks(blocks);

        // отрисовка элементов игры
        batch.begin();
        batch.draw(gameGrid, 0, 0, 10 * Piece.SIZE, 20 * Piece.SIZE);
        blocks.draw();
        platform.draw();
        ball.draw();
        sidePanel.draw();
        stage.draw();
        batch.end();

        // принудительный выход из игры
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) game.changeScreen(0);

        // переход на следующий уровень
        if (blocks.blocks.size() == 0) {
            platform = new Platform(batch, 4, 0, 3);
            ball = new Ball(batch, platform.platform[platform.platform.length / 2].getX(), 1, 1, 1);
            blocks.changeLevel();
            sidePanel.levelLabel.setText(("Level: " + blocks.level));
        }

        // проигрыш
        if (ball.ball.getY() < 0) {
            game.changeScreen(6);
            game.endGameSceen.beforeGameScreen = 1;
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
