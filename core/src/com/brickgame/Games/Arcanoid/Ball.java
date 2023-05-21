package com.brickgame.Games.Arcanoid;

import com.brickgame.Games.Piece;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
            ball.y = 1;
            dy = 1;
            ball.x = platform.platform[platform.platform.length / 2].x;
        }
        if (timeUpdatePosition >= timeUpdatePositionLimit) {
            ball.x += dx;
            ball.y += dy;

            // Проверяем столкновение с границами экрана
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                timeUpdatePositionLimit = 1 / 30f;
            } else timeUpdatePositionLimit = 0.2f;
            if (ball.x <= 0 || ball.x >= 9) {
                dx = -dx;
                ArcanoidGameScreen.game.hit.play();
                if (ball.x < 0) ball.x = 0;
                else if (ball.x > 9) ball.x = 9;
            }
            if (ball.y >= 19) {
                dy = -dy;
                ArcanoidGameScreen.game.hit.play();
                ball.y = 19;
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
            if (ball.y == 1 && piece.x == ball.x && dy < 0) {
                dy = -dy;
                ArcanoidGameScreen.game.hit.play();
                break;
            }
            //проверяем падения шарика на правый уголок платформы
            if (ball.y == 1 && ball.x + 1 == platform.platform[0].x && dy < 0 && dx > 0) {
                dy = -dy;
                dx = -dx;
                ArcanoidGameScreen.game.hit.play();
                break;
            }
            // проверяем падения шарика на левый уголок платформы
            if (ball.y == 1 && ball.x - 1 == platform.platform[platform.platform.length - 1].x && dy < 0 && dx < 0) {
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
            if ((ball.y + 1 == block.y || ball.y - 1 == block.y) && block.x == ball.x) {
                dy = -dy;
                flag = true;
            } else if ((ball.x + 1 == block.x || ball.x - 1 == block.x) && block.y == ball.y) {
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
                if (ball.y + dy == block.y && ball.x + dx == block.x) {
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
