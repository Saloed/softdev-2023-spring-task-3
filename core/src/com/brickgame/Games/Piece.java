package com.brickgame.Games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.Games.Tetris.Direction;

import java.util.Arrays;
import java.util.List;

public class Piece {
    public float x, y;
    public Texture onTexture = new Texture(Gdx.files.internal("D:\\BrickSim\\assets\\BrickOn.png"));
    public Texture texturePiece;

    public List<Direction> directions;
    public int distance;

    public static float SIZE = 32f;

    public Piece(float x, float y) {
        this.x = x;
        this.y = y;
        texturePiece = onTexture;
    }

    public void changeTexture() {
       texturePiece = onTexture;
    }

    public void setDirection(Direction... directions) {
        this.directions = Arrays.asList(directions);
    }

    public void draw(SpriteBatch batch) {
        texturePiece.bind();
        batch.draw(texturePiece, x * Piece.SIZE, y * Piece.SIZE, SIZE, SIZE);
    }
}
