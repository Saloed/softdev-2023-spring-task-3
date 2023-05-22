package com.brickgame.Games.Shoot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.brickgame.Games.Piece;


public class Enemy {

    SpriteBatch batch;
    float timeUpdatePosition, timeUpdatePositionLimit = 0.4f;
    boolean killed;
    Piece[] enemy;
    int centerX, hp;

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
                piece.setY(piece.getY() - 1);
            }
            // враг рандомно решает, двигаться ли ему в сторону - 1 да, 0 -  нет
            if (MathUtils.random(1) == 1 && enemy[0].getX() >= 1 && enemy[1].getX() <= 8) {
                //если враг не у границы экрана, то он рандомно выбирает сторону движения
                if (MathUtils.random(1) == 1) {
                    for (Piece piece : enemy) {
                        piece.setX(piece.getX() + 1);
                    }
                } else {
                    for (Piece piece : enemy) {
                        piece.setX(piece.getX() - 1);
                    }
                }
            }
            if (enemy[0].getX() < 1) {
                centerX = 2;
                enemy[0].setX(centerX - 1);
                enemy[1].setX(centerX + 1);
                enemy[2].setX(centerX);
                enemy[3].setX(centerX);
            }
            if (enemy[1].getX() > 8) {
                centerX = 7;
                enemy[0].setX(centerX - 1);
                enemy[1].setX(centerX + 1);
                enemy[2].setX(centerX);
                enemy[3].setX(centerX);
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
