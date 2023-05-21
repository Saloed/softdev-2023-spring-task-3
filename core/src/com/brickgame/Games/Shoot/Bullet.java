package com.brickgame.Games.Shoot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.Games.Piece;

public class Bullet {

    SpriteBatch batch;
    Piece bullet;
    Gun parent;
    float timeUpdatePosition, timeUpdatePositionLimit = 0.1f;
    boolean needToDelete;

    Bullet(SpriteBatch batch, Gun parent) {
        this.batch = batch;
        this.parent = parent;
        bullet = new Piece(parent.gun.get(1).x, 2);
        needToDelete = false;
    }

    public void updatePosition() {
        timeUpdatePosition += Gdx.graphics.getDeltaTime();
        if (timeUpdatePosition >= timeUpdatePositionLimit) {
            for (Bullet bullet : parent.bullets) {
                bullet.bullet.y++;
                if (bullet.bullet.y >= 20) bullet.needToDelete = true;
            }
            timeUpdatePosition = 0;
        }
    }

    public void draw() {
        bullet.draw(batch);
    }
}
