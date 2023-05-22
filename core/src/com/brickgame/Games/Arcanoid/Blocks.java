package com.brickgame.Games.Arcanoid;

import com.badlogic.gdx.math.MathUtils;
import com.brickgame.Games.Piece;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Arrays;

public class Blocks {
    SpriteBatch batch;
    ArrayList<Piece> blocks;
    public int level;
    ArrayList<Piece> heart = new ArrayList<>(
            Arrays.asList(
                    new Piece(4, 14), new Piece(5, 14), new Piece(3, 15), new Piece(6, 15), new Piece(2, 16),
                    new Piece(7, 16), new Piece(1, 17), new Piece(8, 17), new Piece(1, 18), new Piece(8, 18),
                    new Piece(4, 18), new Piece(5, 18), new Piece(2, 19), new Piece(3, 19), new Piece(6, 19),
                    new Piece(7, 19)));

    ArrayList<Piece> wall = new ArrayList<>(Arrays.asList(
            new Piece(0, 15), new Piece(0, 16), new Piece(0, 17), new Piece(0, 18), new Piece(0, 19),
            new Piece(1, 15), new Piece(1, 16), new Piece(1, 17), new Piece(1, 18), new Piece(1, 19),
            new Piece(2, 15), new Piece(2, 16), new Piece(2, 17), new Piece(2, 18), new Piece(2, 19),
            new Piece(3, 15), new Piece(3, 16), new Piece(3, 17), new Piece(3, 18), new Piece(3, 19),
            new Piece(4, 15), new Piece(4, 16), new Piece(4, 17), new Piece(4, 18), new Piece(4, 19),
            new Piece(5, 15), new Piece(5, 16), new Piece(5, 17), new Piece(5, 18), new Piece(5, 19),
            new Piece(6, 15), new Piece(6, 16), new Piece(6, 17), new Piece(6, 18), new Piece(6, 19),
            new Piece(7, 15), new Piece(7, 16), new Piece(7, 17), new Piece(7, 18), new Piece(7, 19),
            new Piece(8, 15), new Piece(8, 16), new Piece(8, 17), new Piece(8, 18), new Piece(8, 19),
            new Piece(9, 15), new Piece(9, 16), new Piece(9, 17), new Piece(9, 18), new Piece(9, 19)));
    ArrayList<Piece> turtle = new ArrayList<>(Arrays.asList(
            new Piece(1, 13), new Piece(2, 13), new Piece(5, 13), new Piece(6, 13), new Piece(1, 14),
            new Piece(2, 14), new Piece(3, 14), new Piece(4, 14), new Piece(5, 14), new Piece(6, 14),
            new Piece(0, 15), new Piece(1, 15), new Piece(2, 15), new Piece(3, 15), new Piece(4, 15),
            new Piece(5, 15), new Piece(6, 15), new Piece(7, 15), new Piece(8, 15), new Piece(1, 16),
            new Piece(2, 16), new Piece(3, 16), new Piece(4, 16), new Piece(5, 16), new Piece(6, 16),
            new Piece(7, 16), new Piece(8, 16), new Piece(9, 16), new Piece(1, 17), new Piece(2, 17),
            new Piece(3, 17), new Piece(4, 17), new Piece(5, 17), new Piece(6, 17), new Piece(7, 17),
            new Piece(9, 17), new Piece(2, 18), new Piece(3, 18), new Piece(4, 18), new Piece(5, 18),
            new Piece(7, 18), new Piece(8, 18), new Piece(3, 19), new Piece(4, 19)));


    Blocks(SpriteBatch batch) {
        this.batch = batch;
        level = 1;
        blocks = wall;
    }

    public void changeLevel() {
        level++;
        switch (level) {
            case 2:
                blocks = heart;
                break;
            case 3:
                blocks = turtle;
                break;
            default:
                blocks = new ArrayList<>();
                for (int x = 0; x < 10; x++)
                    for (int y = 5; y < 20; y++) {
                        if (MathUtils.random(1) == 1) blocks.add(new Piece(x, y));
                    }
                break;
        }
    }

    public void draw() {
        for (Piece piece : blocks) {
            piece.draw(batch);
        }
    }
}
