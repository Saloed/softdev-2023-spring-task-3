package com.brickgame.Games.Shoot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;
import com.brickgame.Games.SidePanel;

import java.util.ArrayList;


public class ShootGameScreen implements Screen {
    public static BrickGame game;
    Stage stage;
    private SpriteBatch batch;
    Texture gameGrid;
    ArrayList<Enemy> enemies;
    float timeSpawn;
    Gun gun;
    static SidePanel sidePanel;

    public ShootGameScreen(BrickGame gam) {
        game = gam;
        stage = new Stage(new ScreenViewport());
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
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

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) gun.shoot();

        // удаление улетевших за экран пуль и убитых врагов
        for (int i = enemies.size() - 1; i >= 0; --i) {
            if (enemies.get(i).killed) {
                enemies.remove(i);
            }
        }
        gun.deleteBullet();

        //спавн врагов
        if (timeSpawn >= 1.2f) {
            enemies.add(new Enemy(batch, sidePanel.level));
            timeSpawn = 0;
        }

        //обновление позиций пуль и врагов
        gun.updatePosition();
        for (Bullet bullet : gun.bullets) {
            bullet.updatePosition();
        }
        for (Enemy enemy : enemies) {
            enemy.updatePosition();
            gun.iskillEnemy(enemy);
        }


        timeSpawn += Gdx.graphics.getDeltaTime();

        // отрисовка игровых объектов
        batch.begin();
        sidePanel.draw();
        batch.draw(gameGrid, 0, 0, 10 * Piece.SIZE, 20 * Piece.SIZE);
        gun.draw();
        stage.draw();
        for (Enemy enemy : enemies) enemy.draw();
        batch.end();

        // переход на следующий уровень
        if (sidePanel.isNextLevel()) {
            game.achives.play();
            gun.timeShootLimit -= 0.2f;
        }

        // принудительный выход из игры
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.changeScreen(0);
        }

        // проигрыш
        for (Enemy enemy : enemies) {
            if (enemy.enemy[3].getY() <= 0) {
                game.changeScreen(6);
                game.endGameSceen.beforeGameScreen = 2;
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
