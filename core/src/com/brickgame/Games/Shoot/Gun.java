package com.brickgame.Games.Shoot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.Games.Piece;
import com.brickgame.Games.Score;

import java.util.ArrayList;
import java.util.Arrays;

public class Gun {
    ArrayList<Piece> gun;
    ArrayList<Bullet> bullets;
    SpriteBatch batch;
    Score score;
    float timeStepShoot, timeMove, timeShootLimit = 1f, timeMoveLimit = 0.1f;

    public Gun(SpriteBatch batch) {
        this.batch = batch;
        score = new Score(batch);
        gun = new ArrayList<>(Arrays.asList(new Piece(4, 0), new Piece(5, 0), new Piece(6, 0), new Piece(5, 1)));
        bullets = new ArrayList<>();
    }

    public void updatePosition() {
        timeMove += Gdx.graphics.getDeltaTime();
        if (timeMove >= timeMoveLimit) {
            if (gun.get(gun.size() - 1).x + 1 <= 8 && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                for (Piece piece : gun) piece.x++;
            }
            if (gun.get(0).x - 1 >= 0 && Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                for (Piece piece : gun) piece.x--;
            }
            timeMove = 0;
        }
    }

    public void shoot() {
        timeStepShoot += Gdx.graphics.getDeltaTime();
        if (timeStepShoot >= timeShootLimit) {
            bullets.add(new Bullet(batch, this));
            timeStepShoot = 0;
            ShootGameScreen.game.hit.play();
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
    public void iskillEnemy(Enemy enemy) {
        for (Piece e : enemy.enemy) {
            for (Bullet b : bullets) {
                if (b.bullet.x == e.x && b.bullet.y == e.y) {
                    enemy.hp--;
                    ShootGameScreen.game.broke.play();
                    if (enemy.hp == 0) {
                        ShootGameScreen.sidePanel.score.increaseScore();
                        enemy.killed = true;
                    }
                    b.bullet.y = 30;
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
