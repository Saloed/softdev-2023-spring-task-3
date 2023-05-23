package com.brickgame.Games.Snake;

import com.badlogic.gdx.*;
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
                snake.get(i).setX(snake.get(i - 1).getX());
                snake.get(i).setY(snake.get(i - 1).getY());
            }

            // Движение головы змеи
            switch (direction) {
                case 0:
                    if (snake.get(0).getY() < 19) snake.get(0).setY(snake.get(0).getY() + 1);
                    else snake.get(0).setY(0);
                    break;
                case 1:
                    if (snake.get(0).getX() < 9) snake.get(0).setX(snake.get(0).getX() + 1);
                    else snake.get(0).setX(0);
                    break;
                case 2:
                    if (snake.get(0).getY() > 0) snake.get(0).setY(snake.get(0).getY() - 1);
                    else snake.get(0).setY(19);
                    break;
                case 3:
                    if (snake.get(0).getX() > 0) snake.get(0).setX(snake.get(0).getX() - 1);
                    else snake.get(0).setX(9);
                    break;
            }
            // Проверка столкновения головы с яблоком
            if (snake.get(0).getX() == apple.apple.getX() && snake.get(0).getY() == apple.apple.getY()) {
                snake.add(new Piece(snake.get(snake.size() - 1).getX(), snake.get(snake.size() - 1).getY()));
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
            if (snake.get(0).getX() == snake.get(i).getX() && snake.get(0).getY() == snake.get(i).getY()) {
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
