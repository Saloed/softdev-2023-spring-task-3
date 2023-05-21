package com.brickgame.Games.Snake;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.brickgame.Games.Piece;

public class Apple {

    SpriteBatch batch;
    Piece apple;
    Snake snake;


    public Apple(SpriteBatch batch, Snake snake) {
        this.batch = batch;
        this.snake = snake;
        int x = MathUtils.random(0, 9);
        int y = MathUtils.random(1, 19);
        apple = new Piece(x, y);
    }


    public void spawnApple() {
        int x;
        int y;

        do {
            x = MathUtils.random(0, 9);
            y = MathUtils.random(0, 19);
        } while (isAppleOnSnake(x, y));

        apple.y = y;
        apple.x = x;
    }

    private boolean isAppleOnSnake(int x, int y) {
        for (Piece piece : snake.snake) {
            if (piece.x == x && piece.y == y) {
                return true;
            }
        }
        return false;
    }

    public void draw() {
        apple.draw(batch);
    }
}
