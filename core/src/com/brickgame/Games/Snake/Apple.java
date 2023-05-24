package com.brickgame.Games.Snake;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;

public class Apple {

    private final SpriteBatch batch;
   public Piece apple;
    private final Snake snake;


    public Apple(SpriteBatch batch, Snake snake) {
        this.batch = batch;
        this.snake = snake;
        int x = MathUtils.random(0, BrickGame.GRID_WIDTH - 1);
        int y = MathUtils.random(1, BrickGame.GRID_HEIGHT - 1);
        apple = new Piece(x, y);
    }


    public void spawnApple() {
        int x;
        int y;

        do {
            x = MathUtils.random(0, BrickGame.GRID_WIDTH - 1);
            y = MathUtils.random(0, BrickGame.GRID_HEIGHT - 1);
        } while (isAppleOnSnake(x, y));

        apple.setY(y);
        apple.setX(x);
    }

    private boolean isAppleOnSnake(int x, int y) {
        for (Piece piece : snake.snake) {
            if (piece.getX() == x && piece.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public void draw() {
        apple.draw(batch);
    }
}
