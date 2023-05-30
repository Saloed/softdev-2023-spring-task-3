package com.brickgame.Games.Snake;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;

import java.util.*;

public class Snake {
    private final SpriteBatch batch;
    public float timeUpdatePosition = 0, timeUpdatePositionLimit;

    public int direction; // 0 - вверх, 1 - вправо, 2 - вниз, 3 - влево

    public List<Piece> snake;
    public boolean isNeedHitPlay, isNeedIncreaseScore;

    public Snake(SpriteBatch batch) {
        this.batch = batch;
        timeUpdatePositionLimit = 0.2f;
        snake = new ArrayList<>(Arrays.asList(new Piece(2, 0), new Piece(1, 0), new Piece(0, 0)));
        this.direction = 1;
    }

    public void updatePosition(Apple apple) {
        isNeedHitPlay = false;
        isNeedIncreaseScore = false;
        timeUpdatePosition += Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && direction != 1) direction = 3;
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && direction != 3) direction = 1;
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && direction != 2) direction = 0;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && direction != 0) direction = 2;

        float endX = snake.get(snake.size()-1).getX(), endY = snake.get(snake.size()-1).getY();
        if (timeUpdatePosition >= timeUpdatePositionLimit) {
            // Движение тела змеи
            for (int i = snake.size() - 1; i > 0; i--) {
                snake.get(i).setX(snake.get(i - 1).getX());
                snake.get(i).setY(snake.get(i - 1).getY());
            }

            // Движение головы змеи
            switch (direction) {
                case 0:
                    if (snake.get(0).getY() < BrickGame.GRID_HEIGHT - 1) snake.get(0).setY(snake.get(0).getY() + 1);
                    else snake.get(0).setY(0);
                    break;
                case 1:
                    if (snake.get(0).getX() < BrickGame.GRID_WIDTH - 1) snake.get(0).setX(snake.get(0).getX() + 1);
                    else snake.get(0).setX(0);
                    break;
                case 2:
                    if (snake.get(0).getY() > 0) snake.get(0).setY(snake.get(0).getY() - 1);
                    else snake.get(0).setY(BrickGame.GRID_HEIGHT - 1);
                    break;
                case 3:
                    if (snake.get(0).getX() > 0) snake.get(0).setX(snake.get(0).getX() - 1);
                    else snake.get(0).setX(BrickGame.GRID_WIDTH - 1);
                    break;
            }
            // Проверка столкновения головы с яблоком
            if (snake.get(0).getX() == apple.apple.getX() && snake.get(0).getY() == apple.apple.getY()) {
                snake.add(new Piece(endX, endY));
                apple.spawnApple();
                isNeedIncreaseScore = true;
                isNeedHitPlay = true;
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
