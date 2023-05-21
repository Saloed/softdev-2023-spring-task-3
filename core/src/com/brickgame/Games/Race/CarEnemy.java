package com.brickgame.Games.Race;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.brickgame.Games.Piece;


public class CarEnemy {
    SpriteBatch batch;
    float timeUpdatePosition = 0;
    static float timeUpdatePositionLimit = 0.4f;
    Piece[] car;

    public CarEnemy(SpriteBatch batch) {
        this.batch = batch;
        int centerCar = MathUtils.random(1);
        if (centerCar == 1) centerCar = 3;
        else centerCar = 6;
        car = new Piece[]{
                new Piece(centerCar, 23), new Piece(centerCar, 22), new Piece(centerCar, 21),
                new Piece(centerCar - 1, 22), new Piece(centerCar + 1, 22),
                new Piece(centerCar - 1, 20), new Piece(centerCar + 1, 20)};
        for (Piece piece : car) piece.changeTexture();
    }

    public void updatePosition() {
        timeUpdatePosition += Gdx.graphics.getDeltaTime();
        if (timeUpdatePosition >= timeUpdatePositionLimit) {
            for (Piece piece : car) {
                piece.y--;
            }
            timeUpdatePosition = 0;
        }
    }

    public void draw() {
        for (Piece piece : car) piece.draw(batch);
    }
}
