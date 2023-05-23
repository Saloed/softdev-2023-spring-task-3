package com.brickgame.Games;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.Games.Tetris.Direction;

import java.util.Arrays;
import java.util.List;

public class Piece {
    private float x, y;
    private final Texture texturePiece;

    public List<Direction> directions;
    private int distance;

    public static float SIZE = 32f;

    public Piece(float x, float y) {
        this.x = x;
        this.y = y;
        texturePiece = new Texture("BrickOn.png");
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getDistance() {
        return distance;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setDirection(Direction... directions) {
        this.directions = Arrays.asList(directions);
    }

    public void draw(SpriteBatch batch) {
        texturePiece.bind();
        batch.draw(texturePiece, x * Piece.SIZE, y * Piece.SIZE, SIZE, SIZE);
    }
}
