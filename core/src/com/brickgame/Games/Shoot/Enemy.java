package com.brickgame.Games.Shoot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;


public class Enemy {

    private final SpriteBatch batch;
    private float timeUpdatePosition, timeUpdatePositionLimit = 0.4f;
    public boolean killed;
    public Piece[] enemy;
    private int centerX;
    public int hp;

    public Enemy(SpriteBatch batch, int level) {
        this.batch = batch;
        killed = false;
        hp = MathUtils.random(1, level);
        centerX = MathUtils.random(1, 8);
        enemy = new Piece[]{new Piece(centerX - 1, BrickGame.GRID_HEIGHT + 2), new Piece(centerX + 1, BrickGame.GRID_HEIGHT + 2),
                new Piece(centerX, BrickGame.GRID_HEIGHT + 3), new Piece(centerX, BrickGame.GRID_HEIGHT + 1)};
    }

    public void updatePosition() {
        timeUpdatePosition += Gdx.graphics.getDeltaTime();
        if (timeUpdatePosition >= timeUpdatePositionLimit) {
            for (Piece piece : enemy) {
                piece.setY(piece.getY() - 1);
            }
            // враг рандомно решает, двигаться ли ему в сторону - 1 да, 0 -  нет
            if (MathUtils.random(1) == 1 && enemy[0].getX() >= 1 && enemy[1].getX() <= BrickGame.GRID_WIDTH - 2) {
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
            if (enemy[1].getX() > BrickGame.GRID_WIDTH - 2) {
                centerX = BrickGame.GRID_WIDTH - 3;
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
