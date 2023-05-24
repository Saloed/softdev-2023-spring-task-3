package com.brickgame.Games.Shoot;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;

import java.util.ArrayList;
import java.util.Arrays;

public class Gun {
    public ArrayList<Piece> gun;
    public ArrayList<Bullet> bullets;
    private final SpriteBatch batch;
    public float timeStepShoot, timeMove, timeShootLimit = 1f, timeMoveLimit = 0.1f;
    public boolean isNeedPlayHit, isNeedPlayBroke, isNeedIncreaseScore;

    public Gun(SpriteBatch batch) {
        this.batch = batch;
        gun = new ArrayList<>(Arrays.asList(new Piece((float)(BrickGame.GRID_WIDTH /2 -1), 0), new Piece((float)(BrickGame.GRID_WIDTH / 2), 0), new Piece((float)(BrickGame.GRID_WIDTH / 2) + 1, 0), new Piece((float)(BrickGame.GRID_WIDTH / 2), 1)));
        bullets = new ArrayList<>();
    }

    public void updatePosition() {
        timeMove += Gdx.graphics.getDeltaTime();
        if (timeMove >= timeMoveLimit) {
            if (gun.get(gun.size() - 1).getX() + 1 <= BrickGame.GRID_WIDTH - 2 && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                for (Piece piece : gun) piece.setX(piece.getX() + 1);
            }
            if (gun.get(0).getX() - 1 >= 0 && Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                for (Piece piece : gun) piece.setX(piece.getX() - 1);
            }
            timeMove = 0;
        }
    }

    public void shoot() {
        isNeedPlayHit = false;
        timeStepShoot += Gdx.graphics.getDeltaTime();
        if (timeStepShoot >= timeShootLimit) {
            bullets.add(new Bullet(batch, this));
            timeStepShoot = 0;
            isNeedPlayHit = true;
        }
    }

    public void deleteBullet() {
        for (int i = bullets.size() - 1; i >= 0; --i) {
            if (bullets.get(i).needToDelete) {
                bullets.remove(i);
            }
        }
    }

    //проверка столкновения пули и врага
    public void isKillEnemy(Enemy enemy) {
        isNeedIncreaseScore = false;
        isNeedPlayBroke = false;
        for (Piece e : enemy.enemy) {
            for (Bullet b : bullets) {
                if (b.bullet.getX() == e.getX() && b.bullet.getY() == e.getY()) {
                    enemy.hp--;
                    isNeedPlayBroke = true;
                    if (enemy.hp == 0) {
                        isNeedIncreaseScore = true;
                        enemy.killed = true;
                    }
                    b.bullet.setY(BrickGame.GRID_HEIGHT + 5);
                    break;
                }
            }
        }
    }

    public void draw() {
        for (Piece piece : gun) piece.draw(batch);
        for (Bullet bullet : bullets) {
            bullet.draw();
        }
    }
}
