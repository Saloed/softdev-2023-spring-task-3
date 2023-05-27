package com.brickgame.Games.Shoot;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gun {
    public List<Piece> gun;
    public List<Bullet> bullets;
    private final SpriteBatch batch;
    public float timeUpdatePositions, timeUpdatePositionLimit = 0.1f;
    public boolean isNeedPlayHit, isNeedPlayBroke, isNeedIncreaseScore;

    public Gun(SpriteBatch batch) {
        this.batch = batch;
        gun = new ArrayList<>(Arrays.asList(new Piece((float) (BrickGame.GRID_WIDTH / 2 - 1), 0), new Piece((float) (BrickGame.GRID_WIDTH / 2), 0), new Piece((float) (BrickGame.GRID_WIDTH / 2) + 1, 0), new Piece((float) (BrickGame.GRID_WIDTH / 2), 1)));
        bullets = new ArrayList<>();
    }

    public void moveRight(){
        timeUpdatePositions += Gdx.graphics.getDeltaTime();
        if (gun.get(gun.size() - 1).getX() + 1 <= BrickGame.GRID_WIDTH - 2 && timeUpdatePositions >= timeUpdatePositionLimit) {
            for (Piece piece : gun) piece.setX(piece.getX() + 1);
            timeUpdatePositions = 0;
        }
    }

    public void moveLeft(){
        timeUpdatePositions += Gdx.graphics.getDeltaTime();
        if (gun.get(0).getX() - 1 >= 0 && timeUpdatePositions >= timeUpdatePositionLimit) {
            for (Piece piece : gun) piece.setX(piece.getX() - 1);
            timeUpdatePositions = 0;
        }

    }

    public void shoot() {
            bullets.add(new Bullet(batch, this));
            isNeedPlayHit = true;
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
