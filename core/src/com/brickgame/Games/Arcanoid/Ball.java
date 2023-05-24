package com.brickgame.Games.Arcanoid;

import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ball {
    private float dx, dy, timeUpdatePosition, timeUpdatePositionLimit = 0.2f;
    public Piece ball;
    private final SpriteBatch batch;
    public boolean isNeedPlayHit, isNeedIncreaseScore, isNeedPlayBroke;

    public Ball(SpriteBatch batch, float x, float y, float dx, float dy) {
        ball = new Piece(x, y);
        this.batch = batch;
        this.dx = dx;
        this.dy = dy;
    }

    public void updatePosition(Platform platform) {
        isNeedPlayHit = false;
        timeUpdatePosition += Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
            ball.setY(1);
            dy = 1;
            ball.setX(platform.platform[platform.platform.length / 2].getX());
        }
        if (timeUpdatePosition >= timeUpdatePositionLimit) {
            ball.setX(ball.getX() + dx);
            ball.setY(ball.getY() + dy);

            // Проверяем столкновение с границами экрана
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                timeUpdatePositionLimit = 1 / 30f;
            } else timeUpdatePositionLimit = 0.2f;
            if (ball.getX() <= 0 || ball.getX() >= BrickGame.GRID_WIDTH - 1) {
                dx = -dx;
                isNeedPlayHit = true;
                if (ball.getX() < 0) ball.setX(0);
                else if (ball.getX() > BrickGame.GRID_WIDTH -1) ball.setX(BrickGame.GRID_WIDTH - 1);
            }
            if (ball.getY() >= BrickGame.GRID_HEIGHT - 1) {
                dy = -dy;
                isNeedPlayHit = true;
                ball.setY(BrickGame.GRID_HEIGHT - 1);
            }
            checkCollisionPlatform(platform);
            timeUpdatePosition = 0;
        }
    }


    public void draw() {
        ball.draw(batch);
    }

    private void checkCollisionPlatform(Platform platform) {
        for (Piece piece : platform.platform) {
            if (ball.getY() == 1 && piece.getX() == ball.getX() && dy < 0) {
                dy = -dy;
                isNeedPlayHit = true;
                break;
            }
            //проверяем падения шарика на правый уголок платформы
            if (ball.getY() == 1 && ball.getX() + 1 == platform.platform[0].getX() && dy < 0 && dx > 0) {
                dy = -dy;
                dx = -dx;
                isNeedPlayHit = true;
                break;
            }
            // проверяем падения шарика на левый уголок платформы
            if (ball.getY() == 1 && ball.getX() - 1 == platform.platform[platform.platform.length - 1].getX() && dy < 0 && dx < 0) {
                dy = -dy;
                dx = -dx;
                isNeedPlayHit = true;
                break;
            }
        }
    }

    public void collidesWithBlocks(Blocks blocks) {
        boolean flag = false; // было ли столкновение справа/слева/сверху/снизу
        isNeedIncreaseScore = false;
        isNeedPlayBroke = false;

        // проверка столкновений сверху/снизу/справа/слева
        for (Piece block : blocks.blocks) {
            if ((ball.getY() + 1 == block.getY() || ball.getY() - 1 == block.getY()) && block.getX() == ball.getX()) {
                dy = -dy;
                flag = true;
            } else if ((ball.getX() + 1 == block.getX() || ball.getX() - 1 == block.getX()) && block.getY() == ball.getY()) {
                dx = -dx;
                flag = true;
            }
            if (flag) {
                isNeedPlayBroke = true;
                blocks.blocks.remove(block);
                isNeedIncreaseScore = true;
                break;
            }
        }
        // проверка столкновений по диагонали
        if (!flag) {
            for (Piece block : blocks.blocks) {
                if (ball.getY() + dy == block.getY() && ball.getX() + dx == block.getX()) {
                    dx = -dx;
                    dy = -dy;
                    isNeedPlayBroke = true;
                    blocks.blocks.remove(block);
                    isNeedIncreaseScore = true;
                    break;
                }
            }
        }
    }
}
