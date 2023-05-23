package com.brickgame.Games.Arcanoid;

import com.brickgame.Games.Piece;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Platform {
    SpriteBatch batch;
    public Piece[] platform;
    float timeStep;
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

    public void updatePosition() {
        timeStep += Gdx.graphics.getDeltaTime();
        if (timeStep >= 0.1f) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                if (platform[platform.length - 1].getX() + 1 <= 9) {
                    for (Piece piece : platform) {
                        piece.setX(piece.getX() + 1);
                    }
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                if (platform[0].getX() - 1 >= 0) {
                    for (Piece piece : platform) {
                        piece.setX(piece.getX() - 1);
                    }
                }
            }
            timeStep = 0;
        }
    }

    public void draw() {
        for (Piece piece : platform) {
            piece.draw(batch);
        }
    }
}
