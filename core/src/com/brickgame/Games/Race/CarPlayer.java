package com.brickgame.Games.Race;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.brickgame.Games.Piece;

import java.util.ArrayList;

public class CarPlayer {
    private final SpriteBatch batch;
    public final Piece[] car;
    public boolean isNeedPlayHit, isNeedIncreaseScore;

    public CarPlayer(SpriteBatch batch) {
        this.batch = batch;
        int centerCar = MathUtils.random(1);
        if (centerCar == 1) centerCar = 3;
        else centerCar = 6;
        car = new Piece[]{
                new Piece(centerCar, 3), new Piece(centerCar, 2), new Piece(centerCar, 1),
                new Piece(centerCar - 1, 2), new Piece(centerCar + 1, 2),
                new Piece(centerCar - 1, 0), new Piece(centerCar + 1, 0)};
    }


    public void moveRight() {
        if (car[0].getX() == 3) {
            for (Piece piece : car) piece.setX(piece.getX() + 3);
        }
    }

    public void moveLeft() {
        if (car[0].getX() == 6) {
            for (Piece piece : car) piece.setX(piece.getX() - 3);
        }
    }

    public boolean checkCrash(ArrayList<CarEnemy> carEnemies) {
        for (CarEnemy carEnemy : carEnemies) {
            if (carEnemy.car[0].getX() == car[0].getX() && carEnemy.car[6].getY() <= car[0].getY()) return true;
        }
        return false;
    }

    public void deleteCarEnemy(ArrayList<CarEnemy> carEnemies) {
        isNeedPlayHit = false;
        isNeedIncreaseScore = false;
        for (int i = carEnemies.size() - 1; i >= 0; --i) {
            if (carEnemies.get(i).car[0].getY() < 0) {
                carEnemies.remove(i);
                isNeedPlayHit = true;
                isNeedIncreaseScore = true;
            }
        }
    }

    public void draw() {
        for (Piece piece : car) piece.draw(batch);
    }
}
