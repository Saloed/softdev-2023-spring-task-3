package com.brickgame.Games.Shoot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.brickgame.Games.Piece;


public class Enemy {

    SpriteBatch batch;
    float timeUpdatePosition, timeUpdatePositionLimit = 0.4f;
    boolean killed;
    Piece[] enemy; // враг состоит из массива piece
    int centerX, hp; // центр врага по координате x

    public Enemy(SpriteBatch batch, int level) {
        this.batch = batch;
        killed = false;
        hp = MathUtils.random(1, level);
        centerX = MathUtils.random(1, 8);
        enemy = new Piece[]{new Piece(centerX - 1, 22), new Piece(centerX + 1, 22),
                new Piece(centerX, 23), new Piece(centerX, 21)};
    }

    public void updatePosition() {
        timeUpdatePosition += Gdx.graphics.getDeltaTime();
        if (timeUpdatePosition >= timeUpdatePositionLimit) {
            for (Piece piece : enemy) {
                piece.y--;
            }
            // враг рандомно решает, двигаться ли ему в сторону - 1 да, 0 -  нет
            if (MathUtils.random(1) == 1 && enemy[0].x >= 1 && enemy[1].x <= 8) {
                //если враг не у границы экрана, то он рандомно выбирает сторону движения
                if (MathUtils.random(1) == 1) {
                    for (Piece piece : enemy) {
                        piece.x++;
                    }
                } else {
                    for (Piece piece : enemy) {
                        piece.x--;
                    }
                }
            }
            if (enemy[0].x < 1) {
                centerX = 1;
                enemy[0].x = 0;
                enemy[1].x = centerX + 1;
                enemy[2].x = centerX;
                enemy[3].x = centerX;
            }
            if (enemy[1].x > 8) {
                centerX = 8;
                enemy[0].x = centerX - 1;
                enemy[1].x = centerX + 1;
                enemy[2].x = centerX;
                enemy[3].x = centerX;
            }
            timeUpdatePosition = 0;
        }
    }

    public void draw() {
        for (Piece piece : enemy) {
            piece.draw(batch);
        }
    }
}
