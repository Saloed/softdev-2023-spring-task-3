package com.brickgame.Games.Arcanoid;

import com.brickgame.Games.Piece;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ball {
    public float dx, dy;
    Piece ball;
    SpriteBatch batch;
    float timeUpdatePosition, timeUpdatePositionLimit = 0.2f;

    Ball(SpriteBatch batch, float x, float y, float dx, float dy) {
        ball = new Piece(x, y);
        this.batch = batch;
        this.dx = dx;
        this.dy = dy;
    }

    public void updatePosition(Platform platform) {
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
            if (ball.getX() <= 0 || ball.getX() >= 9) {
                dx = -dx;
                ArcanoidGameScreen.game.hit.play();
                if (ball.getX() < 0) ball.setX(0);
                else if (ball.getX() > 9) ball.setX(9);
            }
            if (ball.getY() >= 19) {
                dy = -dy;
                ArcanoidGameScreen.game.hit.play();
                ball.setY(19);
            }
            checkCollisionPlatform(platform);
            timeUpdatePosition = 0;
        }
    }


    public void draw() {
        ball.draw(batch);
    }

    public void checkCollisionPlatform(Platform platform) {
        for (Piece piece : platform.platform) {
            if (ball.getY() == 1 && piece.getX() == ball.getX() && dy < 0) {
                dy = -dy;
                ArcanoidGameScreen.game.hit.play();
                break;
            }
            //проверяем падения шарика на правый уголок платформы
            if (ball.getY() == 1 && ball.getX() + 1 == platform.platform[0].getX() && dy < 0 && dx > 0) {
                dy = -dy;
                dx = -dx;
                ArcanoidGameScreen.game.hit.play();
                break;
            }
            // проверяем падения шарика на левый уголок платформы
            if (ball.getY() == 1 && ball.getX() - 1 == platform.platform[platform.platform.length - 1].getX() && dy < 0 && dx < 0) {
                dy = -dy;
                dx = -dx;
                ArcanoidGameScreen.game.hit.play();
                break;
            }
        }
    }

    public void collidesWithBlocks(Blocks blocks) {
        boolean flag = false; // было ли столкновение справа/слева/сверху/снизу

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
                ArcanoidGameScreen.game.broke.play();
                blocks.blocks.remove(block);
                ArcanoidGameScreen.sidePanel.score.increaseScore();
                break;
            }
        }
        // проверка столкновений по диагонали
        if (!flag) {
            for (Piece block : blocks.blocks) {
                if (ball.getY() + dy == block.getY() && ball.getX() + dx == block.getX()) {
                    dx = -dx;
                    dy = -dy;
                    ArcanoidGameScreen.game.broke.play();
                    blocks.blocks.remove(block);
                    ArcanoidGameScreen.sidePanel.score.increaseScore();
                    break;
                }
            }
        }
    }
}
