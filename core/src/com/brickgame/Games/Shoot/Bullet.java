package com.brickgame.Games.Shoot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brickgame.BrickGame;
import com.brickgame.Games.Piece;

public class Bullet {

    private final SpriteBatch batch;
    public final Piece bullet;
    private final Gun parent;
    public float timeUpdatePosition, timeUpdatePositionLimit;
    public boolean needToDelete;

    public Bullet(SpriteBatch batch, Gun parent) {
        this.batch = batch;
        timeUpdatePositionLimit = 0.1f;
        this.parent = parent;
        bullet = new Piece(parent.gun.get(1).getX(), 2);
        needToDelete = false;
    }

    public void updatePosition() {
        timeUpdatePosition += Gdx.graphics.getDeltaTime();
        if (timeUpdatePosition >= timeUpdatePositionLimit) {
            for (Bullet bullet : parent.bullets) {
                bullet.bullet.setY(bullet.bullet.getY() + 1);
                if (bullet.bullet.getY() >= BrickGame.GRID_HEIGHT) bullet.needToDelete = true;
            }
            timeUpdatePosition = 0;
        }
    }

    public void draw() {
        bullet.draw(batch);
    }
}
