package com.brickgame.Games.Arcanoid;

import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Platform {
    private final SpriteBatch batch;
    public Piece[] platform;
    // x, y - координаты первой слева ячейки, она - начало отсчета для платформы(ячейка начала)
    // width - ширина платформы - кол-во ячеек от начала, включая ячейку начала

    public Platform(SpriteBatch batch, int x, int y, int width) {
        platform = new Piece[width];
        for (int i = 0; i < width; i++) {
            Piece piece = new Piece(i + x, y);
            platform[i] = piece;
        }
        this.batch = batch;
    }

    public void moveRight(){
        if (platform[platform.length - 1].getX() + 1 <= BrickGame.GRID_WIDTH - 1) {
            for (Piece piece : platform) {
                piece.setX(piece.getX() + 1);
            }
        }
    }
    public void moveLeft(){
        if (platform[0].getX() - 1 >= 0) {
            for (Piece piece : platform) {
                piece.setX(piece.getX() - 1);
            }
        }
    }

    public void draw() {
        for (Piece piece : platform) {
            piece.draw(batch);
        }
    }
}
