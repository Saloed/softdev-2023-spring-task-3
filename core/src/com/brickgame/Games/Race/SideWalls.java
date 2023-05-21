package com.brickgame.Games.Race;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.Games.Piece;

import java.util.ArrayList;
import java.util.Arrays;

public class SideWalls {
    SpriteBatch batch;
    float timeUpdatePosition = 0;
    static float timeUpdatePositionLimit = 0.4f;

    ArrayList<Piece> sideWalls;

    public SideWalls(SpriteBatch batch) {
        this.batch = batch;
        sideWalls = new ArrayList<>(Arrays.asList(
                new Piece(0, 19), new Piece(0, 18), new Piece(0, 17),
                new Piece(0, 15), new Piece(0, 14), new Piece(0, 13),
                new Piece(0, 11), new Piece(0, 10), new Piece(0, 9),
                new Piece(0, 7), new Piece(0, 6), new Piece(0, 5),
                new Piece(0, 3), new Piece(0, 2), new Piece(0, 1),
                new Piece(9, 19), new Piece(9, 18), new Piece(9, 17),
                new Piece(9, 15), new Piece(9, 14), new Piece(9, 13),
                new Piece(9, 11), new Piece(9, 10), new Piece(9, 9),
                new Piece(9, 7), new Piece(9, 6), new Piece(9, 5),
                new Piece(9, 3), new Piece(9, 2), new Piece(9, 1)));
    }

    public void updatePosition() {
        timeUpdatePosition += Gdx.graphics.getDeltaTime();
        if (timeUpdatePosition >= timeUpdatePositionLimit) {
            for (Piece piece : sideWalls) {
                piece.y--;
                if (piece.y < 0) piece.y = 20;
            }
            timeUpdatePosition = 0;
        }
    }

    public void draw() {
        for (Piece piece : sideWalls) {
            piece.draw(batch);
        }
    }
}
