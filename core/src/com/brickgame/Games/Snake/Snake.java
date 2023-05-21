package com.brickgame.Games.Snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.Games.Piece;

import java.util.ArrayList;
import java.util.Arrays;

public class Snake {
    SpriteBatch batch;
    float timeUpdatePosition = 0, timeUpdatePositionLimit = 0.2f;

    int direction; // 0 - вверх, 1 - вправо, 2 - вниз, 3 - влево

    ArrayList<Piece> snake;

    public Snake(SpriteBatch batch) {
        this.batch = batch;
        snake = new ArrayList<>(Arrays.asList(new Piece(2, 0), new Piece(1, 0), new Piece(0, 0)));
        this.direction = 1;
    }

    public void updatePosition(Apple apple) {
        timeUpdatePosition += Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && direction != 1) direction = 3;
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && direction != 3) direction = 1;
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && direction != 2) direction = 0;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && direction != 0) direction = 2;

        if (timeUpdatePosition >= timeUpdatePositionLimit) {
            // Движение тела змеи
            for (int i = snake.size() - 1; i > 0; i--) {
                snake.get(i).x = snake.get(i - 1).x;
                snake.get(i).y = snake.get(i - 1).y;
            }

            // Движение головы змеи
            switch (direction) {
                case 0:
                    if (snake.get(0).y < 19) snake.get(0).y++;
                    else snake.get(0).y = 0;
                    break;
                case 1:
                    if (snake.get(0).x < 9) snake.get(0).x++;
                    else snake.get(0).x = 0;
                    break;
                case 2:
                    if (snake.get(0).y > 0) snake.get(0).y--;
                    else snake.get(0).y = 19;
                    break;
                case 3:
                    if (snake.get(0).x > 0) snake.get(0).x--;
                    else snake.get(0).x = 9;
                    break;
            }
            float endX = snake.get(snake.size() - 1).x;
            float endY = snake.get(snake.size() - 1).y;

            // Проверка столкновения головы с яблоком
            if (snake.get(0).x == apple.apple.x && snake.get(0).y == apple.apple.y) {
                Piece newPiece = new Piece(endX, endY);
                newPiece.changeTexture();
                snake.add(newPiece);
                apple.spawnApple();
                SnakeGameScreen.sidePanel.score.increaseScore();
                SnakeGameScreen.game.hit.play();
            }
            timeUpdatePosition = 0;
        }
    }

    public boolean checkSelfCollision() {
        //проверка на столкновение головы змеи с телом змеи
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                return true;
            }
        }
        return false;
    }

    public void draw() {
        for (Piece piece : snake) {
            piece.draw(batch);
        }
    }
}
