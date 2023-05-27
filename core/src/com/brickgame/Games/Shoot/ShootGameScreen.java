package com.brickgame.Games.Shoot;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;
import com.brickgame.Games.SidePanel;


import java.util.ArrayList;


public class ShootGameScreen implements Screen {
    private final BrickGame game;
    private Stage stage;
    private SpriteBatch batch;
    private Texture gameGrid;
    private ArrayList<Enemy> enemies;
    private float timeSpawn,timeStepShoot, timeShootLimit = 1f, timeSpawnLimit = 1.5f;
    private Gun gun;
    private SidePanel sidePanel;

    public ShootGameScreen(BrickGame gam) {
        game = gam;
    }


    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        sidePanel = new SidePanel(batch, game);
        gameGrid = new Texture(Gdx.files.internal("background.png"));
        enemies = new ArrayList<>();
        gun = new Gun(batch);
        timeSpawn = 0;
        stage.addActor(sidePanel.musicButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(66f / 255f, 66f / 255f, 231f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Обновление времен
        timeStepShoot += Gdx.graphics.getDeltaTime();
        timeSpawn += Gdx.graphics.getDeltaTime();


        // удаление улетевших за экран пуль и убитых врагов
        for (int i = enemies.size() - 1; i >= 0; --i) {
            if (enemies.get(i).killed) enemies.remove(i);
        }
        gun.deleteBullet();

        //спавн врагов
        if (timeSpawn >= timeSpawnLimit) {
            enemies.add(new Enemy(batch, sidePanel.level));
            timeSpawn = 0;
        }

        // стрельба
        if (timeStepShoot >= timeShootLimit && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            gun.shoot();
            timeStepShoot = 0;
            if (gun.isNeedPlayHit){
                game.hit.play();
                gun.isNeedPlayHit = false;
            }
        }

        //обновление позиций пуль и врагов
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) gun.moveRight();
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) gun.moveLeft();
        for (Bullet bullet : gun.bullets) {
            bullet.updatePosition();
        }
        for (Enemy enemy : enemies) {
            enemy.updatePosition();
            gun.isKillEnemy(enemy);
            if (gun.isNeedPlayBroke) game.broke.play();
            if (gun.isNeedIncreaseScore) sidePanel.score.increaseScore();
        }

        // отрисовка игровых объектов
        batch.begin();
        sidePanel.draw();
        batch.draw(gameGrid, 0, 0, BrickGame.GRID_WIDTH * Piece.SIZE, BrickGame.GRID_HEIGHT * Piece.SIZE);
        gun.draw();
        stage.draw();
        for (Enemy enemy : enemies) enemy.draw();
        batch.end();

        // переход на следующий уровень
        if (sidePanel.isNextLevel()) {
            game.achives.play();
            timeShootLimit -= 0.2f;
        }

        // принудительный выход из игры
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.changeScreen(0);
        }

        // проигрыш
        for (Enemy enemy : enemies) {
            if (enemy.enemy[3].getY() <= 0) {
                game.changeScreen(6);
                game.endGameScreen.beforeGameScreen = 2;
            }
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
