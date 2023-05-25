package com.brickgame.Games.Race;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;


public class CarEnemy {
    private final SpriteBatch batch;
    private float timeUpdatePosition = 0;
    public static float timeUpdatePositionLimit = 0.4f;
    public Piece[] car;

    public CarEnemy(SpriteBatch batch) {
        this.batch = batch;
        int centerCar = MathUtils.random(1);
        if (centerCar == 1) centerCar = 3;
        else centerCar = 6;
        car = new Piece[]{
                new Piece(centerCar, BrickGame.GRID_HEIGHT + 3), new Piece(centerCar, BrickGame.GRID_HEIGHT + 2), new Piece(centerCar, BrickGame.GRID_HEIGHT + 1),
                new Piece(centerCar - 1, BrickGame.GRID_HEIGHT + 2), new Piece(centerCar + 1, BrickGame.GRID_HEIGHT + 2),
                new Piece(centerCar - 1, BrickGame.GRID_HEIGHT), new Piece(centerCar + 1, BrickGame.GRID_HEIGHT)};
    }

    public void updatePosition() {
        timeUpdatePosition += Gdx.graphics.getDeltaTime();
        if (timeUpdatePosition >= timeUpdatePositionLimit) {
            for (Piece piece : car) {
                piece.setY(piece.getY() - 1);
            }
            timeUpdatePosition = 0;
        }
    }

    public void draw() {
        for (Piece piece : car) piece.draw(batch);
    }
}
