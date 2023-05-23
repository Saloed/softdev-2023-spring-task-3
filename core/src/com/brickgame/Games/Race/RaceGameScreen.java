package com.brickgame.Games.Race;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;
import com.brickgame.Games.SidePanel;

import java.util.ArrayList;

public class RaceGameScreen implements Screen {
    static SidePanel sidePanel;
    static BrickGame game;
    Stage stage;
    Texture gameGrid;
    SpriteBatch batch;
    SideWalls sideWalls;
    CarPlayer carPlayer;
    ArrayList<CarEnemy> carsEnemy;
    float timeSpawnCarEnemy, timeSpawnCarEnemyLimit = 3.6f;

    public RaceGameScreen(BrickGame gam) {
        game = gam;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        sidePanel = new SidePanel(batch, game);

        gameGrid = new Texture(Gdx.files.internal("background.png"));
        sideWalls = new SideWalls(batch);
        carPlayer = new CarPlayer(batch);
        carsEnemy = new ArrayList<>();
        timeSpawnCarEnemy = 0;
        stage.addActor(sidePanel.musicButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(66f / 255f, 66f / 255f, 231f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timeSpawnCarEnemy += Gdx.graphics.getDeltaTime();

        // спавн вражеских машин
        if (timeSpawnCarEnemy >= timeSpawnCarEnemyLimit) {
            carsEnemy.add(new CarEnemy(batch));
            timeSpawnCarEnemy = 0;
        }
        //обновление позиций объектов игры
        sideWalls.updatePosition();
        carPlayer.updatePosition();
        for (CarEnemy car : carsEnemy) car.updatePosition();
        carPlayer.deleteCarEnemy(carsEnemy); //удаление машин, ушедших за экран

        // отрисовка объектов игры
        batch.begin();
        batch.draw(gameGrid, 0, 0, 10 * Piece.SIZE, 20 * Piece.SIZE);
        sideWalls.draw();
        carPlayer.draw();
        for (CarEnemy car : carsEnemy) car.draw();
        sidePanel.draw();
        stage.draw();
        batch.end();

        // переход на новый уровень
        if (sidePanel.isNextLevel()) {
            if (CarEnemy.timeUpdatePositionLimit >= 0.05f) CarEnemy.timeUpdatePositionLimit -= 0.05f;
            if (timeSpawnCarEnemyLimit >= 0.8) timeSpawnCarEnemyLimit -= 0.2f;
            if (SideWalls.timeUpdatePositionLimit >= 0.05) SideWalls.timeUpdatePositionLimit -= 0.05f;
        }

        // проигрыш
        if (carPlayer.checkCrash(carsEnemy)) {
            game.changeScreen(6);
            game.endGameSceen.beforeGameScreen = 3;
        }

        // принудительный выход из игры
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.changeScreen(0);
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
